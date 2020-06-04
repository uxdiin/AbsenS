package com.example.kknkt.ui.person

import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.kknkt.R
import com.example.kknkt.adapter.AbsenAdapter
import com.example.kknkt.models.Absen
import com.example.kknkt.ui.MainActivity
import com.example.kknkt.ui.dashboard.BottomNavigationDrawerFragment
import com.example.kknkt.utils.ExportSQLiteToCSV
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_absen.*

class AbsenFragment : Fragment() {

    private val TAG = "ABSENTFRAGMENT"
    private lateinit var viewModel: PersonViewModel
    private lateinit var absenAdapter: AbsenAdapter


    fun myFragmentPreparation(){
        (activity as MainActivity).apply {
            tvTitleFragment.setText(getString(R.string.event_list))
            bottom_app_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
            fab.show()
            fab.hide(addVisibilityChanged)
            fab.setImageResource(R.drawable.ic_add_black_24dp)
            btnBack.visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_absen, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myFragmentPreparation()
        Log.d(TAG,"person")
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()
        (activity as MainActivity).fab.setOnClickListener {
            goToAddAbsenFragment()
        }


        absenAdapter.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("absen", it)
                putString("Update","update")
            }
            (activity as MainActivity).apply {
                bottom_app_bar.navigationIcon = null
                invalidateOptionsMenu()
                tvTitleFragment.setText(getString(R.string.absen))
                btnBack.visibility = View.VISIBLE
            }
            findNavController().navigate(
                R.id.action_absenFragment_to_absenningFragment,
                bundle
            )
        }

        viewModel.getAllAbsen().observe(viewLifecycleOwner, Observer { absens ->
            absenAdapter.differ.submitList(absens)
//            (activity as MainActivity).personToExport = persons
        })

    }

    private fun setupRecyclerView() {
        Log.d(TAG,"tersetting")
        absenAdapter = AbsenAdapter()
        rv_absent.apply {
            adapter = absenAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        setRetainInstance(true)
    }

    override fun onResume() {
        super.onResume()
        myFragmentPreparation()
    }
    private fun goToAddAbsenFragment(){

        (activity as MainActivity).apply {
            bottom_app_bar.navigationIcon = null
            btnBack.visibility = View.VISIBLE
            fab.hide((activity as MainActivity).addVisibilityChanged)
            invalidateOptionsMenu()
            tvTitleFragment.setText(getString(R.string.event))
        }
        findNavController().navigate(R.id.action_absenFragment_to_addAbsenFragemnt)
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        menu.clear()
//        inflater.inflate(R.menu.bottom_nav_menu,menu)
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_quarantine -> {
                (activity as MainActivity).apply {
                    bottom_app_bar.navigationIcon = null
                    tvTitleFragment.setText(getString(R.string.quarantined))
                    fab.hide()
                }
                findNavController().navigate(R.id.action_absenFragment_to_quarantineFragment)
            }
//            R.id.navigation_export -> {
//                val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(requireContext())
//                builder.setTitle(getString(R.string.file_name))
//                val input = EditText(requireContext())
////                input.width = 100
//                var m_Text = ""
//                input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
//                builder.setView(input)
//                builder.setPositiveButton(getString(R.string.ok)
//                ) { _, _ ->
//                    m_Text = input.text.toString()
//                    ExportSQLiteToCSV(requireContext(),(activity as MainActivity).personToExport,m_Text).execute()
//                }
//                builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->  dialog.cancel()
//                }
//                builder.show()
//
//            }
            R.id.navigation_home -> {
                findNavController().navigate(R.id.action_absenFragment_to_personFragment)
            }
            android.R.id.home -> {
                val bottomNavDrawerFragment = BottomNavigationDrawerFragment()
                bottomNavDrawerFragment.show((activity as MainActivity).supportFragmentManager, bottomNavDrawerFragment.tag)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
