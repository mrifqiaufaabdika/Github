package com.example.github.activites

import android.content.Context
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.github.databinding.ActivitySettingBinding
import com.example.github.datastore.SettingPreferences
import com.example.github.ui.main.MainViewModel
import com.example.github.ui.main.ViewModelFactory
import com.google.android.material.switchmaterial.SwitchMaterial

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var switchTheme: SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val activity = this
        val pref = SettingPreferences.getInstance(dataStore)

        val mainViewModel =
            ViewModelProvider(this, ViewModelFactory(activity.application, pref)).get(
                MainViewModel::class.java
            )

        mainViewModel.getThemeSettings().observe(this,
            { isDrakModeActive: Boolean ->
                if (isDrakModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    switchTheme.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    switchTheme.isChecked = false
                }

            })

        switchTheme = binding.switchTheme
        switchTheme.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)
        }

    }
}