package com.example.marvel_app

import com.example.marvel_app.data.framework.util.generateHash
import junit.framework.TestCase.assertEquals
import org.junit.Test

class GenerateHashCodeTest {


    @Test
    fun `generateHash returns correct MD5 hash`() {

        val ts = "123432"
        val privateKey = "9c3de5724690bf008ef09484f4e6cf283b6707b9"
        val publicKey = "0de055665d65a7da0a64fc7e494ed135"

        val expectedHash = "fa3a11a00b317a11e8f94cc727283381"

        // Act
        val result = generateHash(ts, privateKey, publicKey)

        println(result)

        // Assert
        assertEquals(expectedHash, result)
    }
}
