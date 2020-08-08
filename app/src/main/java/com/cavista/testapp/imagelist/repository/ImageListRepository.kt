package com.cavista.testapp.imagelist.repository

import android.content.Context
import com.android.volley.Response
import com.android.volley.VolleyError
import com.cavista.testapp.network.GsonVolleyRequest
import com.cavista.testapp.utility.Utility
import org.json.JSONObject


typealias getImageListSuccess = (ImageListResponse?, VolleyError?) -> Unit

/*
* Repository for ImageList screen
* */
class ImageListRepository(private val cContext: Context) {

    fun getImageList(query:String, callBack:getImageListSuccess ){
        GsonVolleyRequest(
            cContext = cContext,
            cUrl = Utility.getQueryURL(query),
            cClazz = ImageListResponse::class.java,
            cErrorListener = Response.ErrorListener {
                callBack.invoke(null, it)
            },
            cListener = Response.Listener {
                callBack.invoke(it, null)
            },
            cBody = JSONObject()
        ).performRequest()
    }

}