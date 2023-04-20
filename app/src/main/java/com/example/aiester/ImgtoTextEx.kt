package com.example.aiester

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

@Suppress("DEPRECATION")
class ImgtoTextEx : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imgto_text_ex)

        val captureimg = findViewById<Button>(R.id.Captureimg)
        captureimg.setOnClickListener {
            val capture = Intent(this,Capture::class.java)
            startActivity(capture)
        }
        val gallery = findViewById<Button>(R.id.gallery)
        gallery.setOnClickListener {
            val galleryimg = Intent(this,Gallery::class.java)
            startActivity(galleryimg)
        }
    }

}