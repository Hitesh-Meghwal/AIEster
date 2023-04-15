package com.example.aiester

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        translator activity will be open
        val translatorbtn = findViewById<Button>(R.id.translator)
        translatorbtn.setOnClickListener {
            val translatorintent = Intent(this,Translator::class.java)
            startActivity(translatorintent)
        }
    }
}