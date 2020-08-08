package com.cavista.testapp.imagedetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cavista.testapp.R
import com.cavista.testapp.database.ImageCommentEntity

/*
* Recycler view for showing the comments under the selected image
* */

class CommentsAdapter(cContext: Context, cImages:List<ImageCommentEntity>) :RecyclerView.Adapter<CommentsAdapter.CommentAdapterViewHolder>() {
    private var mImageList= cImages
    private var mContext=cContext

    override fun getItemCount()= mImageList.size

    override fun onBindViewHolder(holder: CommentAdapterViewHolder, position: Int) {
        holder.text1.text= mImageList[position].comment
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapterViewHolder {
        var view= LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, parent, false)   // used the default android layout as comment had only string
        return CommentAdapterViewHolder(view)
    }

    class CommentAdapterViewHolder(view: View):RecyclerView.ViewHolder(view){
        var text1:TextView =view.findViewById(android.R.id.text1)

    }

}