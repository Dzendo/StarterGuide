package com.dinadurykina.starter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<View>(R.id.button)
        button.setOnClickListener {
            val intent = Intent("com.dinadurykina.mediagid.MediaGuide")
            intent.putExtra("FragmentName", "nav_p_4")
            startActivity(intent)
        }
    }
}