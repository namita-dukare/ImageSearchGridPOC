package com.cavista.testapp.imagelist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.VolleyError
import com.cavista.testapp.imagelist.repository.ImageListRepository
import com.cavista.testapp.imagelist.repository.ImageListResponse
import com.cavista.testapp.utility.printLoge
/*
* ViewModel for image list screen
* */
class ImageListViewModel(private val cRepository:ImageListRepository) : ViewModel() {

    var itemListLiveData: MutableLiveData<GetItemListOperation> = MutableLiveData()


    sealed class GetItemListOperation{
        class GetItemListSuccess( val result: ImageListResponse): GetItemListOperation()
        class GetItemListError(val error:VolleyError):GetItemListOperation()
    }

    fun getImageList(query:String){

        cRepository.getImageList(query){imageListResponse, volleyError ->
            imageListResponse?.let{
                itemListLiveData.postValue(GetItemListOperation.GetItemListSuccess(result = it))
            }
            volleyError?.let{
                itemListLiveData.postValue(GetItemListOperation.GetItemListError(error = it))
            }
        }
    }
}