package com.esspresso.nocnaukowcwpk.expiration

import java.util.concurrent.TimeUnit

class ExpirationHandler {

    var itemExpirationMap = mutableMapOf<String, Long>()
        private set

    fun setExpirationTimer(id: String) {
        itemExpirationMap[id] = getCurrentTimeInMinutes()
    }

    fun deleteExpiredItems(onDeleteItemAction: (String) -> Unit) {
        val itemsToDelete = mutableListOf<String>()
        itemExpirationMap.forEach {
            if (getCurrentTimeInMinutes() - it.value >= EXPIRATION_TIME_IN_MIN) {
                onDeleteItemAction.invoke(it.key)
                itemsToDelete.add(it.key)
            }
        }
        itemsToDelete.forEach { itemExpirationMap.remove(it) }
    }

    private fun getCurrentTimeInMinutes() = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis())

    companion object {
        private const val EXPIRATION_TIME_IN_MIN = 5
    }
}
