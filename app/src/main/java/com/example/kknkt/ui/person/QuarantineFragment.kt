package com.example.kknkt.ui.person

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.example.kknkt.R
import com.example.kknkt.ui.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_quarantine.*

class QuarantineFragment : Fragment() {

    fun prepareMyFragment(){
        (activity as MainActivity).apply {
            btnBack.visibility = View.VISIBLE
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            tvTitleFragment.setText(getString(R.string.free_today))
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
        return inflater.inflate(R.layout.fragment_quarantine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareMyFragment()
        ll_inQurantine.setOnClickListener {
            findNavController().navigate(R.id.action_quarantineFragment_to_inQuarantinedFragment)
        }
        ll_free_today.setOnClickListener {
            findNavController().navigate(R.id.action_quarantineFragment_to_freePersonFragment)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.navigation_home -> {
                findNavController().navigate(R.id.action_quarantineFragment_to_personFragment)
            }
            R.id.navigation_absen -> {
                findNavController().navigate(R.id.action_quarantineFragment_to_absenFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
