package com.lotte.dcx.security

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.view.WindowManager
import java.io.File
import java.security.MessageDigest

/** 보안 솔루션
 * 1. 루팅탐지
 * 2. 무결섬 검증
 * 3. 화면 보호(백그라운드, 캡처방지)
 * */
class RootDetection {
    /** 1. 루팅 탐지 */
    //TODO: Coroutine 처리
    fun isDeviceRooted(context: Context): Boolean {
        return isRootPackagesInstalled(context) || hasRootBinaries() || canExecuteRootCommands()
    }

    /** 루팅관련 패키지 설치 확인 - (루팅 후 생성 되는 파일 확인) */
    private fun isRootPackagesInstalled(context: Context): Boolean {
        val rootPackages = listOf(
            "com.topjohnwu.magisk", // Magisk
            "me.weishu.kernelsu",   // KernelSU
            "me.bmax.apatch"        // APatch
        )
        val packageManager = context.packageManager
        return rootPackages.any { packageName ->
            try {
                packageManager.getPackageInfo(packageName, 0)
                true // 루팅 관련 패키지가 설치됨
            } catch (e: PackageManager.NameNotFoundException) {
                false // 패키지가 설치되지 않음
            }
        }
    }

    /** 루트 바이너리 존재하는지 확인 */
    private fun hasRootBinaries(): Boolean {
        val rootBinaries = listOf(
            "/system/bin/su",
            "/system/xbin/su",
            "/sbin/su",
            "/system/su",
            "/system/bin/magisk",
            "/system/xbin/magisk",
            "/system/app/Superuser.apk",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/data/adb/magisk/",
            "/data/adb/magisk.db",
            "/data/adb/shamiko/",
            "/data/adb/zygisksu/",
            "/data/adb/service.d/",
            "/data/adb/modules/",
            "/data/adb/ksu/",
            "/data/adb/ksud",
            "/data/adb/ap/",
            "/data/adb/apd",
            "/data/adb/kpatch"
        )
        return rootBinaries.any { filePath ->
            File(filePath).exists()
        }
    }

    /** 루트 명령어 실행 가능 여부 확인 */
    private fun canExecuteRootCommands(): Boolean {
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            val result = process.inputStream.bufferedReader().readLine()
            result != null
        } catch (e: Exception) {
            false
        }
    }

    /** ========================================================================================= */
    /** 2. 무결성 체크 */
    fun getAppSignature(context: Context): String? {
        return try {
            val packageManager = context.packageManager
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                // API 28 이상: signingInfo 사용
                //TODO: TargetSDK 35로 올리면 해당부분 이슈
                packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNING_CERTIFICATES).signingInfo.apkContentsSigners
            } else {
                // API 27 이하: GET_SIGNATURES 사용
                packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES).signatures
            }

            // 첫 번째 서명을 사용하여 해시 계산
            //TODO: TargetSDK 35로 올리면 해당부분 이슈
            val signature = packageInfo[0].toByteArray()
            val digest = MessageDigest.getInstance("SHA-256")
            val hashBytes = digest.digest(signature)

            // 16진수 문자열로 변환
            val sb = StringBuilder()
            for (b in hashBytes) {
                sb.append(String.format("%02x", b))
            }
            sb.toString()
        } catch (e: Exception) {
            SLog.printStackTrace(e)
            null // 예외 발생 시 null 반환
        }
    }


    /** ========================================================================================= */
    /** 3. 백그라운드 화면보호 */
    fun setScreenProtect(activity: Activity, result: ((Boolean) -> Unit) = {}) {
        try {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
            result(true)
        } catch (e: Exception) {
            SLog.printStackTrace(e)
            result(false)
        }
    }

    fun removeScreenProtect(activity: Activity, result: ((Boolean) -> Unit) = {}) {
        try {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
            result(true)
        } catch (e: Exception) {
            SLog.printStackTrace(e)
            result(false)
        }
    }
}