package com.example.kknkt.ui.person


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs

import com.example.kknkt.R
import com.example.kknkt.models.Person
import kotlinx.android.synthetic.main.fragment_show_person.*

/**
 * A simple [Fragment] subclass.
 */
class ShowPersonActivity : AppCompatActivity() {
    val TAG: String? = "PersonAddUpdateFragment"
    lateinit var personViewModel: PersonViewModel
    val args: PersonAddUpdateFragmentArgs by navArgs()


    private var person : com.example.kknkt.models.Person? = null
    private val spinner: Spinner? = null
    companion object{
        public val EXTRA_PERSON: String = "extra_person"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_show_person)
//        var update = args.Update

//        if (update.equals("update")){
        person = args.person
        if(person == null)
            person = intent?.getSerializableExtra(EXTRA_PERSON) as Person?
//        }

        if (person==null){
            person =  Person()
        }else{
            edtNik.setText(person!!.nik)
            edtName.setText(person!!.name)
            edtTtl.setText(person!!.ttl)
            spinner?.setSelection(person!!.gender!!)
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
    }
}
