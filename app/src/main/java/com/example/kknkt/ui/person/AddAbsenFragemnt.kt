package com.example.kknkt.ui.person

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

import com.example.kknkt.R
import com.example.kknkt.models.Absen
import com.example.kknkt.ui.MainActivity
import com.example.kknkt.ui.settings.Settings
import com.example.kknkt.utils.DateUtils
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_add_absen_fragemnt.*
import kotlinx.android.synthetic.main.fragment_add_absen_fragemnt.btnSave
import kotlinx.android.synthetic.main.fragment_person_add_update.*
import kotlinx.android.synthetic.main.item_row_absen.*
import java.util.*

class AddAbsenFragemnt : Fragment() {

    fun prepareMyFragment(){
        (activity as MainActivity).apply {
            tvTitleFragment.setText(getString(R.string.event))
            bottom_app_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
            fab.setImageResource(R.drawable.ic_delete_forever_black_24dp)
            btnBack.setOnClickListener {
                findNavController().navigate(R.id.action_addAbsenFragemnt_to_absenFragment)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_absen_fragemnt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareMyFragment()
        btnSave.setOnClickListener {
            val absen = Absen()
            absen.eventName = edtEventName?.text.toString().trim()
            absen.date = edtEventDate.text.toString().trim()
            (activity as MainActivity).viewModel.addAbsen(absen)
            findNavController().navigate(R.id.action_addAbsenFragemnt_to_absenFragment)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }
}
