package com.cavista.testapp.utility

import android.util.Log
import java.util.*
/*
* Common Utility option which can be used
* */

private var TAG="TestApplication"
fun printLoge(msg:String){
    Log.e(TAG, msg)
}


class Utility {

    companion object{
        const val SELECTED_IMAGE="selcted_image"
        const val REQUEST_HEADER_VALUE="Client-ID 137cda6b5008a7c"
        const val REQUEST_HEADER_KEY="Authorization"
        const val URL="https://api.imgur.com/3/gallery/search/1?q="


        fun getQueryURL(query:String="")= "$URL$query"
        fun getRandomNumber() =Integer.parseInt(Math.abs(Random().nextInt()).toString())
    }




}