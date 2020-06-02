package com.example.kknkt.utils

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder

class QRGenerator {
    companion object{
        fun generate(rawText: String): Bitmap{
            var text = rawText
            val multiFormatWriter =  MultiFormatWriter();
            var bitmap: Bitmap? = null
            try {
                val bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,600,600)
                val barcodeEncoder = BarcodeEncoder()
                bitmap = barcodeEncoder.createBitmap(bitMatrix)
//                imageView.setImageBitmap(bitmap)
            } catch (e: WriterException) {
                e.printStackTrace();
            }
            return bitmap!!
        }
    }
}