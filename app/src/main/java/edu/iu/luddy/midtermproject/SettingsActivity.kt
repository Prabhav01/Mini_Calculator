package edu.iu.luddy.midtermproject

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val radioLight = findViewById<RadioButton>(R.id.radioLight)
        val radioDark  = findViewById<RadioButton>(R.id.radioDark)
        val fontSpin   = findViewById<Spinner>(R.id.fontSizeSpinner)
        val colorSpin  = findViewById<Spinner>(R.id.colorSpinner)
        val saveBtn    = findViewById<Button>(R.id.saveButton)

        val fontOpts = listOf("Small", "Medium", "Large")
        val colorOpts = listOf("Accent", "Primary", "Operator")
        fontSpin.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, fontOpts)
        colorSpin.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, colorOpts)

        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        when (prefs.getString("theme", "Light")) {
            "Dark" -> radioDark.isChecked = true
            else   -> radioLight.isChecked = true
        }
        fontSpin.setSelection(fontOpts.indexOf(prefs.getString("fontSize","Medium")))
        colorSpin.setSelection(colorOpts.indexOf(prefs.getString("colorChoice","Accent")))

        radioLight.setOnClickListener {
            radioLight.isChecked = true
            radioDark.isChecked = false
        }

        radioDark.setOnClickListener {
            radioDark.isChecked = true
            radioLight.isChecked = false
        }

        saveBtn.setOnClickListener {
            val themePref = if (radioDark.isChecked) "Dark" else "Light"
            val fontPref = fontSpin.selectedItem?.toString() ?: "Medium"
            val colorPref = colorSpin.selectedItem?.toString() ?: "Accent"

            prefs.edit()
                .putString("theme", themePref)
                .putString("fontSize", fontPref)
                .putString("colorChoice", colorPref)
                .apply()

            finish()
        }
    }
}