package com.esspresso.db.userquestions

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable

@Dao
interface UserQuestionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(question: UserQuestion): Completable

    @Query("SELECT * FROM user_questions")
    fun getAllQuestions(): Observable<List<UserQuestion>>

    @Query("SELECT id FROM user_questions WHERE questionAnsweredCorrectly = 1")
    fun getAllCorrectQuestionsIDS(): Observable<List<String>>

    @Query("SELECT * FROM user_questions WHERE id LIKE :questionId")
    fun getSingleQuestion(questionId: String): Maybe<UserQuestion>

    @Query("DELETE FROM user_questions")
    fun clearDatabase(): Completable
}
