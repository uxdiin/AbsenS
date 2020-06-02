package com.example.kknkt.ui.person

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.kknkt.R
import com.example.kknkt.models.Absen
import com.example.kknkt.ui.MainActivity
import com.example.kknkt.ui.settings.Settings
import com.example.kknkt.utils.DateUtils
import kotlinx.android.synthetic.main.fragment_add_absen_fragemnt.*
import kotlinx.android.synthetic.main.fragment_add_absen_fragemnt.btnSave
import kotlinx.android.synthetic.main.fragment_person_add_update.*
import kotlinx.android.synthetic.main.item_row_absen.*
import java.util.*

class AddAbsenFragemnt : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_absen_fragemnt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSave.setOnClickListener {
            val absen = Absen()
            absen.eventName = edtEventName?.text.toString().trim()
            absen.date = edtEventDate.text.toString().trim()
            (activity as MainActivity).viewModel.addAbsen(absen)
        }
        prepareDatePicker()
    }
    private fun prepareDatePicker(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        edtEventDate.setOnClickListener {
            var dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                month = monthOfYear + 1
                edtEventDate.setText(""+year + "-" + month +"-" + dayOfMonth)
//                var date = Date(year, month, dayOfMonth)
//                var freeDate = DateUtils.addDate(date, Settings.numberOfQuarantineDays)
            }, year, month, day)
            dpd.show()

        }
    }
}
