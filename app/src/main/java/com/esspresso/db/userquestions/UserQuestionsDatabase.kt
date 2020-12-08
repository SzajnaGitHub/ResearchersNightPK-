package com.esspresso.db.userquestions

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserQuestion::class], version = 1)
abstract class UserQuestionsDatabase : RoomDatabase() {
    abstract fun getUserQuestionsDao(): UserQuestionsDao

    companion object {
        const val USER_QUESTION_DATABASE_NAME = "user_questions_db.db"
    }
}
