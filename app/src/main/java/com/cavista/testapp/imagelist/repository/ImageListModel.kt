package com.cavista.testapp.imagelist.repository

import java.io.Serializable

data class ImageListResponse(
    val data: ArrayList<DataItem?>?,
    val success: Boolean?,
    val status:Int?
)

data class DataItem(
 val id:String?,
 val title:String?,
 val images_count: Int?,
 val images: ArrayList<ImageItem>
)

data class ImageItem(
    val id:String?,
    val description:String?,
    val type:String?,
    val width:Int?,
    val height:Int?,
    val link:String?,
    val title:String?
):Serializable