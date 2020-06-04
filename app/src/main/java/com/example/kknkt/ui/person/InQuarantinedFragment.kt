package com.example.kknkt.ui.person

import com.example.kknkt.models.Person
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.kknkt.R
import com.example.kknkt.adapter.PersonsAdapter
import com.example.kknkt.ui.MainActivity
import com.example.kknkt.utils.DateUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_in_quarantined.*
import kotlinx.android.synthetic.main.fragment_person.*

/**
 * A simple [Fragment] subclass.
 */
class InQuarantinedFragment : Fragment() {

    lateinit var personViewModel: PersonViewModel
    lateinit var personsAdapter: PersonsAdapter

    private val TAG = "INQURANTINEFRAGMENT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        setRetainInstance(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_in_quarantined, container, false)
    }

    fun prepareMyFragment(){
        (activity as MainActivity).apply {
            btnBack.visibility = View.VISIBLE
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            tvTitleFragment.setText(getString(R.string.quarantined))
        }
        personViewModel = (activity as MainActivity).viewModel

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareMyFragment()
        setupRecyclerView()

        personViewModel.getAllPerson().observe(viewLifecycleOwner, Observer { persons ->
            val personsInQuarantine = ArrayList<Person>()
            for(person in persons){
                if (person.freeDate != null){
                    if (DateUtils.dateCompare(person.freeDate!!,DateUtils.getTodayDate()) > 0){
                        personsInQuarantine.add(person)
                    }
                }
            }
            personsAdapter.differ.submitList(personsInQuarantine)
            (activity as MainActivity).personToExport = persons
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.navigation_home -> {
                findNavController().navigate(R.id.action_inQuarantinedFragment_to_personFragment)
            }
            R.id.navigation_absen -> {
                findNavController().navigate(R.id.action_inQuarantinedFragment_to_absenFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        Log.d(TAG,"tersetting")
        personsAdapter = PersonsAdapter()
        rv_inQuarantined.apply {
            adapter = personsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

 }
