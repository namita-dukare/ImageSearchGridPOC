package com.cavista.testapp.imagedetails.repository

import android.content.Context
import com.cavista.testapp.database.AppDatabase
import com.cavista.testapp.database.ImageCommentEntity

/* Repository to perform data operations for image details screen
* */

class DatabaseRepository(cContext: Context) {
    private var mContext= cContext

    fun getAppDBInstance()=
        AppDatabase.invoke(
            context = mContext
        )

    fun saveComment(img: ImageCommentEntity){
        try {
            getAppDBInstance().getImageCommentDao().insert(img)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getComments(imgId:String): List<ImageCommentEntity>{
        var list:List<ImageCommentEntity> = ArrayList()
        try{
            list= getAppDBInstance().getImageCommentDao().getComments(imgId)
        }catch(e:Exception){
            e.printStackTrace()
        }
        return list
    }
}