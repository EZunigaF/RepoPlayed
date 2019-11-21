package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.loginapp.model.Post;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SearchActivity extends AppCompatActivity {

    private EditText mSearchField;
    private ImageButton mSearchBtn;
    private RecyclerView mResultList;

    private DatabaseReference mPostReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mPostReference = FirebaseDatabase.getInstance().getReference("Blog");

        mSearchField = (EditText) findViewById(R.id.user_searched_post);
        mSearchBtn = (ImageButton) findViewById(R.id.searchPostBtn);
        mResultList = (RecyclerView) findViewById(R.id.result_post);

        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchText = mSearchField.getText().toString();
                firebaseSearch(searchText);
            }
        });

    }

    private void firebaseSearch(String searchText) {

        Toast.makeText(this, "SEARCHED", Toast.LENGTH_SHORT).show();

        Query firebaseSearchQueryBlog = mPostReference.orderByChild("title").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class,
                R.layout.search_list_layout,
                BlogViewHolder.class,
                firebaseSearchQueryBlog


        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder blogViewHolder, Blog blog, int i) {
                blogViewHolder.setDetails(getApplicationContext(), blog.getCateg(), blog.getTitle(), blog.getimageUrl());
            }
        };

        mResultList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {

        View mPostView;

        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);

            mPostView = itemView;
        }

        public void setDetails(Context ctx, String blogCategory, String title, String blogImage){
            TextView blog_category = (TextView) mPostView.findViewById(R.id.category_post);
            TextView blog_id = (TextView) mPostView.findViewById(R.id.id_post);
            ImageView blog_image = (ImageView) mPostView.findViewById(R.id.icon_result);

            blog_category.setText(blogCategory);

            Glide.with(ctx).load(blogImage).into(blog_image);

            blog_id.setText(title);
        }


    }
}
