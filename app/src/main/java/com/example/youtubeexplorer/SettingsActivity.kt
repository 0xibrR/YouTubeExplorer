package com.example.youtubeexplorer

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import kotlin.properties.Delegates

class SettingsActivity : AppCompatActivity() {

    private val sharedPrefKey = "appSettings"
    private val nightModeKey = "NightMode"
    lateinit var appPref: SharedPreferences
    lateinit var sharedPrefsEdit: SharedPreferences.Editor
    var nightModeStatus by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        appPref = this.getSharedPreferences(sharedPrefKey, 0)!!
        nightModeStatus = appPref.getInt("NightMode", 3)
        setThemee(nightModeStatus)

        val btnChangeTheme = findViewById<MaterialButton>(R.id.btnToggleTheme)
        val tvVersion = findViewById<TextView>(R.id.tvVersion)

        btnChangeTheme.setOnClickListener {
            setThemeDialog()
        }

        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val versionName = packageInfo.versionName
        tvVersion.text = getString(R.string.app_version_placeholder, versionName)


    }

    private fun setThemee(nightStatus: Int) {
        when (nightStatus) {
            1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                Log.d("AllNoteFrag", "Light theme SetTheme()")
            }

            2 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                Log.d("AllNoteFrag", "Dark theme SetTheme()")
            }

            else -> {
                Log.d("AllNoteFrag", "System theme SetTheme()")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                else
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
            }
        }

    }

    @SuppressLint("UseKtx", "CutPasteId", "SetTextI18n")
    private fun setThemeDialog() {
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.alert_dialog_theme_select, null)
        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val themeRadioGroup = view.findViewById<RadioGroup>(R.id.theme_button_group)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            view.findViewById<RadioButton>(R.id.deafultRadioButton).text = "Choose theme"
        } else {
            view.findViewById<RadioButton>(R.id.deafultRadioButton).text = "Follow Battery save"
        }

        when (nightModeStatus) {
            1 -> view.findViewById<RadioButton>(R.id.lightRadioButton).isChecked = true
            2 -> view.findViewById<RadioButton>(R.id.darkRadioButton).isChecked = true
            3 -> view.findViewById<RadioButton>(R.id.deafultRadioButton).isChecked = true
        }

        themeRadioGroup.setOnCheckedChangeListener { _, id ->
            sharedPrefsEdit = appPref.edit()
            when (id) {
                R.id.lightRadioButton -> {
                    sharedPrefsEdit.putInt(nightModeKey, 1)
                    sharedPrefsEdit.apply()
                    nightModeStatus = 1
                    Log.d("AllNoteFrag", "Light theme")
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }

                R.id.darkRadioButton -> {
                    sharedPrefsEdit.putInt(nightModeKey, 2)
                    sharedPrefsEdit.apply()
                    nightModeStatus = 2
                    Log.d("AllNoteFrag", "Dark theme")
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }

                R.id.deafultRadioButton -> {
                    sharedPrefsEdit.putInt(nightModeKey, 3)
                    sharedPrefsEdit.apply()
                    nightModeStatus = 3
                    Log.d("AllNoteFrag", "System theme")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    )
                    else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)

                }
            }
            dialog.dismiss()
        }

    }
}