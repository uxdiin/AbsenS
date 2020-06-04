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
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_person.*
import java.io.File

class PersonFragment : Fragment(R.layout.fragment_person) {

    lateinit var viewModel: PersonViewModel
    private lateinit var personsAdapter: PersonsAdapter

    val TAG = "PersonsFragment"

    companion object{
        public val REQUEST_GET_TEXT_RESULT = 1
    }

    fun prepareMyFragment(){
        (activity as MainActivity).apply {
            bottom_app_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
            tvTitleFragment.setText(getString(R.string.personL_list))
            bottom_app_bar.setNavigationIcon(R.drawable.ic_menu_black_24dp)
            fab.show()
            fab.hide(addVisibilityChanged)
            fab.setImageResource(R.drawable.ic_add_black_24dp)
            fab.setOnClickListener {
                goToAddUpdateFragment()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareMyFragment()
        Log.d(TAG,"person")
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()


        personsAdapter.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("person", it)
                putString("Update","update")
            }

            (activity as MainActivity).apply {
                bottom_app_bar.navigationIcon = null
                fab.hide((activity as MainActivity).addVisibilityChanged)
                invalidateOptionsMenu()
                tvTitleFragment.setText(getString(R.string.person))
                btnBack.visibility = View.VISIBLE
            }
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
        (activity as MainActivity).apply {
            bottom_app_bar.navigationIcon = null
            btnBack.visibility = View.VISIBLE
            invalidateOptionsMenu()
            tvTitleFragment.setText(getString(R.string.person))
        }
        findNavController().navigate(R.id.action_personFragment_to_personAddUpdateFragment)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_quarantine -> {
                Log.d(TAG,"free")
                (activity as MainActivity).apply {
                    tvTitleFragment.setText(getString(R.string.quarantined))
                    fab.hide()
                    bottom_app_bar.navigationIcon = null
                }
                findNavController().navigate(R.id.action_personFragment_to_quarantineFragment)
            }
            R.id.navigation_absen -> {
                (activity as MainActivity).bottom_app_bar.navigationIcon = null
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
