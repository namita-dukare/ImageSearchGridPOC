package com.cavista.testapp.imagelist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.cavista.testapp.R
import com.cavista.testapp.imagelist.repository.ImageItem

typealias onItemClickListener =(imageItem:ImageItem?) -> Unit
/*
* Recyclerview adapter for showing images
* */
class ImagesAdapter(cContext: Context, cImages:ArrayList<ImageItem>) : RecyclerView.Adapter<ImagesAdapter.ImageListViewHolder>() {
    private var mImageList= cImages
    private var mContext=cContext
    private lateinit var mClickListener: onItemClickListener

    fun setOnItemClickListener(clickListener: onItemClickListener){
        this.mClickListener= clickListener
    }

    override fun getItemCount()= mImageList.size

    override fun onBindViewHolder(holder: ImageListViewHolder, position: Int) {
        val circularProgressDrawable = CircularProgressDrawable(mContext)
        circularProgressDrawable.strokeWidth = 6f
        circularProgressDrawable.centerRadius = 20f
        circularProgressDrawable.start()

        Glide.with(mContext)
            .load(mImageList[position].link).placeholder(circularProgressDrawable).into(holder.image)
        holder.title.text = mImageList[position].title ?: ""
        holder.image?.setOnClickListener {
            mClickListener?.invoke(mImageList[position])
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageListViewHolder {
        var view= LayoutInflater.from(mContext).inflate(R.layout.image_with_title_item_view, parent,false)
        return ImageListViewHolder(view)
    }


    class ImageListViewHolder: RecyclerView.ViewHolder{
        var image:ImageView
        var title: TextView
        constructor(view: View):super(view){
            image= view.findViewById(R.id.ivItemImage)
            title= view.findViewById(R.id.tvItemTitle)
        }
    }
}

