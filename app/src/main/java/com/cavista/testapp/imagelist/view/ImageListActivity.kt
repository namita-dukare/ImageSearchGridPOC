package com.cavista.testapp.imagelist.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.cavista.testapp.R
import com.cavista.testapp.imagedetails.view.ImageDetailActivity
import com.cavista.testapp.imagelist.adapter.ImagesAdapter
import com.cavista.testapp.imagelist.repository.ImageItem
import com.cavista.testapp.imagelist.viewmodel.ImageListViewModel
import com.cavista.testapp.utility.Utility.Companion.SELECTED_IMAGE
import com.cavista.testapp.utility.ViewModelProvideFactory
import com.cavista.testapp.utility.printLoge
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.roundToInt


class ImageListActivity : AppCompatActivity() {
    private lateinit var mViewModel: ImageListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mViewModel= ViewModelProviders.of(this,ViewModelProvideFactory(applicationContext)) .get(ImageListViewModel::class.java)
        setImageListObserver()

        imgSearch?.setOnClickListener {
            var query= edtSearch?.text.toString().trim()
            if(TextUtils.isEmpty(query)){
                edtSearch?.setError(getString(R.string.str_empty_field))
            }else{
                showLoader()
                mViewModel.getImageList(query)
            }
        }
    }
    var list= ArrayList<ImageItem>()
    private fun setImageListObserver(){
        mViewModel.itemListLiveData.observe(this, Observer{operation->
            hideLoader()
            when(operation){
                is ImageListViewModel.GetItemListOperation.GetItemListSuccess->{
                    list.clear()
                    operation.result.data?.forEach {
                        var filter= it?.images?.filter { it.type.equals("image/jpeg")}
                        list.addAll(filter ?: ArrayList())
                    }
                    if(list.isNotEmpty()){
                        var adapter= ImagesAdapter(this, list)
                        adapter.setOnItemClickListener {
                            var intent= Intent(this, ImageDetailActivity::class.java)
                            intent.putExtra(SELECTED_IMAGE, it)
                            startActivity(intent)
                        }
                        rvImages?.adapter= adapter
                        tvEmptyImagesRecyclerView?.visibility= View.GONE
                    }else{
                        tvEmptyImagesRecyclerView?.visibility= View.VISIBLE
                    }
                }
                is ImageListViewModel.GetItemListOperation.GetItemListError->{
                    operation.error?.let{
                        printLoge("Volley error-> ${it.message}")
                        Toast.makeText(this, getString(R.string.str_err), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
    private fun setupRecylerViewForGridLayout(){
        rvImages?.setLayoutManager( GridLayoutManager(this, getDynamicNumberOfColumns()))
    }

    private fun showLoader(){
        pBar.visibility= View.VISIBLE
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        rvImages?.visibility= View.GONE
        dismissKeyboard(this, this.currentFocus ?: View(this))
        tvEmptyImagesRecyclerView.visibility= View.GONE
    }
    private fun hideLoader(){
        pBar.visibility= View.GONE
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        rvImages?.visibility= View.VISIBLE

    }
    fun dismissKeyboard(context: Context, view: View) {
        val imm: InputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onResume() {
        super.onResume()
       setupRecylerViewForGridLayout()
    }

    fun getDynamicNumberOfColumns(): Int{
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        var columnNumber= width/dpToPx(this, 150)
        return Math.round(columnNumber)
    }

    fun dpToPx(context: Context, dpValue: Int): Float {
        return (dpValue * (context.resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt().toFloat()
    }
}
