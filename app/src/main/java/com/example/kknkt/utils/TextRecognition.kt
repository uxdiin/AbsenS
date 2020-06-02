package com.example.kknkt.utils

import android.content.Context
import android.graphics.Insets.add
import android.media.Image
import android.util.Log
import androidx.camera.core.ImageProxy
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions
import com.google.firebase.ml.vision.text.FirebaseVisionText

import com.example.kknkt.models.Person
import com.google.firebase.ml.vision.document.FirebaseVisionCloudDocumentRecognizerOptions
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import kotlinx.android.synthetic.main.fragment_person_add_update.*

class TextRecognition(context: Context,textRecognitionCallBack: TextRecognitionCallBack) {
    lateinit var context: Context
    lateinit var textRecognitionCallBack: TextRecognitionCallBack
    init {
        this.context = context
        this.textRecognitionCallBack = textRecognitionCallBack
    }

    val TAG: String = "ReadableImageAnalyzer"
    var resultText = "ksong"
    private fun degreesToFirebaseRotation(degrees: Int): Int = when(degrees) {
        0 -> FirebaseVisionImageMetadata.ROTATION_0
        90 -> FirebaseVisionImageMetadata.ROTATION_90
        180 -> FirebaseVisionImageMetadata.ROTATION_180
        270 -> FirebaseVisionImageMetadata.ROTATION_270
        else -> throw Exception("Rotation must be 0, 90, 180, or 270.")
    }

    fun readLocal(image: FirebaseVisionImage): String {
            val detector = FirebaseVision.getInstance()
                .onDeviceTextRecognizer
            val result = detector.processImage(image)
                .addOnSuccessListener { firebaseVisionText ->

                    setToEditPerson(firebaseVisionText!!)
                }
                .addOnFailureListener { e ->
                }

//        }
        return this.resultText
    }
    public fun readOnline(image: FirebaseVisionImage){
        val options = FirebaseVisionCloudTextRecognizerOptions.Builder()
            .setLanguageHints(listOf("id", "en"))
            .build()
        val detector = FirebaseVision.getInstance().getCloudTextRecognizer(options)
        val result = detector.processImage(image)
            .addOnSuccessListener { firebaseVisionText ->

                setToEditPerson(firebaseVisionText)
            }
            .addOnFailureListener { e ->
            }
    }

    private fun setToEditPerson(firebaseVisionText : FirebaseVisionText){
        var person = Person()
        var lines = firebaseVisionText.text.split("\n")
        val res = mutableListOf<String>()
        for ((i, line) in lines.withIndex()) {
            var lineReplace = line.replace(Regex("""(?i)gol\. darah|nik|Ternpat/Tgtahir|kewarganegaraan|nama|status perkawinan|berlaku hingga|alamat|agama|tempat/tgl lahir|jenis kelamin|gol darah|rt/rw|kel|desa|kecamatan|ATRW|Pekerjaan"""), "")
            line.replace(":","")

            if (line != "") {
                res.add(lineReplace)
                Log.d("TAG RES", res[i]+i )
            }
        }
        try {
            person?.nik = res[1]
        }catch (e: java.lang.Exception) {

        }
        try {
            person?.name = res[2]
        }catch (e : java.lang.Exception){

        }
        try {
            person?.ttl = res[3]
        }catch (e : java.lang.Exception){

        }
        try {
            person?.address = res[5]
        }catch (e : java.lang.Exception){

        }
        try {
            person?.rt = res[6].substring(0,2)
        }catch (e : java.lang.Exception){

        }
        try {
            person?.rw = res[6].substring(4,6)
        }catch (e : java.lang.Exception){

        }
        try {
            person?.village = res[7]
        }catch (e : java.lang.Exception){
//            person?.subDistrict = res[8]
        }
        try {
            person?.subDistrict = res[8]
        }catch (e : java.lang.Exception){

        }
        try {
            person?.religion = res[9]
        }catch (e : java.lang.Exception){

        }
        try {
            person?.religion = res[9]
        }catch (e : java.lang.Exception){

        }
        try {
            person?.religion = res[9]
        }catch (e : java.lang.Exception){

        }
        try {
            person?.job = res[14]
        }catch (e : java.lang.Exception){

        }
//                    9->person?.gender = blockText
        person?.citizenship = "WNI"

            this.textRecognitionCallBack.onSuccessRead(person)
    }
    public fun readDocument(image: FirebaseVisionImage){
        val options = FirebaseVisionCloudDocumentRecognizerOptions.Builder()
            .setLanguageHints(listOf("en", "id"))
            .build()
        val detector = FirebaseVision.getInstance().getCloudDocumentTextRecognizer(options)
        detector.processImage(image)
            .addOnSuccessListener { firebaseVisionDocumentText ->
                setToPerson(firebaseVisionDocumentText)
            }
            .addOnFailureListener { e ->
            }

    }

    public fun setToPerson(result: FirebaseVisionDocumentText){
        Log.d(TAG,result.text)
        for (block in result.blocks) {
            val blockText = block.text
            Log.d(TAG,block.text)
            val blockConfidence = block.confidence
            val blockRecognizedLanguages = block.recognizedLanguages
            val blockFrame = block.boundingBox
            for (paragraph in block.paragraphs) {
                val paragraphText = paragraph.text
                val paragraphConfidence = paragraph.confidence
                val paragraphRecognizedLanguages = paragraph.recognizedLanguages
                val paragraphFrame = paragraph.boundingBox
                for (word in paragraph.words) {
                    val wordText = word.text
                    val wordConfidence = word.confidence
                    val wordRecognizedLanguages = word.recognizedLanguages
                    val wordFrame = word.boundingBox
                    for (symbol in word.symbols) {
                        val symbolText = symbol.text
                        val symbolConfidence = symbol.confidence
                        val symbolRecognizedLanguages = symbol.recognizedLanguages
                        val symbolFrame = symbol.boundingBox
                    }
                }
            }
        }
    }
    interface TextRecognitionCallBack{
        fun onSuccessRead(person: Person)
    }
}