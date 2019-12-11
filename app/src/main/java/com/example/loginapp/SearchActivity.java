package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.loginapp.Adapters.MyApplication;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity {

    private EditText mSearchField;
    private ImageButton mSearchBtn;
    private RecyclerView mResultList;
    private TextWatcher mSearchWatcher = null;
    private DatabaseReference mPostReference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



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

        mSearchWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mSearchField.getText().toString().equals(null)||mSearchField.getText().toString().equals("") ){
                    Toast.makeText(SearchActivity.this, "VACIOO NULLL ", Toast.LENGTH_SHORT).show();
                    String searchText = "";
                    firebaseSearch(searchText);
                }else {
                    String searchText = mSearchField.getText().toString().trim();
                    String queryText = searchText.substring(0,1).toUpperCase() + searchText.substring(1);
                    firebaseSearch(queryText);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        };

        mSearchField.addTextChangedListener(mSearchWatcher);

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchField.getText().toString().equals(null)||mSearchField.getText().toString().equals("") ){
                    Toast.makeText(SearchActivity.this, "VACIOO NULLL ", Toast.LENGTH_SHORT).show();
                    String searchText = "";
                    firebaseSearch(searchText);

                }else {
                    String searchText = mSearchField.getText().toString().trim();
                    String queryText = searchText.substring(0,1).toUpperCase() + searchText.substring(1);
                    firebaseSearch(queryText);
                }
            }
        });

    }


    private void firebaseSearch(String searchText) {

        Query firebaseSearchQueryBlog = mPostReference.orderByChild("title").startAt(searchText).endAt(searchText + "\uf8ff");
        Query admins = mPostReference.orderByChild("creator").equalTo(user.getUid());


        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class,
                R.layout.search_list_layout,
                BlogViewHolder.class,
                firebaseSearchQueryBlog


        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder blogViewHolder, Blog blog, int i) {
                blogViewHolder.setCategoryBlog(blog.getCateg());
                blogViewHolder.setBlog(blog);
                blogViewHolder.setBlogTitle(blog.getTitle());
                blogViewHolder.setImgBlog(getApplicationContext(), blog.getimageUrl());
            }
        };

        mResultList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View mPostView;
        Blog blogAux;
        TextView blog_category;
        TextView blog_title;
        ImageView blog_image;

        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);

            mPostView = itemView;
        }

        public void setCategoryBlog(String category){
            blog_category = (TextView) mPostView.findViewById(R.id.category_post);
            blog_category.setText(category);
            blog_category.setOnClickListener(this);
        }

        public void setBlogTitle(String category){
            blog_title = (TextView) mPostView.findViewById(R.id.title_post);
            blog_title.setText(category);
            blog_title.setOnClickListener(this);
        }

        public void setImgBlog(Context ctx, String imgae){
            blog_image= (ImageView) mPostView.findViewById(R.id.icon_result);
            Picasso.with(ctx).load(imgae).into(blog_image);
            blog_image.setOnClickListener(this);
        }


        public void setBlog(Blog blog){
            blogAux= blog;
        }


        @Override
        public void onClick(View v) {
            Toast.makeText(MyApplication.getAppContext(), "Checkout this Blog!", Toast.LENGTH_SHORT).show();
            Intent BlogPage = new Intent(MyApplication.getAppContext(), BlogPageActivity.class);
            BlogPage.putExtra("title",blogAux.getTitle());
            BlogPage.putExtra("console", blogAux.getConso());
            BlogPage.putExtra("descrip", blogAux.getDesc());
            BlogPage.putExtra("category", blogAux.getCateg());
            BlogPage.putExtra("imageurl", blogAux.getimageUrl());

            //ERROR NOVIEMBRE 21    Calling startActivity() from outside of an Activity
            BlogPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //End

            MyApplication.getAppContext().startActivity(BlogPage);
        }
    }
}
