package com.example.kknkt.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.kknkt.R
import com.example.kknkt.db.RTDatabase
import com.example.kknkt.models.Alarm
import com.example.kknkt.receiver.AlarmReceiver
import com.example.kknkt.repository.RTRepository
import com.example.kknkt.ui.person.PersonViewModel
import com.example.kknkt.ui.person.PersonsViewModelProviderFactory
import com.example.kknkt.ui.settings.AlarmViewModel
import com.example.kknkt.ui.settings.AlarmViewModelFactory
import com.example.kknkt.ui.settings.Settings
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.opencsv.CSVWriter
import kotlinx.android.synthetic.main.activity_main.*
import com.example.kknkt.models.Person

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: PersonViewModel
    lateinit var alarmviewModel: AlarmViewModel

    lateinit var personToExport: List<Person>

    private var currentFabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
    private val TAG: String = "MainActivity"
    var addVisibilityChanged: FloatingActionButton.OnVisibilityChangedListener

    init {

        addVisibilityChanged = object : FloatingActionButton.OnVisibilityChangedListener() {
            override fun onShown(fab: FloatingActionButton?) {
                super.onShown(fab)
            }
            override fun onHidden(fab: FloatingActionButton?) {
                super.onHidden(fab)
                fab?.show()
            }
        }
    }
    companion object{
        public var idFrom: Int?= null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(
            this@MainActivity, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            100
        )


        val rTRepository = RTRepository(RTDatabase(this))
        val alarmViewModelProviderFactory = AlarmViewModelFactory(rTRepository)
        alarmviewModel = ViewModelProvider(this, alarmViewModelProviderFactory).get(AlarmViewModel::class.java)

        alarmviewModel.getAlarm().observe(this, Observer { alarms ->
            if(!alarms?.isEmpty()!!){
                val alarm = alarms?.get(0)
                Settings?.apply {
                    numberOfQuarantineDays = alarm?.day!!
                    remiderTime = alarm?.time!!
                    isAlarmOn = alarm?.isOn!!
                }
            }else{
                val alarm = Alarm()
                alarm.day = Settings.numberOfQuarantineDays
                alarm.time = Settings.remiderTime
                alarm.isOn = true
                alarmviewModel.saveAlarmTime(alarm)
            }
            if(Settings?.isAlarmOn == false)
                AlarmReceiver().setRepeatingAlarm(this,AlarmReceiver.TYPE_REPEATING,"09:00","setting repeating alarm")
        })

        val personsRepository = RTRepository(RTDatabase(this))
        val viewModelProviderFactory = PersonsViewModelProviderFactory(personsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(PersonViewModel::class.java)

        setSupportActionBar(bottom_app_bar)


    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.bottom_nav_menu, menu)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        tvTitleFragment.setText(getString(R.string.quarantined))
        btnBack.visibility = View.GONE
//        ExportSQLiteToCSV(this).execute()
        fab.hide(addVisibilityChanged)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> if (grantResults.isNotEmpty() || grantResults[0] === PackageManager.PERMISSION_GRANTED || grantResults[1] === PackageManager.PERMISSION_GRANTED
            ) {
                /* User checks permission. */
            } else {
                Toast.makeText(this@MainActivity, "Permission is denied.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
