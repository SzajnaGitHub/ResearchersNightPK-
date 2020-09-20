package com.esspresso.nocnaukowcwpk.database

import com.esspresso.nocnaukowcwpk.database.user.DBUserModel
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseManager @Inject constructor() {
    private val databaseInstance = FirebaseFirestore.getInstance()
    private val userDirectory = databaseInstance.document("users/Waldek")
    var initialUserModel: DBUserModel? = null

    private fun getUserModel() = userDirectory.get().addOnSuccessListener {
        if (it.exists()) {
            val userModel = it.toObject(DBUserModel::class.java)
            initialUserModel = userModel
            println("TEKST ${userModel?.number_of_points}")
        }
    }

    fun saveUserToDatabase() {
        initialUserModel?.number_of_points = 16
        initialUserModel?.let {
            println("TEKST USERMODEL TO SAVE ${it.number_of_points}")
            userDirectory.set(it).addOnSuccessListener {
                println("TEKST DB USER ADDED")
            }
        }
    }

    init {
        getUserModel()
    }
}
