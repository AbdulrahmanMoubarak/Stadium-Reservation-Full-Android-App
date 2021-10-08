package com.training.util

import java.security.MessageDigest

class ItemHasherSHA256 {
    companion object {
        fun hashItem(item: String): String {
            val bytes = this.toString().toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            return digest.fold("", { str, it -> str + "%02x".format(it) })
        }

        fun compareHashed(item: String, hashed_item: String): Boolean {
            return hashed_item.equals(hashItem(item))
        }
    }
}