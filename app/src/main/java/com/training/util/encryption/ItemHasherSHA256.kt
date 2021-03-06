package com.training.util.encryption

import java.security.MessageDigest

class ItemHasherSHA256 {
    companion object {
        fun hashItem(item: String): String {
            return try {
                val digest: MessageDigest = MessageDigest.getInstance("SHA-256")
                val hash: ByteArray = digest.digest(item.toByteArray(charset("UTF-8")))
                val hexString = StringBuffer()
                for (i in hash.indices) {
                    val hex = Integer.toHexString(0xff and hash[i].toInt())
                    if (hex.length == 1) hexString.append('0')
                    hexString.append(hex)
                }
                hexString.toString()
            } catch (ex: Exception) {
                throw RuntimeException(ex)
            }
        }
    }
}