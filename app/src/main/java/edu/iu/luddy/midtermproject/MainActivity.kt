package edu.iu.luddy.midtermproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private lateinit var display: TextView

    private var input = "0"
    private var d: BigDecimal? = null
    private var c: Char? = null
    private var e = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        display = findViewById(R.id.display)

        if (savedInstanceState != null) {
            input = savedInstanceState.getString("display_input", "0")
            d = savedInstanceState.getString("acc")?.toBigDecimalOrNull()
            c = savedInstanceState.getChar("op", '\u0000').takeIf { it != '\u0000' }
            e = savedInstanceState.getBoolean("afterEq", false)
            updateDisplay(d?.takeIf { c != null && input == "0" })
        }

        listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        ).forEach { id ->
            findViewById<Button>(id).setOnClickListener {
                onDigit((it as Button).text.toString())
            }
        }

        findViewById<Button>(R.id.btnAdd).setOnClickListener { onOp('+') }
        findViewById<Button>(R.id.btnSubtract).setOnClickListener { onOp('-') }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { onOp('*') }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { onOp('/') }
        findViewById<Button>(R.id.btnEquals).setOnClickListener { onEquals() }

        findViewById<Button>(R.id.btnDecimal).setOnClickListener { onDot() }
        findViewById<Button>(R.id.btnBackspace).setOnClickListener { onBackspace() }
        findViewById<Button>(R.id.btnSquareRoot).setOnClickListener { onSqrt() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { confirmClear() }

        findViewById<ImageView>(R.id.settingsIcon).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        applySettingsToMain()
        updateDisplay()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("display_input", input)
        outState.putString("acc", d?.toPlainString())
        c?.let { outState.putChar("op", it) }
        outState.putBoolean("afterEq", e)
    }

    override fun onResume() {
        super.onResume()
        applySettingsToMain()
    }

    override fun onStop() {
        super.onStop()
        // spec: reset to zero when app becomes invisible
        softReset()
    }

    private fun onDigit(s: String) {
        if (e) { input = "0"; e = false }
        val count = input.count { it.isDigit() }
        if (count >= 5 && input != "0") {
            Toast.makeText(this, "Max 5 digits", Toast.LENGTH_SHORT).show()
            return
        }
        input = if (input == "0") s else input + s
        updateDisplay()
    }

    private fun onDot() {
        if (e) { input = "0"; e = false }
        if (!input.contains('.')) {
            input += if (input.isEmpty()) "0." else "."
            updateDisplay()
        }
    }

    private fun onBackspace() {
        if (e) return
        input = if (input.length <= 1) "0" else input.dropLast(1)
        if (input == "-" || input == "-0") input = "0"
        updateDisplay()
    }

    private fun onSqrt() {
        val x = input.toBigDecimalOrNull() ?: BigDecimal.ZERO
        if (x < BigDecimal.ZERO) {
            Toast.makeText(this, "Cannot sqrt negative", Toast.LENGTH_SHORT).show()
            return
        }
        val res = BigDecimal(sqrt(x.toDouble()))
        input = res.setScale(5, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()
        d = null; c = null; e = true
        updateDisplay()
    }

    private fun onOp(op: Char) {
        val cur = input.toBigDecimalOrNull() ?: BigDecimal.ZERO
        d = when {
            d == null -> cur
            !e        -> applyOp(d!!, cur, c)
            else      -> d
        }
        c = op
        input = "0"
        e = false
        updateDisplay(d)
    }

    private fun onEquals() {
        val cur = input.toBigDecimalOrNull() ?: BigDecimal.ZERO
        if (d == null || c == null) { updateDisplay(); return }
        val res = applyOp(d!!, cur, c)
        input = res.setScale(5, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()
        d = null; c = null; e = true
        updateDisplay()
    }

    private fun confirmClear() {
        AlertDialog.Builder(this)
            .setTitle("Reset")
            .setMessage("Clear the current value?")
            .setPositiveButton("Yes") { _, _ -> softReset() }
            .setNegativeButton("No", null)
            .show()
    }

    private fun softReset() {
        input = "0"; d = null; c = null; e = false
        updateDisplay()
    }

    private fun applyOp(a: BigDecimal, b: BigDecimal, op: Char?): BigDecimal = when (op) {
        '+' -> a + b
        '-' -> a - b
        '*' -> a * b
        '/' -> if (b.compareTo(BigDecimal.ZERO) == 0) {
            Toast.makeText(this, "Division by zero", Toast.LENGTH_SHORT).show()
            BigDecimal.ZERO
        } else a.divide(b, 10, RoundingMode.HALF_UP)
        else -> b
    }

    private fun updateDisplay(value: BigDecimal? = null) {
        display.text = value?.toPlainString() ?: input
    }

    private fun applySettingsToMain() {
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val themePref = prefs.getString("theme", null)
        val font = prefs.getString("fontSize", null)
        val color = prefs.getString("colorChoice", null)

        val root: View = findViewById(android.R.id.content)

        when (themePref) {
            "Dark" -> root.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_background))
            "Light" -> root.setBackgroundColor(ContextCompat.getColor(this, R.color.light_background))
        }

        when (font) {
            "Small" -> display.textSize = 32f
            "Medium" -> display.textSize = 40f
            "Large" -> display.textSize = 48f
        }

        val tc = when (color) {
            "Accent" -> ContextCompat.getColor(this, R.color.accent)
            "Primary" -> ContextCompat.getColor(this, R.color.light_primary)
            "Operator" -> ContextCompat.getColor(this, R.color.operator_button)
            else -> null
        }
        tc?.let { display.setTextColor(it) }
    }
}