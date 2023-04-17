package com.example.aiester

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import java.util.Locale


@Suppress("DEPRECATION")
class Translator : AppCompatActivity() {

    private val fromlanguages = arrayOf<String>("From","English","Afrikaans","Arabic","Belarusian","Bulgarian","Bengali","Catalan","Czech","Dutch","Danish","French","Greek","Gujarati","Italian","Indonesian","Hindi","Urdu","Marathi","Russian","Welsh","Tamil")
    private val tolanguages = arrayOf<String>("To","English","Afrikaans","Arabic","Belarusian","Bulgarian","Bengali","Catalan","Czech","Dutch","Danish","French","Greek","Gujarati","Italian","Indonesian","Hindi","Urdu","Marathi","Russian","Welsh","Tamil")

    private val REQUEST_PERMISSION_CODE = 1
    private var languageCode = 0
    private var fromlanguageCode = 0
    private var tolanguageCode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translator)

        val fromspinner = findViewById<Spinner>(R.id.fromspinner)
        val tospinner = findViewById<Spinner>(R.id.tospinner)
        val sourcetext = findViewById<TextInputEditText>(R.id.idedittext)
        val mic = findViewById<ImageView>(R.id.micimg)
        val translatebtn = findViewById<Button>(R.id.translatebtn)
        val translatedtv = findViewById<TextView>(R.id.translatedtextid)
        val translatedmic = findViewById<ImageView>(R.id.translatedmicid)

//        fromspinner
        val fromadapterView = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, fromlanguages)
        fromspinner.adapter = fromadapterView
        fromspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                fromlanguageCode = getLanguageCode(fromlanguages[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

//        tospinner
        val toadapterView = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,tolanguages)
        tospinner.adapter = toadapterView
        tospinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                tolanguageCode = getLanguageCode(tolanguages[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
//        translatebutton
     translatebtn.setOnClickListener {
         if (sourcetext.text.toString().isEmpty()){
             Toast.makeText(this, "Please enter your text to translate", Toast.LENGTH_SHORT).show()
         }
         else if (fromlanguageCode == 0){
             Toast.makeText(this, "Please select source language", Toast.LENGTH_SHORT).show()
         }
         else if (tolanguageCode == 0){
             Toast.makeText(this, "Please select language to make translation", Toast.LENGTH_SHORT).show()
         }
         else{
             translatetext(fromlanguageCode,tolanguageCode,sourcetext.text.toString())
         }
     }

//        mic click on event
        mic.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak to convert into text")
            try {
                startActivityForResult(intent,REQUEST_PERMISSION_CODE)
            }
            catch (e:Exception){
                e.printStackTrace()
                Toast.makeText(this, ""+e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

//    getLanguageCode function which contains languagecode of languages
    fun getLanguageCode(language: String) : Int {
        return when(language){
            "English" -> FirebaseTranslateLanguage.EN
            "Afrikaans" -> FirebaseTranslateLanguage.AF
            "Belarusian" -> FirebaseTranslateLanguage.BE
            "Bulgarian" -> FirebaseTranslateLanguage.BG
            "Bengali" -> FirebaseTranslateLanguage.BN
            "Catalan" -> FirebaseTranslateLanguage.CA
            "Czech" -> FirebaseTranslateLanguage.CS
            "Dutch" -> FirebaseTranslateLanguage.NL
            "Danish" -> FirebaseTranslateLanguage.DA
            "French" -> FirebaseTranslateLanguage.FR
            "Greek" -> FirebaseTranslateLanguage.EL
            "Gujarati" -> FirebaseTranslateLanguage.GU
            "Italian" -> FirebaseTranslateLanguage.IT
            "Indonesian" -> FirebaseTranslateLanguage.ID
            "Hindi" -> FirebaseTranslateLanguage.HI
            "Urdu" -> FirebaseTranslateLanguage.UR
            "Marathi" -> FirebaseTranslateLanguage.MR
            "Russian" -> FirebaseTranslateLanguage.RU
            "Welsh" -> FirebaseTranslateLanguage.CY
            "Tamil" -> FirebaseTranslateLanguage.TA

            else -> FirebaseTranslateLanguage.EN
        }
    }
    fun translatetext(fromlanguageCode:Int, tolanguageCode:Int,source:String){

    }

}