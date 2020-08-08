package com.cavista.testapp.utility

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cavista.testapp.imagedetails.repository.DatabaseRepository
import com.cavista.testapp.imagedetails.viewmodel.ImageDetailViewModel
import com.cavista.testapp.imagelist.repository.ImageListRepository
import com.cavista.testapp.imagelist.viewmodel.ImageListViewModel
/*
* ViewModel Factory to provide the specified class
* */
class ViewModelProvideFactory(private val cContext: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when(modelClass){
            ImageListViewModel::class.java->{
                ImageListViewModel(ImageListRepository(cContext)) as T
            }
            ImageDetailViewModel::class.java->{
                ImageDetailViewModel(cRepository = DatabaseRepository(cContext)) as T
            }
            else->{
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}