package com.example.kknkt.ui.dashboard

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.example.kknkt.R
import com.example.kknkt.ui.MainActivity
import com.example.kknkt.ui.person.PersonFragment
import com.example.kknkt.utils.ExportSQLiteToCSV
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_bottom_navigation_drawer.*

class BottomNavigationDrawerFragment: BottomSheetDialogFragment() {

    private val TAG: String = "BottomNavigation"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bottom_navigation_drawer, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        navigation_view.setNavigationItemSelectedListener { menuItem ->
            // Bottom Navigation Drawer menu item clicks
            when (menuItem.itemId) {
                 R.id.navigation_setting -> {
                     var currentDes = findNavController().currentDestination
                     Log.d("BottomNavigation",currentDes.toString())
                     when(currentDes!!.id){
                         R.id.personFragment -> findNavController().navigate(R.id.action_personFragment_to_settingsActivity)
                         R.id.freePersonFragment -> findNavController().navigate(R.id.action_freePersonFragment_to_settingsActivity)
                         R.id.personAddUpdateFragment -> findNavController().navigate(R.id.action_personAddUpdateFragment_to_settingsActivity)
                     }
                 }
                R.id.navigation_about->{
                    var currentDes = findNavController().currentDestination
                    Log.d("BottomNavigation",currentDes.toString())
                    when(currentDes!!.id){
                        R.id.personFragment -> findNavController().navigate(R.id.action_personFragment_to_aboutActivity)
                        R.id.freePersonFragment -> findNavController().navigate(R.id.action_freePersonFragment_to_aboutActivity)
                        R.id.personAddUpdateFragment -> findNavController().navigate(R.id.action_personAddUpdateFragment_to_aboutActivity)
                    }
                }
                R.id.navigation_export -> {
                    val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(requireContext())
                    builder.setTitle(getString(R.string.file_name))
                    val input = EditText(requireContext())
//                input.width = 100
                    var m_Text = ""
                    input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                    builder.setView(input)
                    builder.setPositiveButton(getString(R.string.ok)
                    ) { _, _ ->
                        m_Text = input.text.toString()
                        ExportSQLiteToCSV(requireContext(),(activity as MainActivity).personToExport,m_Text).execute()
                    }
                    builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->  dialog.cancel()
                    }
                    builder.show()

                }
            }
            true
        }
    }
}