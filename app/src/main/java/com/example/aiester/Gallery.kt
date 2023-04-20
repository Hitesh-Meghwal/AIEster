package com.example.aiester

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

@Suppress("DEPRECATION")
class Gallery : AppCompatActivity() {
    lateinit var captureiv : ImageView
    lateinit var detectbtn : Button
    lateinit var detectedtv : TextView
    lateinit var opengal : Button
    lateinit var bitmap :Bitmap
    lateinit var clipboardManager: ClipboardManager
    val PICK_IMAGE_REQUEST = 1

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        captureiv = findViewById(R.id.captureiv)
        opengal = findViewById(R.id.opengal)
        detectedtv = findViewById(R.id.resultedtext)

        opengal.setOnClickListener {
            val galleryimg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryimg,PICK_IMAGE_REQUEST)
        }

        detectbtn = findViewById(R.id.detecttext)
        detectbtn.setOnClickListener {
            detecttext()
        }

        clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        detectedtv.setOnClickListener {
            clipboardManager.setPrimaryClip(ClipData.newPlainText("Text",detectedtv.text.toString()))
            Toast.makeText(this, "Text Copied", Toast.LENGTH_SHORT).show()
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==PICK_IMAGE_REQUEST && resultCode== RESULT_OK && data!=null && data.data !=null) run {
            val imageuri: Uri = data.data!!
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver,imageuri)
                captureiv.setImageBitmap(bitmap)
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    fun detecttext(){
        val galleryimg = InputImage.fromBitmap(bitmap,0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        var result : Task<Text>  = recognizer.process(galleryimg).addOnSuccessListener { visionText ->
            val result = StringBuilder()
            for(block in visionText.textBlocks){
                val blocktext = block.text
                val blockCornerpoint = block.cornerPoints
                val blockFrame = block.boundingBox
                for (line in block.lines){
                    val linetext = line.text
                    val lineCornerpoint = line.cornerPoints
                    val lineFrame = line.boundingBox
                    for (element in line.elements){
                        val elementtext = element.text
                        result.append(elementtext)
                    }
                }
            }
            detectedtv.text = result.toString()
        }.addOnFailureListener { exception ->
            Toast.makeText(this,"Fail to detect text from Image.."+exception.message,Toast.LENGTH_SHORT).show()
        }
    }
}