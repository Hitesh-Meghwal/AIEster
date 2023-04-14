package com.example.aiester

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash2)
//        coroutines
        Handler(Looper.getMainLooper()).postDelayed({
            val mainactivitys = Intent(this,MainActivity::class.java)
            startActivity(mainactivitys)
            finish()
        },2000)
    }
}