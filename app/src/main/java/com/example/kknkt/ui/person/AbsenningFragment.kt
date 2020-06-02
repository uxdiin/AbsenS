package com.example.kknkt.ui.person

import com.example.kknkt.models.Person
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
import com.example.kknkt.adapter.AbsenAdapter
import com.example.kknkt.adapter.AbsenningAdapter
import com.example.kknkt.models.Absen
import com.example.kknkt.models.PersonAbsen
import com.example.kknkt.ui.MainActivity
import com.example.kknkt.utils.ExportSQLiteToCSV
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_absen.*
import kotlinx.android.synthetic.main.fragment_absenning.*
import org.apache.commons.beanutils.converters.IntegerConverter

class AbsenningFragment : Fragment() {

    private  val TAG = "ABSENNINGFRAGMENT"
    private lateinit var absenningAdapter: AbsenningAdapter
    private val args: AbsenningFragmentArgs by navArgs()

    private var absen: Absen? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        setRetainInstance(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_absenning, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).fab.setImageResource(R.drawable.ic_action_scan)

        absen = args.absen

        setupRecyclerView()

        absenningAdapter.setOnClickListener {

            val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.temperature))
            val input = EditText(requireContext())
            var m_Text = ""
            input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            builder.setView(input)
            builder.setPositiveButton(getString(R.string.ok)
            ) { _, _ ->
                m_Text = input.text.toString()

                val personAbsen = PersonAbsen()

                personAbsen.apply {
                    personId = it.id
                    absenId = absen?.id
                    temperature = m_Text.toInt()
                }

                (activity as MainActivity).viewModel.addPersonAbsen(personAbsen)
            }
            builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->  dialog.cancel()
            }
            builder.show()

        }

        btnSearch.setOnClickListener {
            edtFindByName?.let {
                (activity as MainActivity).viewModel.searchPerson(it.text.toString().trim()).observe(viewLifecycleOwner, Observer { persons->
                    absenningAdapter.differ.submitList(persons)
                })
            }
        }
        (activity as MainActivity).fab.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("absen",absen)
            }
            findNavController().navigate(R.id.action_absenningFragment_to_QRScannerActivity,bundle)
        }
    }

    private fun setupRecyclerView() {
        Log.d(TAG,"tersetting")
        absenningAdapter = AbsenningAdapter()
        rv_absenning.apply {
            adapter = absenningAdapter
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
            R.id.navigation_present -> {
                val bundle = Bundle().apply {
                    putSerializable("absen", absen)
                }
                findNavController().navigate(R.id.action_absenningFragment_to_personAbsenFragment,bundle)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
