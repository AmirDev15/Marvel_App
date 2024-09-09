package com.example.marvel_app.util



import java.security.MessageDigest

fun generateHash(ts: String, privateKey: String, publicKey: String): String {
    val input = "$ts$privateKey$publicKey"
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(input.toByteArray())
    return digest.joinToString("") { String.format("%02x", it) }
}
