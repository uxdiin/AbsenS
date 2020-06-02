package com.example.kknkt.ui.person

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kknkt.ui.MainActivity
import com.example.kknkt.R
import com.example.kknkt.adapter.PersonsAdapter
import com.example.kknkt.ui.dashboard.BottomNavigationDrawerFragment
import com.example.kknkt.utils.DateUtils
import com.example.kknkt.utils.ExportSQLiteToCSV
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.person_free_fragment.*

class FreePersonFragment : Fragment(R.layout.person_free_fragment) {

    private val TAG: String? = "PersonFreeFragment"
    private lateinit var viewModel: PersonViewModel
    private lateinit var personsAdapter: PersonsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG,"person")
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        personsAdapter.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("person", it)
                putString("Update","update")
            }

            (activity as MainActivity).fab.hide(
                (activity as MainActivity).addVisibilityChanged)
            (activity as MainActivity).invalidateOptionsMenu()
            findNavController().navigate(
                R.id.action_freePersonFragment_to_showPersonActivity2,
                bundle
            )
        }

        viewModel.getFreePerson(DateUtils.getTodayDate()).observe(viewLifecycleOwner, Observer { persons ->
            personsAdapter.differ.submitList(persons)
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun setupRecyclerView() {
        Log.d(TAG,"tersetting")
        personsAdapter = PersonsAdapter()
        rvFreePerson.apply {
            adapter = personsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when {
            item.itemId == R.id.navigation_home -> {
                Log.d(TAG,"home")
                (activity as MainActivity).tvTitleFragment.setText(requireContext().getString(R.string.quarantined))
                (activity as MainActivity).fab.show()
                findNavController().navigate(R.id.action_freePersonFragment_to_personFragment)
            }
            item.itemId == android.R.id.home -> {
                val bottomNavDrawerFragment = BottomNavigationDrawerFragment()
                bottomNavDrawerFragment.show((activity as MainActivity).supportFragmentManager, bottomNavDrawerFragment.tag)
            }
            item.itemId == R.id.navigation_export -> {
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
        return super.onOptionsItemSelected(item)
    }
}
