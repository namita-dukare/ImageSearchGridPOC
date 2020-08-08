package com.cavista.testapp.imagedetails.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.cavista.testapp.R;
import com.cavista.testapp.database.AppDatabase;
import com.cavista.testapp.database.ImageCommentEntity;
import com.cavista.testapp.imagedetails.CommentsAdapter;
import com.cavista.testapp.imagedetails.viewmodel.ImageDetailViewModel;
import com.cavista.testapp.imagedetails.viewmodel.ImageDetailViewModel.ImageDetailsOperation.GetImageCommentsFromDB;
import com.cavista.testapp.imagedetails.viewmodel.ImageDetailViewModel.ImageDetailsOperation.GetImageCommentsError;
import com.cavista.testapp.imagelist.repository.ImageItem;
import com.cavista.testapp.utility.ViewModelProvideFactory;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import static com.cavista.testapp.utility.Utility.SELECTED_IMAGE;

/*
* Activity displayed when clicked on a image item from the list
* */

public class ImageDetailActivity extends AppCompatActivity {
    private ImageItem item;
    private ImageView ivDetailedImage;
    private ImageButton ibSubmitComment;
    private TextInputEditText edtComment;
    private ImageDetailViewModel mViewModel;
    private RecyclerView rvComments;
    private TextView tvEmptyRecyclerView;
private ArrayList<ImageCommentEntity> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        item = (ImageItem) getIntent().getSerializableExtra(SELECTED_IMAGE);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(item.getTitle()== null? "Dummy title":item.getTitle());
        getSupportActionBar().setIcon(R.drawable.ic_search_black_24dp);


        mViewModel = ViewModelProviders.of(this, new ViewModelProvideFactory(this)).get(ImageDetailViewModel.class);
        final AppDatabase appDatabase = AppDatabase.Companion.invoke(this);
        ivDetailedImage = findViewById(R.id.ivDetailedImage);
        ibSubmitComment = findViewById(R.id.ibSubmitComment);
        edtComment = findViewById(R.id.edtComment);
        rvComments = findViewById(R.id.rvComments);
        tvEmptyRecyclerView = findViewById(R.id.tvEmptyRecyclerView);

        setupRecyclerView();
        Glide.with(this).load(item.getLink()).into(ivDetailedImage);
        getCommentsForImage();
        setupObserver();
        ibSubmitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = edtComment.getText().toString().trim();
                if (TextUtils.isEmpty(text)) {
                    edtComment.setError(getString(R.string.str_empty_field));
                } else {

                    mViewModel.saveComment(item.getId(), text);
                    edtComment.setText("");
                    getCommentsForImage();
                }
            }
        });
    }

    private void getCommentsForImage() {
        mViewModel.getComments(item.getId());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void setupObserver() {
        mViewModel.getMLiveData().observe(this, new Observer<ImageDetailViewModel.ImageDetailsOperation>() {
            @Override
            public void onChanged(ImageDetailViewModel.ImageDetailsOperation imageDetailsOperation) {
                if (imageDetailsOperation instanceof GetImageCommentsFromDB) {
                    if(list== null){
                        list= new ArrayList<>(((GetImageCommentsFromDB) imageDetailsOperation).getResult());
                    }else{
                        list.clear();
                        list.addAll(((GetImageCommentsFromDB) imageDetailsOperation).getResult());
                    }
                    if(!list.isEmpty()){
                        CommentsAdapter adapter= new CommentsAdapter(ImageDetailActivity.this, ((GetImageCommentsFromDB) imageDetailsOperation).getResult());
                        rvComments.setAdapter(adapter);
                        tvEmptyRecyclerView.setVisibility(View.GONE);
                    }else{
                        tvEmptyRecyclerView.setVisibility(View.VISIBLE);
                    }

                }
                if(imageDetailsOperation instanceof GetImageCommentsError){
                    Toast.makeText(ImageDetailActivity.this, getString(R.string.str_err), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupRecyclerView() {
        LinearLayoutManager layout = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvComments.setLayoutManager(layout);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(ImageDetailActivity.this, R.drawable.vertical_divider_decorator));
        rvComments.addItemDecoration(divider);
        rvComments.setFocusable(false);
    }



}
