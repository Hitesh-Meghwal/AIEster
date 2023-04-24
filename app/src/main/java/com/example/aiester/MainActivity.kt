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

//        ImgtoTextEx activity will be open
        val img2text = findViewById<Button>(R.id.Ima2text)
        img2text.setOnClickListener {
            val img2txtclass = Intent(this,ImgtoTextEx::class.java)
            startActivity(img2txtclass)
        }

//        Imagegenerator activity will be open
        val imggenerator = findViewById<Button>(R.id.imggenerator)
        imggenerator.setOnClickListener {
            val imggeneratorclass = Intent(this,Imagegenerator::class.java)
            startActivity(imggeneratorclass)
        }
    }
}