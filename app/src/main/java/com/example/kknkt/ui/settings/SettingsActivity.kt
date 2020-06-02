package com.example.kknkt.ui.settings

import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.kknkt.R
import com.example.kknkt.db.RTDatabase
import com.example.kknkt.models.Alarm
import com.example.kknkt.receiver.AlarmReceiver
import com.example.kknkt.repository.RTRepository
import kotlinx.android.synthetic.main.activity_settings.*
import java.text.SimpleDateFormat
import java.util.*

class SettingsActivity : AppCompatActivity() {

    private val TAG: String = "SettingsActivity"
    private lateinit var viewModel: AlarmViewModel
    private var alarm: Alarm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        prepareDatePicker()

        val rTRepository = RTRepository(RTDatabase(this))
        val viewModelProviderFactory = AlarmViewModelFactory(rTRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(AlarmViewModel::class.java)
        viewModel.getAlarm().observe(this, androidx.lifecycle.Observer {alarms: List<Alarm>? ->
            if(!alarms?.isEmpty()!!){
                this.alarm = alarms?.get(0)
                Log.d(TAG,alarm?.time.toString())
                edtNumQuarantineDays.setText(alarm!!.day.toString())
                edtReminderTime.setText(alarm!!.time.toString())
            }
        })
        btnBack.setOnClickListener {
            finish()
        }
        btnSaveSetting.setOnClickListener {
            this.alarm?.apply {
                time = edtReminderTime.text.toString().trim()
                day = Integer.parseInt(edtNumQuarantineDays.text.toString())
                isOn = true
            }
            Settings?.apply {
                numberOfQuarantineDays = alarm?.day!!
                remiderTime = alarm?.time!!
                isAlarmOn = true
            }
            AlarmReceiver().cancelAlarm(this,AlarmReceiver.TYPE_REPEATING)
            AlarmReceiver().setRepeatingAlarm(this,AlarmReceiver.TYPE_REPEATING,alarm?.time!!,"setting repeating Alarm")
            Log.d(TAG,alarm?.time.toString())
            viewModel.saveAlarmTime(alarm!!)
        }
    }
    private fun prepareDatePicker(){
        val cal = Calendar.getInstance()
        edtReminderTime.setOnClickListener {
            Log.d(TAG,"terclick")
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                edtReminderTime.setText(SimpleDateFormat("HH:mm").format(cal.time))
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
            if (alarm==null)
                alarm = Alarm()

        }
    }
}
