package com.example.kknkt.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Environment
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.kknkt.R
import com.example.kknkt.models.Person
import com.opencsv.CSVWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException


class ExportSQLiteToCSV(context: Context,persons: List<Person>,fileName: String): AsyncTask<String, String, String>() {
    private var mContext: Context = context
    private val personToExport = persons
    private var m_Text = fileName
    val TAG = "EXPORT"

    private val dialog = ProgressDialog(mContext)
    override fun onPreExecute() {
        dialog.setMessage("Exporting database...")
        dialog.show()
    }
    override fun doInBackground(vararg params: String?): String {
        val exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }

        val file = File(exportDir, "$m_Text.csv")
        try {

            val permissionCheck = ContextCompat.checkSelfPermission(
                mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                if (!file.createNewFile()) {
                    Log.d(
                        "Test",
                        "This file is already exist: " + file.absolutePath
                    )
                }
            }

            val csvWrite = CSVWriter(FileWriter(file))

            //data
            val listdata = ArrayList<String>()
            //Headers
            val arrStr1 =
                arrayOf("Nik", "Nama", "Tempat tanggal lahir", "Jenis Kelamin", "Alamat", "RT", "RW", "Kelurahan", "Kecamatan", "Agama", "Pekerjaan", "Kewarganegaraan", "Tanggal datang", "Tanggal Bebas")
            csvWrite.writeNext(arrStr1)
            for(person in personToExport){
                val arrStr = arrayOf(
                    person.nik.toString(),
                    person.name.toString(),
                    person.ttl.toString(),
                    person.gender.toString(),
                    person.address.toString(),
                    person.rt.toString(),
                    person.rw.toString(),
                    person.village.toString(),
                    person.subDistrict.toString(),
                    person.religion.toString(),
                    person.job.toString(),
                    person.citizenship.toString(),
                    person.arrivalDate.toString(),
                    person.freeDate.toString()
                )
                csvWrite.writeNext(arrStr)
            }
            csvWrite.close()
            return mContext.getString(R.string.success_export)
        } catch (e: IOException) {
            Log.e("MainActivity", e.message, e)
            return e.message!!
        }
    }

    @SuppressLint("NewApi")
    override fun onPostExecute(response: String) {
        if (this.dialog.isShowing()) {
            this.dialog.dismiss()
        }
//        if (response.isEmpty()) {
            Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(mContext, "Export failed!", Toast.LENGTH_SHORT).show()
//        }
    }
}