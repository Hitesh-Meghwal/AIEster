package com.example.aiester

import android.Manifest.permission.CAMERA
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.Text.*
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

@Suppress("DEPRECATION")
class Capture : AppCompatActivity() {

    lateinit var snapbtn :Button
    lateinit var detecttxtbtn :Button
    lateinit var captureiv : ImageView
    lateinit var detectedtv : TextView
    lateinit var imagebitmap : Bitmap
    lateinit var clipboardManager: ClipboardManager
    var REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture)

        snapbtn = findViewById(R.id.Snap)
        detecttxtbtn = findViewById(R.id.detecttext)
        captureiv = findViewById(R.id.captureiv)
        detectedtv = findViewById(R.id.resultedtext)


        snapbtn.setOnClickListener {
            if(checkpermission()){
                captureimg()
            }
            else{
                requestpermission()
            }
        }

        detecttxtbtn.setOnClickListener {
            detecttext()
        }
        clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        detectedtv.setOnClickListener {
            clipboardManager.setPrimaryClip(ClipData.newPlainText("Text",detectedtv.text.toString()))
            Toast.makeText(this, "Text Copied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkpermission() : Boolean{
        val camerpermission = ContextCompat.checkSelfPermission(applicationContext,CAMERA)
        return camerpermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestpermission(){
        val PERMISSION_CODE = 200
        ActivityCompat.requestPermissions(this, arrayOf(CAMERA),PERMISSION_CODE)
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun captureimg(){
        val takepicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(takepicture.resolveActivity(packageManager)!=null){
            startActivityForResult(takepicture,REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults.size>0) run {
            val cameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED
            if (cameraPermission){
                Toast.makeText(this,"Permission Granted", Toast.LENGTH_SHORT).show()
                captureimg()
            }
            else{
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show()
            }
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK ){
            val extras: Bundle? = data?.extras
            imagebitmap =extras?.get("data") as Bitmap
            captureiv.setImageBitmap(imagebitmap)
        }
    }

    fun detecttext(){
        val InputImage = InputImage.fromBitmap(imagebitmap,0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        var result : Task<Text>  = recognizer.process(InputImage).addOnSuccessListener { visionText ->
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