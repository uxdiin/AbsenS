package com.example.kknkt.ui.person

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.kknkt.R
import com.example.kknkt.camera.CameraActivity
import com.example.kknkt.models.Person
import com.example.kknkt.ui.MainActivity
import com.example.kknkt.ui.dashboard.BottomNavigationDrawerFragment
import com.example.kknkt.ui.settings.Settings
import com.example.kknkt.utils.DateUtils
import com.example.kknkt.utils.TextRecognition
import com.example.kknkt.utils.UniqueCodeGenerator
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.jaredrummler.materialspinner.MaterialSpinner
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_person_add_update.*
import java.util.*


class PersonAddUpdateFragment : Fragment(R.layout.fragment_person_add_update), TextRecognition.TextRecognitionCallBack {

    val TAG: String? = "PersonAddUpdateFragment"
    lateinit var personViewModel: PersonViewModel
    val args: PersonAddUpdateFragmentArgs by navArgs()

    private var person : Person? = null
    private val spinner: Spinner? = null
    companion object{
        public val REQUEST_GET_TEXT_RESULT = 1
        public val REQUEST_GET_IMAGE = 2
        public val EXTRA_PERSON = "extra_person"
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        personViewModel = (activity as MainActivity).viewModel
        (activity as MainActivity).btnBack.setOnClickListener {
            goBack()
        }
        prepareDatePicker()
        prepareSpinner()
        var update = args.Update

        if (update.equals("update")){
            person = args.person
        }

        if (person==null){
            person =  Person()
        }else{
            setToEditText(person!!)
        }
        (activity as MainActivity).fab.setOnClickListener {
            personViewModel.deletePerson(person!!)
            goBack()
        }

        btnSave.setOnClickListener {
            Log.d(TAG,"terclick")
            setToPerson()
            personViewModel.savePerson(person!!)
            goBack()
        }
    }
    private fun setToPerson(){
        person?.apply {
            nik = edtNik.text.toString().trim()
            name = edtName.text.toString().trim()
            ttl = edtTtl.text.toString().trim()
            gender = materialGenderSpinner.selectedIndex
            address = edtAddress.text.toString().trim()
            rt = edtRt.text.toString().trim()
            rw = edtRw.text.toString().trim()
            village = edtVillage.text.toString().trim()
            subDistrict = edtSubDistrict.text.toString().trim()
            religion = edtReligion.text.toString().trim()
            job = edtReligion.text.toString().trim()
            citizenship = edtCitizenship.text.toString().trim()
            arrivalDate = edtArrivalDate.text.toString().trim()
            freeDate = edtFreeDate.text.toString().trim()

        }
        person?.uniqueCode = UniqueCodeGenerator.formatToCode(person?.name!!,person?.address!!)

    }
    private fun goBack(){
        (activity as MainActivity).fab.hide(
            (activity as MainActivity).addVisibilityChanged)
        (activity as MainActivity).tvTitleFragment.setText(getString(R.string.quarantined))
        (activity as MainActivity).btnBack.visibility = View.GONE
        findNavController().navigate(R.id.action_personAddUpdateFragment_to_personFragment)
    }
    private fun prepareSpinner(){

        materialGenderSpinner.setItems(
            getString(R.string.male),
            getString(R.string.female)
        )
    }
    private fun prepareDatePicker(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        edtArrivalDate.setOnClickListener {
            Log.d(TAG,"terclick")
            var dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                month = monthOfYear + 1
                edtArrivalDate.setText(""+year + "-" + month +"-" + dayOfMonth)
                var date = Date(year, month, dayOfMonth)
                var freeDate = DateUtils.addDate(date,Settings.numberOfQuarantineDays)

                edtFreeDate.setText(freeDate.year.toString()+"-"+freeDate.month.toString()+"-"+freeDate.date.toString())
            }, year, month, day)
            dpd.show()

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_free -> {
                Log.d(TAG,"free")
                findNavController().navigate(R.id.action_personFragment_to_freePersonFragment)
            }
            android.R.id.home -> {
                val bottomNavDrawerFragment = BottomNavigationDrawerFragment()
                bottomNavDrawerFragment.show((activity as MainActivity).supportFragmentManager, bottomNavDrawerFragment.tag)
            }
            R.id.navigation_camera -> {
                val intent = Intent(requireContext(), CameraActivity::class.java)
                startActivityForResult(intent, REQUEST_GET_TEXT_RESULT)
            }
            R.id.navigation_photo -> {
                val intent = Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_GET_CONTENT)
                startActivityForResult(Intent.createChooser(intent, "Select a file"),
                    REQUEST_GET_IMAGE)
            }
            R.id.navigation_QR -> {
                setToPerson()
                val bundle = Bundle().apply{
                    putSerializable("person",person)
                }
                findNavController().navigate(R.id.action_personAddUpdateFragment_to_personQRFragment,bundle)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setToEditText(person: Person){
        edtNik.setText(person!!.nik)
        edtName.setText(person!!.name)
        edtTtl.setText(person!!.ttl)
        person.gender?.let {
            materialGenderSpinner.setSelectedIndex(person!!.gender!!)
        }
        edtAddress.setText(person!!.address)
        edtRt.setText(person!!.rt)
        edtRw.setText(person!!.rw)
        edtVillage.setText(person!!.village)
        edtSubDistrict.setText(person!!.subDistrict)
        edtReligion.setText(person!!.religion)
        edtJob.setText(person!!.job)
        edtCitizenship.setText(person!!.citizenship)
        edtArrivalDate.setText(person!!.arrivalDate)
        edtFreeDate.setText(person!!.freeDate)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_GET_TEXT_RESULT && resultCode == Activity.RESULT_OK){
            person = data?.getSerializableExtra(EXTRA_PERSON) as Person?
            setToEditText(person!!)
        }else if(requestCode == REQUEST_GET_IMAGE && resultCode == Activity.RESULT_OK){
            var image = FirebaseVisionImage.fromFilePath(requireContext(), data?.data!!)
            TextRecognition(requireContext(),this).readLocal(image)
        }

    }

    override fun onSuccessRead(person: Person) {
        setToEditText(person)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.bottom_nav_menu_add_update,menu)
    }
}
