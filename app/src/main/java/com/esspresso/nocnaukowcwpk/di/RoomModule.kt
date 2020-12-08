package com.esspresso.nocnaukowcwpk.di

import android.content.Context
import androidx.room.Room
import com.esspresso.db.userquestions.UserQuestionsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun providesUserQuestionsDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, UserQuestionsDatabase::class.java, UserQuestionsDatabase.USER_QUESTION_DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideUserQuestionDAO(db: UserQuestionsDatabase) = db.getUserQuestionsDao()

}
