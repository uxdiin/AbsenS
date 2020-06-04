package com.example.kknkt.ui.person

import com.example.kknkt.models.Person
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.kknkt.R
import com.example.kknkt.adapter.AbsenAdapter
import com.example.kknkt.adapter.AbsenningAdapter
import com.example.kknkt.models.Absen
import com.example.kknkt.models.PersonAbsen
import com.example.kknkt.ui.MainActivity
import com.example.kknkt.ui.person.PersonViewModel.SelectOnePersonCallBack
import com.example.kknkt.utils.ExportSQLiteToCSV
import com.example.kknkt.utils.UniqueCodeGenerator
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_q_r_scanner.*
import kotlinx.android.synthetic.main.fragment_absen.*
import kotlinx.android.synthetic.main.fragment_absenning.*
import kotlinx.coroutines.launch
import org.apache.commons.beanutils.converters.IntegerConverter

class AbsenningFragment : Fragment() {

    private  val TAG = "ABSENNINGFRAGMENT"
    private lateinit var absenningAdapter: AbsenningAdapter
    private val args: AbsenningFragmentArgs by navArgs()

    private var absen: Absen? = null

    fun prepareMyFragment(){
        (activity as MainActivity).apply {
            bottom_app_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
            fab.show()
            fab.hide(addVisibilityChanged)
            fab.setImageResource(R.drawable.ic_action_scan)
            btnBack.visibility = View.VISIBLE
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        setRetainInstance(true)
    }

    override fun onResume() {
        super.onResume()
        prepareMyFragment()
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

        prepareMyFragment()

        absen = args.absen

        setupRecyclerView()

        absenningAdapter.setOnClickListener {

            val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.temperature))
            val input = EditText(requireContext())
            var m_Text = ""
            input.inputType = InputType.TYPE_CLASS_NUMBER
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

        btnAbsenUnregistered.setOnClickListener {
            absenForUnregistered()
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

    private fun absenForUnregistered(){
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.add_person))
        val name = EditText(context)
        val address = EditText(context)
        val temperature = EditText(context)
        var nameText = ""
        var addressText = ""
        var temperatureText = ""

        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.VERTICAL
        name.inputType = InputType.TYPE_CLASS_TEXT
        name.hint = getString(R.string.name)
        address.inputType = InputType.TYPE_CLASS_TEXT
        address.hint = getString(R.string.address)
        temperature.inputType = InputType.TYPE_CLASS_NUMBER
        temperature.hint = getString(R.string.temperature)

        linearLayout.addView(name)
        linearLayout.addView(address)
        linearLayout.addView(temperature)

        builder.setView(linearLayout)
        builder.setPositiveButton(getString(R.string.ok)
        ) { _, _ ->
            nameText = name.text.toString()
            addressText = address.text.toString()
            temperatureText = temperature.text.toString()

            if (nameText.isNotEmpty() && addressText.isNotEmpty() && addressText.length > 5 && temperatureText.isNotEmpty()) {
                val person = Person()
                person.apply {
                    this.name = nameText
                    this.address = addressText
                    this.uniqueCode = UniqueCodeGenerator.formatToCode(nameText, addressText)
                }

//            var personAbsen = PersonAbsen()
//            val person = listPerson.get(0)
//            val absen = args.absen

                (activity as MainActivity).viewModel.let {
                    lifecycleScope.launch {
                        it.savePerson(person)
                        it.getPersonByUniqueCode(person.uniqueCode.toString(),
                            object : SelectOnePersonCallBack {
                                override fun onSucces(listPerson: List<Person>) {
                                    Log.d(TAG, "SUCCESS")
                                    val personAbsen = PersonAbsen()
                                    personAbsen.apply {
                                        personId = listPerson.get(0).id
                                        absenId = absen?.id
                                        this.temperature = temperatureText.toInt()
                                    }
                                    it.addPersonAbsen(personAbsen)
                                }
                            })

                    }
                }
            }else{
                Toast.makeText(context,"Please Fill The Blank, address > 5",Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->  dialog.cancel()
        }
        builder.show()


    }
}
