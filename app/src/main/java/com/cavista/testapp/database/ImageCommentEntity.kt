package com.cavista.testapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cavista.testapp.utility.Utility.Companion.getRandomNumber
import java.util.*

/*
* Entity for storing imageid and comment on its
* */
@Entity(tableName = TABLE_NAME)
data class ImageCommentEntity (
    @PrimaryKey var txnId:Int= getRandomNumber(),
    var imageId:String?,
    @ColumnInfo(name = "comment") var comment:String?
)