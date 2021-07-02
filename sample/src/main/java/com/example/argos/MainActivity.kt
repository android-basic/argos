package com.example.argos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import redroid.log.argos.Argos

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Argos.d("onCreate")
    }
}