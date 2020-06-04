package com.example.kknkt.ui.person

import android.Manifest
import android.content.pm.PackageManager
import com.example.kknkt.models.Person
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import com.example.kknkt.R
import com.example.kknkt.db.RTDatabase
import com.example.kknkt.models.PersonAbsen
import com.example.kknkt.repository.RTRepository
import com.example.kknkt.ui.MainActivity
import com.example.kknkt.utils.CSVExtractor
import com.google.zxing.Result
import kotlinx.android.synthetic.main.activity_q_r_scanner.*
import kotlinx.coroutines.launch
import me.dm7.barcodescanner.zxing.ZXingScannerView

private const val REQUEST_CODE_PERMISSIONS = 10
private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

class QRScannerActivity : AppCompatActivity(),ZXingScannerView.ResultHandler,PersonViewModel.SelectOnePersonCallBack {

    private val TAG = "QRSCANNERACTIVITY"
    private lateinit var viewModel: PersonViewModel
    private val args: QRScannerActivityArgs by navArgs()
    private var person = Person()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_q_r_scanner)

        if (allPermissionsGranted()) {
            mScannerView.stopCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                com.example.kknkt.ui.person.REQUIRED_PERMISSIONS,
                com.example.kknkt.ui.person.REQUEST_CODE_PERMISSIONS
            )
        }

        btn_scanner_backward.setOnClickListener {
            finish()
        }
        btn_scanner_flashlight.setOnClickListener {
            mScannerView.flash = !mScannerView.flash
        }

        val personsRepository = RTRepository(RTDatabase(this))
        val viewModelProviderFactory = PersonsViewModelProviderFactory(personsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(PersonViewModel::class.java)

    }



    override fun onResume() {
        super.onResume()
        mScannerView.setResultHandler(this) // Register ourselves as a handler for scan results.
        mScannerView.startCamera() // Start camera on resume
    }
    override fun onPause() {
        super.onPause()
        mScannerView.stopCamera() // Stop camera on pause
    }
    override fun handleResult(rawResult: Result) {
        var list = CSVExtractor.extract(rawResult.text)
        person.apply {
            uniqueCode = list[0]
            name = list[1]
            address = list[2]
        }
        viewModel.getPersonByUniqueCode(person.uniqueCode.toString(),this)
        mScannerView.stopCamera()
        mScannerView.resumeCameraPreview(this)
    }

    override fun onSucces(listPerson: List<Person>) {
       if(listPerson.isNotEmpty()){
           val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
           builder.setTitle(getString(R.string.temperature))
           val input = EditText(this)
           var m_Text = ""
           input.inputType = InputType.TYPE_CLASS_NUMBER
           builder.setView(input)
           builder.setPositiveButton(getString(R.string.ok)
           ) { _, _ ->
               m_Text = input.text.toString()

               var personAbsen = PersonAbsen()
               val person = listPerson.get(0)
               val absen = args.absen
               personAbsen.apply {
                   personId = person.id
                   absenId = absen?.id
                   temperature = m_Text.toInt()
               }

               viewModel.addPersonAbsen(personAbsen)
               mScannerView.startCamera()
           }
           builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->  dialog.cancel()
           }
           builder.show()
       }else{
           val selectOnePersonCallBack = this
           lifecycleScope.launch {
               viewModel.savePerson(person)
               viewModel.getPersonByUniqueCode(person.uniqueCode.toString(),selectOnePersonCallBack)
           }
       }
    }

    private fun personAbsen(personAbsen: PersonAbsen){

    }

    private fun allPermissionsGranted() = com.example.kknkt.ui.person.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == com.example.kknkt.ui.person.REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                mScannerView.post { mScannerView.startCamera() }
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
