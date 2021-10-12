package com.training.util.encryption

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.training.util.constants.Encryption
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class ItemEncryptorASETest{

    @Test
    fun test_encryptionDecryption(){
        val str = "01058647856"

        val enc = ItemEncryptorASE().encrypt(str, Encryption.KEY)

        val dec = ItemEncryptorASE().decrypt(enc, Encryption.KEY)

        assert(str.equals(dec))
    }
}