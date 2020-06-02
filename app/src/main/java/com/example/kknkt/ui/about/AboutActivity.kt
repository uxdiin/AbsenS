package com.example.kknkt.ui.about

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kknkt.R
import kotlinx.android.synthetic.main.activity_settings.btnBack

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        btnBack.setOnClickListener {
            finish()
        }
    }
}
