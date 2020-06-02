package com.example.kknkt.ui.person

import com.example.kknkt.models.Person
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs

import com.example.kknkt.R
import com.example.kknkt.utils.QRGenerator
import com.example.kknkt.utils.UniqueCodeGenerator
import kotlinx.android.synthetic.main.fragment_person_q_r.*

class PersonQRFragment : Fragment() {

    private lateinit var person: Person
    private val args: PersonQRFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_person_q_r, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        person = args.person!!
        var bitmap = QRGenerator.generate(UniqueCodeGenerator.formatToCode(person.name!!,person.address!!)+","+person.name+","+person.address)
        imgQRCode.setImageBitmap(bitmap)
    }
}
