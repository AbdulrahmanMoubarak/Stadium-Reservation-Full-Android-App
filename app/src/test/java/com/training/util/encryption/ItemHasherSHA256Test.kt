package com.training.util.encryption

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ItemHasherSHA256Test{

    @Test
    fun test_HashingSuccess(){
        assert(ItemHasherSHA256.hashItem("password").equals( ItemHasherSHA256.hashItem("password")))
    }

    @Test
    fun test_HashingInequality(){
        assertThat(ItemHasherSHA256.hashItem("notPass")).isNotEqualTo(ItemHasherSHA256.hashItem("password"))
    }
}