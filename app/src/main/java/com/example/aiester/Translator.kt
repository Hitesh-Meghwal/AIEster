package com.example.aiester

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.view.View
import android.widget.*
import androidx.core.content.getSystemService
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import org.w3c.dom.Text
import java.util.Locale


@Suppress("DEPRECATION")
class Translator : AppCompatActivity() {

    lateinit var tts : TextToSpeech
    lateinit var clipboardManager: ClipboardManager
    lateinit var fromspinner : Spinner
    lateinit var tospinner : Spinner
    lateinit var switcharrow : ImageView
    lateinit var sourcetext : TextInputEditText
    lateinit var mic : ImageView
    lateinit var translatebtn : Button
    lateinit var translatedtv : TextView
    lateinit var translatedmic : ImageView

    private val fromlanguages = arrayOf<String>("From","English","Afrikaans","Arabic","Belarusian","Bulgarian","Bengali","Catalan","Czech","Dutch","Danish","French","Greek","Gujarati","Italian","Indonesian","Hindi","Urdu","Marathi","Russian","Welsh","Tamil")
    private val tolanguages = arrayOf<String>("To","English","Afrikaans","Arabic","Belarusian","Bulgarian","Bengali","Catalan","Czech","Dutch","Danish","French","Greek","Gujarati","Italian","Indonesian","Hindi","Urdu","Marathi","Russian","Welsh","Tamil")

    private val REQUEST_PERMISSION_CODE = 1
    private var fromlanguageCode = 0
    private var tolanguageCode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translator)
        fromspinner = findViewById(R.id.fromspinner)
        tospinner = findViewById(R.id.tospinner)
        switcharrow = findViewById(R.id.bidirectionalarrow)
        sourcetext = findViewById(R.id.idedittext)
        mic = findViewById(R.id.micimg)
        translatebtn = findViewById(R.id.translatebtn)
        translatedtv = findViewById(R.id.translatedtextid)
        translatedmic = findViewById(R.id.translatedmicid)

//        fromspinner
        val fromadapterView =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, fromlanguages)
        fromspinner.adapter = fromadapterView
        fromspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                fromlanguageCode = getLanguageCode(fromlanguages[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

//        tospinner
        val toadapterView = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tolanguages)
        tospinner.adapter = toadapterView
        tospinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                tolanguageCode = getLanguageCode(tolanguages[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

//        translatebutton
        translatebtn.setOnClickListener {
            if (sourcetext.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter your text to translate", Toast.LENGTH_SHORT)
                    .show()
            } else if (fromlanguageCode == 0 && tolanguageCode == 0) {
                Toast.makeText(this, "Please select language", Toast.LENGTH_SHORT).show()
            } else {
                translatetext(fromlanguageCode, tolanguageCode, sourcetext.text.toString())
            }
        }

//        mic click on event
        mic.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())      //default language
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to convert into text")  //used for prompt a msg
            try {
                startActivityForResult(intent, REQUEST_PERMISSION_CODE)
            } catch (e: Exception) {
                e.printStackTrace()  //handles the error by specifying line no and class
                Toast.makeText(this, "" + e.message, Toast.LENGTH_SHORT).show()
            }
        }

//        translatedmic event
        translatedmic.setOnClickListener {
            tts = TextToSpeech(this, TextToSpeech.OnInitListener {
                if (it == TextToSpeech.SUCCESS){
                    tts.language = Locale.getDefault()
                    tts.setSpeechRate(1.0f)
                    tts.speak(translatedtv.text.toString(),TextToSpeech.QUEUE_ADD,null)
                }
            })
        }

//        clip board service used for copy and paste
        clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        translatedtv.setOnClickListener{
            clipboardManager.setPrimaryClip(ClipData.newPlainText("Language",translatedtv.text.toString()))
            Toast.makeText(this, "Text Copied", Toast.LENGTH_SHORT).show()
        }

//      switch between language just by using bidirectional arrow
        switcharrow.setOnClickListener{
            var fromlanguageindex = fromspinner.selectedItemPosition
            var tolanguageindex = tospinner.selectedItemPosition

            fromspinner.setSelection(tolanguageindex)
            tospinner.setSelection(fromlanguageindex)
        }

    }

        @Deprecated("Deprecated in Java")
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == REQUEST_PERMISSION_CODE) {
                if (resultCode == RESULT_OK && data != null) {
                    val result: ArrayList<String>? = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    val sourcetext = findViewById<TextInputEditText>(R.id.idedittext)
                    val recognizedText = result?.get(0)
                    val editable= Editable.Factory.getInstance().newEditable(recognizedText)
                    sourcetext.text = editable
                }
            }
        }

        //    getLanguageCode function which contains languagecode of languages
        fun getLanguageCode(language: String): Int {
            return when (language) {
                "English" -> FirebaseTranslateLanguage.EN
                "Afrikaans" -> FirebaseTranslateLanguage.AF
                "Arabic" -> FirebaseTranslateLanguage.AR
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

        fun translatetext(fromlanguageCode: Int, tolanguageCode: Int, source: String) {
            val translatedtv = findViewById<TextView>(R.id.translatedtextid)
            translatedtv.text = "Downloading Modal..."
            val options = FirebaseTranslatorOptions.Builder()
                .setSourceLanguage(fromlanguageCode)
                .setTargetLanguage(tolanguageCode)
                .build()

            val translator = FirebaseNaturalLanguage.getInstance().getTranslator(options)
            val conditions = FirebaseModelDownloadConditions.Builder().build()
            translator.downloadModelIfNeeded(conditions).addOnSuccessListener {
                translatedtv.text = "Translating..."
                translator.translate(source).addOnSuccessListener { translatedtext ->
                    translatedtv.text = translatedtext
                }.addOnFailureListener { exception ->
                    translatedtv.text = "Translation error: ${exception.message}"
                }
            }.addOnFailureListener { exception ->
                translatedtv.text = "Model download error ${exception.message}"
            }
        }
}