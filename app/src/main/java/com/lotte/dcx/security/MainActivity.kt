package com.lotte.dcx.security

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        /** 1. 루팅체크 */
        if (RootDetection().isDeviceRooted(this@MainActivity)) {
            Handler().postDelayed({
                Toast.makeText(this@MainActivity, "루팅이 감지되었습니다.", Toast.LENGTH_SHORT).show()
                finishAffinity()
            }, 1000)
        }

        /** 2. 무결성 Signkey 가져오기 */
        var appSignatrue = RootDetection().getAppSignature(this@MainActivity)
        Log.i("YMC", "appSignatrue: $appSignatrue")

        /** 3. 캡처방지 */
        RootDetection().setScreenProtect(this@MainActivity, result = { result ->
            Log.i("YMC", "appSignatrue: ${if(result) "성공" else "실패"}")
            Toast.makeText(this@MainActivity, "setScreenProtect ${if(result) "성공" else "실패"}", Toast.LENGTH_SHORT).show()
        })

        Handler(Looper.getMainLooper()).postDelayed({
            RootDetection().removeScreenProtect(this@MainActivity, result = { result ->
                Log.i("YMC", "removeScreenProtect: ${if(result) "성공" else "실패"}")
                Toast.makeText(this@MainActivity, "removeScreenProtect ${if(result) "성공" else "실패"}", Toast.LENGTH_SHORT).show()
            })
        },3000)
    }
}