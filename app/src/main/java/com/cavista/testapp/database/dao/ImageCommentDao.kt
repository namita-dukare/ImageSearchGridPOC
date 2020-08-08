package com.cavista.testapp.database.dao

import androidx.room.*
import com.cavista.testapp.database.ImageCommentEntity
/*
* Methods to perform CRUD operation on image_comment table
* */
@Dao
interface ImageCommentDao{
    @Query("SELECT * FROM image_comment WHERE imageId LIKE :id")
    fun getComments(id: String): List<ImageCommentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(comment: ImageCommentEntity)

    @Update
    fun updateTodo(vararg comment: ImageCommentEntity)

}