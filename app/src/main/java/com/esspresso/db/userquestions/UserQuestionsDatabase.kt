package com.esspresso.db.userquestions

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [UserQuestion::class], version = 1)
abstract class UserQuestionsDatabase : RoomDatabase() {
    abstract fun getUserQuestionsDao(): UserQuestionsDao
}
