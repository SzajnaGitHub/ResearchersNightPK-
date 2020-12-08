package com.esspresso.nocnaukowcwpk.expiration

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

class ExpirationHandlerTest {
    private lateinit var expirationHandler: ExpirationHandler

    @Before
    fun setup() {
        expirationHandler = ExpirationHandler()
    }

    @Test
    fun expirationTimeWasSetForItem_returnsTrue() {
        expirationHandler.setExpirationTimer("ID1")
        assertThat(expirationHandler.itemExpirationMap.contains("ID1")).isTrue()
    }

    @Test
    fun expirationTimeWasNotSetForItem_returnsFalse() {
        expirationHandler.setExpirationTimer("ID1")
        assertThat(expirationHandler.itemExpirationMap.contains("ID2")).isFalse()
    }

    @Test
    fun expirationHandlerNotDeleteNotExpiredItems_returnsTrue() {
        expirationHandler.setExpirationTimer("ID1")
        expirationHandler.deleteExpiredItems{}
        assertThat(expirationHandler.itemExpirationMap).containsKey("ID1")
    }

    @Test
    fun expirationHandlerDeleteExpiredItems_returnsFalse() {
        expirationHandler.itemExpirationMap["ID1"] = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis()) - 6
        expirationHandler.deleteExpiredItems{}
        assertThat(expirationHandler.itemExpirationMap)
    }
}





