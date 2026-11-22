package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.engine.OfflineTranslationEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Polyglot", appName)
    }

    @Test
    fun `offline translation russian to chinese`() {
        val result = OfflineTranslationEngine.translate("Привет", "ru", "zh")
        assertEquals("你好", result.translatedText)
        assertEquals("Nǐ hǎo", result.phonetic)
    }

    @Test
    fun `offline translation water to chinese`() {
        val result = OfflineTranslationEngine.translate("Вода", "ru", "zh")
        assertEquals("水", result.translatedText)
    }
}
