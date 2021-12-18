package com.training.encryption

import com.training.util.encryption.ItemHasherSHA256
import org.junit.Test

class ItemHasherSHA256Test{

    @Test
    fun test_HashingSuccess(){
        assert(ItemHasherSHA256.hashItem("password").equals(ItemHasherSHA256.hashItem("password")))
    }

    @Test
    fun test_HashingInequality(){
        assert(!ItemHasherSHA256.hashItem("notPass").equals(ItemHasherSHA256.hashItem("password")))
    }
}