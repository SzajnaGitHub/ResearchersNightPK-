package com.esspresso.db.userquestions

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_questions")
data class UserQuestion(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val questionAnsweredCorrectly: Boolean,
    val category: String,
    val points: Int,
)
