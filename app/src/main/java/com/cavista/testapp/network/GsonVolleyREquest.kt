package com.cavista.testapp.network

import android.content.Context
import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.Volley
import com.cavista.testapp.utility.Utility.Companion.REQUEST_HEADER_KEY
import com.cavista.testapp.utility.Utility.Companion.REQUEST_HEADER_VALUE
import com.cavista.testapp.utility.printLoge
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

/*
* Network layer-- Performing network requests
* */

class GsonVolleyRequest<T>(
    private val cContext: Context,
    cMethod: Int = Method.GET,   // default method GET, can be changed
    cUrl: String?,   // provides URL
    cBody: Any,    // request body-- Currently no need to pass any request body so this is empty, but can be used in future
    private val cClazz: Class<T>,     // need to specify the response model/POJO to parse the string response to specified model class
    private val cListener: Response.Listener<T>,    // listener to set response success  when request is performed
    cErrorListener: Response.ErrorListener    // error listener
) : JsonRequest<T>(cMethod, cUrl, Gson().toJson(cBody), cListener, cErrorListener) {

    init {
        printLoge("Request URL :: $cUrl")
    }
    private val gson = Gson()

    override fun getHeaders(): MutableMap<String, String> {
        val params = HashMap<String, String>()
        params[REQUEST_HEADER_KEY]= REQUEST_HEADER_VALUE
        return params
    }

    override fun deliverResponse(response: T) = cListener.onResponse(response)

    override fun parseNetworkResponse(response: NetworkResponse?): Response<T> {
        return try {
            val json = String(
                response?.data ?: ByteArray(0),
                Charset.forName(HttpHeaderParser.parseCharset(response?.headers))
            )
            printLoge("Raw Response :: $json")
            Response.success(
                gson.fromJson(json, cClazz),
                HttpHeaderParser.parseCacheHeaders(response)
            )
        } catch (e: UnsupportedEncodingException) {
            Response.error(ParseError(e))
        } catch (e: JsonSyntaxException) {
            Response.error(ParseError(e))
        }
    }

    fun performRequest(){    // actually adds the request to volleyRequest
        var volleyRequestQueue= Volley.newRequestQueue(cContext)
        volleyRequestQueue.add(this)
    }
}