package com.example.marvel_app.util

import android.util.Log

fun testGenerateHash() {
    // Known values
    val ts = "123432"  // Example timestamp
    val privateKey = "9c3de5724690bf008ef09484f4e6cf283b6707b9"
    val publicKey = "0de055665d65a7da0a64fc7e494ed135"

    // Generate hash
    val generatedHash = generateHash(ts, privateKey, publicKey)

    // Expected hash (replace this with the correct expected value for the given input)
    val expectedHash = "fa3a11a00b317a11e8f94cc727283381" // Example expected hash

    // Log results
    Log.d("HashTest", "Generated Hash: $generatedHash")
    Log.d("HashTest", "Expected Hash: $expectedHash")

    // Verify if the generated hash matches the expected hash
    if (generatedHash == expectedHash) {
        Log.d("HashTest", "Hash generation is correct!")
    } else {
        Log.e("HashTest", "Hash generation is incorrect!")
    }
}
