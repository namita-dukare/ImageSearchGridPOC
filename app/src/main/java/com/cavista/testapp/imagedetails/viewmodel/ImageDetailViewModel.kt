package com.cavista.testapp.imagedetails.viewmodel

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cavista.testapp.ApplicationClass
import com.cavista.testapp.imagedetails.repository.DatabaseRepository
import com.cavista.testapp.database.ImageCommentEntity
import com.cavista.testapp.utility.printLoge
/*
* View Model for ImageDetail screen
* */
class ImageDetailViewModel(private val cRepository: DatabaseRepository) : ViewModel() {


    var mLiveData: MutableLiveData<ImageDetailsOperation> = MutableLiveData()

    sealed class ImageDetailsOperation {
        class GetImageCommentsFromDB(val result: List<ImageCommentEntity>) : ImageDetailsOperation()
        class GetImageCommentsError(val result:String): ImageDetailsOperation()
    }

    @SuppressLint("StaticFieldLeak")
    fun saveComment(imageId:String, comment:String) {
        var img= ImageCommentEntity(imageId = imageId, comment = comment)
        object : AsyncTask<ImageCommentEntity, Void?, Void?>() {
            override fun doInBackground(vararg p0: ImageCommentEntity?): Void? {
                cRepository.saveComment(img)
                return null
            }
        }.execute(img)
    }

    @SuppressLint("StaticFieldLeak")
    fun getComments(imageId: String) {
        object :AsyncTask<String, Void?, Void?>(){
            override fun doInBackground(vararg p0: String?): Void? {
                var list = cRepository.getComments(imageId)
                mLiveData.postValue(ImageDetailsOperation.GetImageCommentsFromDB(result = list))
                printLoge("Found comments-> ${list.size}")
                return null
            }
        }.execute()

    }


}