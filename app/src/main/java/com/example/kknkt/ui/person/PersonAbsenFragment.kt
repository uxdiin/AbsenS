package com.example.kknkt.ui.person

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.kknkt.R
import com.example.kknkt.adapter.AbsenningAdapter
import com.example.kknkt.adapter.PersonAbsenDataAdapter
import com.example.kknkt.models.Absen
import com.example.kknkt.models.PersonAbsen
import com.example.kknkt.ui.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_absenning.*
import kotlinx.android.synthetic.main.fragment_person_absen.*

class PersonAbsenFragment : Fragment() {

    private val TAG = "PERSONABSENFRAGMENT"
    private lateinit var personAbsenDataAdapter: PersonAbsenDataAdapter 
    private val args: PersonAbsenFragmentArgs by navArgs()
    private lateinit var absen: Absen

    fun prepareMyFragment(){
        (activity as MainActivity).apply {
            fab.hide()
            btnBack.setOnClickListener {
                findNavController().navigate(R.id.action_personAbsenFragment_to_absenFragment)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepareMyFragment()
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_person_absen, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        absen = args.absen!!

        setupRecyclerView()

        (activity as MainActivity).viewModel.getPersonAbsenData(absen.id!!).observe(viewLifecycleOwner, Observer { personAbsenDatas->
            personAbsenDataAdapter.differ.submitList(personAbsenDatas)
        })
        personAbsenDataAdapter.setOnClickListener {

        }

    }

    private fun setupRecyclerView() {
        Log.d(TAG,"tersetting")
        personAbsenDataAdapter = PersonAbsenDataAdapter()
        rvPersonAbsenData.apply {
            adapter = personAbsenDataAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.bottom_nav_menu_absenning,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.navigation_absenning -> findNavController().popBackStack()
        }
        return super.onOptionsItemSelected(item)
    }
}
