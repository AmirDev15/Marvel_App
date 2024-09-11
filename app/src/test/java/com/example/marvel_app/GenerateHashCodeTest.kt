package com.example.marvel_app

import com.example.marvel_app.data.framework.util.generateHash
import junit.framework.TestCase.assertEquals
import org.junit.Test

class GenerateHashCodeTest {


    @Test
    fun `generateHash returns correct MD5 hash`() {

        val ts = "123432"
        val publicKey = BuildConfig.PUBLIC_API_KEY
        val privateKey = BuildConfig.PRIVATE_KEY


        val expectedHash = "fa3a11a00b317a11e8f94cc727283381"

        // Act
        val result = generateHash(ts, privateKey, publicKey)

        println(result)

        // Assert
        assertEquals(expectedHash, result)
    }
}
