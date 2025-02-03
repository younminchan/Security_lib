package com.dcx.security

import android.util.Log
import com.lotte.dcx.security.BuildConfig

//TODO: internal: 같은 패키지 내부만 접근 가능
internal object SLog {
    /** Log Level Error **/
    fun e(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg)
        }
    }

    /** Log Level Warning **/
    fun w(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, msg)
        }
    }

    /** Log Level Debug **/
    fun d(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg)
        }
    }

    /** Log Level Information **/
    fun i(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, msg)
        }
    }

    /** Log Level Verbose **/
    fun v(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, msg)
        }
    }

    /** Exception */
    fun printStackTrace(e: Exception) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace()
        }
    }
}