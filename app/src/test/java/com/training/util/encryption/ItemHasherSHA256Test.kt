package com.training.util.encryption

import org.junit.Assert.*
import org.junit.Test

class ItemHasherSHA256Test{

    @Test
    fun test_HashingSuccess(){
        assert(ItemHasherSHA256.hashItem("password").equals( ItemHasherSHA256.hashItem("password")))
    }
}