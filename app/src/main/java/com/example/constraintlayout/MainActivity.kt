package com.example.constraintlayout

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var edtConta: EditText
    private lateinit var edtPessoas: EditText
    private lateinit var tvResultado: TextView
    private lateinit var fabShare: FloatingActionButton
    private lateinit var fabSpeak: FloatingActionButton
    private lateinit var tts: TextToSpeech
    private var valorPorPessoa: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializa as views
        edtConta = findViewById(R.id.edtConta)
        edtPessoas = findViewById(R.id.edtPessoas)
        tvResultado = findViewById(R.id.tvResultado)
        fabShare = findViewById(R.id.fabShare)
        fabSpeak = findViewById(R.id.fabSpeak)

        // Inicializa o TTS
        tts = TextToSpeech(this, this)

        // Adiciona listeners para os campos de texto
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                calcularValor()
            }
        }

        edtConta.addTextChangedListener(textWatcher)
        edtPessoas.addTextChangedListener(textWatcher)

        // Configura os botões de ação
        fabShare.setOnClickListener { compartilharValor() }
        fabSpeak.setOnClickListener { falarValor() }
    }

    private fun calcularValor() {
        val valorTotal = edtConta.text.toString().toDoubleOrNull() ?: 0.0
        val numPessoas = edtPessoas.text.toString().toIntOrNull() ?: 0

        valorPorPessoa = if (numPessoas > 0) valorTotal / numPessoas else 0.0
        tvResultado.text = String.format("R$ %.2f", valorPorPessoa)
    }

    private fun compartilharValor() {
        val mensagem = "Valor por pessoa: R$ %.2f".format(valorPorPessoa)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, mensagem)
        }
        startActivity(Intent.createChooser(intent, "Compartilhar via"))
    }

    private fun falarValor() {
        val textoParaFalar = "O valor por pessoa é ${String.format("%.2f", valorPorPessoa)} reais"
        tts.speak(textoParaFalar, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale("pt", "BR")
        }
    }

    override fun onDestroy() {
        tts.shutdown()
        super.onDestroy()
    }
}

