package com.example.kknkt.ui.person

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.text.InputType
import android.text.style.TtsSpan.TYPE_TEXT
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kknkt.ui.MainActivity
import com.example.kknkt.R
import com.example.kknkt.adapter.PersonsAdapter
import com.example.kknkt.camera.CameraActivity
import com.example.kknkt.ui.dashboard.BottomNavigationDrawerFragment
import com.example.kknkt.utils.ExportSQLiteToCSV
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_person.*
import java.io.File

class PersonFragment : Fragment(R.layout.fragment_person) {

    lateinit var viewModel: PersonViewModel
    private lateinit var personsAdapter: PersonsAdapter

    val TAG = "PersonsFragment"

    companion object{
        public val REQUEST_GET_TEXT_RESULT = 1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG,"person")
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()
        (activity as MainActivity).fab.setOnClickListener {
            goToAddUpdateFragment()
        }


        personsAdapter.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("person", it)
                putString("Update","update")
            }

            (activity as MainActivity).fab.hide(
                (activity as MainActivity).addVisibilityChanged)
            (activity as MainActivity).invalidateOptionsMenu()
            (activity as MainActivity).tvTitleFragment.setText(getString(R.string.person))
            (activity as MainActivity).btnBack.visibility = View.VISIBLE
            findNavController().navigate(
                R.id.action_personFragment_to_personAddUpdateFragment,
                bundle
            )
        }

        viewModel.getAllPerson().observe(viewLifecycleOwner, Observer { persons ->
            personsAdapter.differ.submitList(persons)
            (activity as MainActivity).personToExport = persons
        })

    }

    private fun setupRecyclerView() {
        Log.d(TAG,"tersetting")
        personsAdapter = PersonsAdapter()
        rvPerson.apply {
            adapter = personsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        setRetainInstance(true)
    }

    private fun goToAddUpdateFragment(){
        (activity as MainActivity).btnBack.visibility = View.VISIBLE
        (activity as MainActivity).fab.hide((activity as MainActivity).addVisibilityChanged)
        (activity as MainActivity).invalidateOptionsMenu()
        (activity as MainActivity).tvTitleFragment.setText(getString(R.string.person))
        findNavController().navigate(R.id.action_personFragment_to_personAddUpdateFragment)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_free -> {
                Log.d(TAG,"free")
                (activity as MainActivity).tvTitleFragment.setText(getString(R.string.free_today))
                (activity as MainActivity).fab.hide()
                findNavController().navigate(R.id.action_personFragment_to_freePersonFragment)
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
            R.id.navigation_absen -> {

                findNavController().navigate(R.id.action_personFragment_to_absenFragment)
            }
            android.R.id.home -> {
                val bottomNavDrawerFragment = BottomNavigationDrawerFragment()
                bottomNavDrawerFragment.show((activity as MainActivity).supportFragmentManager, bottomNavDrawerFragment.tag)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
