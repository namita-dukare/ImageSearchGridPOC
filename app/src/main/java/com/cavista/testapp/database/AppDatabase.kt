package com.cavista.testapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cavista.testapp.database.dao.ImageCommentDao
/*
* This class is used to create Db and get instance of room db
* */
@Database(entities = arrayOf(ImageCommentEntity::class), version = 1)
abstract class AppDatabase :RoomDatabase(){

    abstract fun getImageCommentDao(): ImageCommentDao

    companion object{
        @Volatile private var instance: AppDatabase? = null

        operator fun invoke(context: Context)= instance ?: synchronized(Any()){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            AppDatabase::class.java, "comments.db")
            .build()
    }
}