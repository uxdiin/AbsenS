package com.example.kknkt.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.kknkt.R
import com.example.kknkt.ui.person.PersonFragment
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
            }
            true
        }
    }
}