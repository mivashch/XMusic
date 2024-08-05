package com.example.xmusic

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.xmusic.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
//    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        if (! Python.isStarted()) {
            Python.start(AndroidPlatform(this));
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        changeFragment(MainFragment())
        val homeButton:ImageView = findViewById(R.id.homeButton)
        val libButton:ImageView = findViewById(R.id.libButton)
        val settingsButton:ImageView = findViewById(R.id.settingsButton)

        homeButton.setOnClickListener{
            changeFragment(MainFragment())
        }

    libButton.setOnClickListener{
        changeFragment(LibFragment())
    }

    settingsButton.setOnClickListener{
        changeFragment(SettingsFragment())
    }

    }
    fun changeFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
        fragmentTransaction.commit()
    }
}

