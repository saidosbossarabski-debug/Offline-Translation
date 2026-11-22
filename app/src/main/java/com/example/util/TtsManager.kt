package com.example.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class TtsManager(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isInitialized = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            tts?.setSpeechRate(0.9f)
        } else {
            Log.e("TtsManager", "TTS initialization failed: $status")
        }
    }

    fun speak(text: String, langCode: String) {
        if (!isInitialized || tts == null || text.isBlank()) return

        val locale = getLocaleForCode(langCode)
        try {
            val result = tts?.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Fallback to English or default
                tts?.setLanguage(Locale.US)
            }
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts_${System.currentTimeMillis()}")
        } catch (e: Exception) {
            Log.e("TtsManager", "Error speaking text", e)
        }
    }

    private fun getLocaleForCode(code: String): Locale {
        val clean = code.lowercase()
        return when {
            clean.startsWith("zh") -> Locale.SIMPLIFIED_CHINESE
            clean.startsWith("ru") -> Locale("ru", "RU")
            clean.startsWith("en") -> Locale.US
            clean.startsWith("es") -> Locale("es", "ES")
            clean.startsWith("fr") -> Locale.FRANCE
            clean.startsWith("de") -> Locale.GERMANY
            clean.startsWith("it") -> Locale.ITALY
            clean.startsWith("ja") -> Locale.JAPAN
            clean.startsWith("ko") -> Locale.KOREA
            clean.startsWith("tr") -> Locale("tr", "TR")
            clean.startsWith("ar") -> Locale("ar")
            clean.startsWith("pt") -> Locale("pt", "BR")
            clean.startsWith("hi") -> Locale("hi", "IN")
            clean.startsWith("kk") -> Locale("kk", "KZ")
            clean.startsWith("uz") -> Locale("uz", "UZ")
            clean.startsWith("uk") -> Locale("uk", "UA")
            clean.startsWith("pl") -> Locale("pl", "PL")
            else -> Locale(clean)
        }
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
            tts = null
        } catch (e: Exception) {
            Log.e("TtsManager", "Error shutting down TTS", e)
        }
    }
}
