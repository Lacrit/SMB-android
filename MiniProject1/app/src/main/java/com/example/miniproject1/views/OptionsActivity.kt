package com.example.miniproject1.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.miniproject1.databinding.SettingsActivityBinding
import com.example.miniproject1.shared.SharedPrefs
import com.google.android.material.switchmaterial.SwitchMaterial

class OptionsActivity : AppCompatActivity() {
    private lateinit var sharedPrefs : SharedPrefs;

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPrefs = SharedPrefs(this);
        super.onCreate(savedInstanceState)
        val binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setInitDarkMode(binding)
        setInitFontSize(binding)
    }

    private fun setInitDarkMode(binding : SettingsActivityBinding) {
        val switch : SwitchMaterial = binding.switchComp
        if (sharedPrefs.getNightModeState()) {
            switch.isChecked = true;
        }
        switch.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefs.setNightModeState(isChecked)
            restartApp()
        }
    }

    private fun setInitFontSize(binding: SettingsActivityBinding) {
        val radioGroup : RadioGroup = binding.radioGroup
        when (sharedPrefs.getFontSizeState()) {
            SharedPrefs.SMALL -> (radioGroup.getChildAt(0) as RadioButton).isChecked = true
            SharedPrefs.NORMAL -> (radioGroup.getChildAt(1) as RadioButton).isChecked = true
            SharedPrefs.LARGE ->(radioGroup.getChildAt(2) as RadioButton).isChecked = true
        }
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            if (view.isChecked) {
                sharedPrefs.setFontSizeState(view.text as String)
            }
        }
        restartApp()
    }

    private fun restartApp() {
        val intent = Intent(this, OptionsActivity::class.java);
        startActivity(intent)
        finish()
    }


}