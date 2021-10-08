package com.training.util

import android.content.Context
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class ItemEncryptorASETest{
    private lateinit var itemEnc: ItemEncryptorASE
    private lateinit var instrumentationContext: Context

    @Before
    fun setup(){
        itemEnc = ItemEncryptorASE()
        instrumentationContext = InstrumentationRegistry.getInstrumentation().context
    }

    @Test fun test_encryptionDecryption(){
        val str = "01058647856"

        val enc = itemEnc.encrypt(instrumentationContext, str)

        val dec = itemEnc.decrypt(instrumentationContext, enc)

        assert(str.equals(String(dec)))
    }
}