package com.cavista.testapp

import android.app.Application

/*
* Application class
* */
class ApplicationClass : Application(){
    override fun onCreate() {
        super.onCreate()
        instance= this
    }

    companion object{

        @get:Synchronized
        var instance:ApplicationClass? = null
    }

}