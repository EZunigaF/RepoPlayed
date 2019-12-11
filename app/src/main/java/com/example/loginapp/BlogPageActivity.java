package com.example.loginapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.database.DatabaseReference;
import com.koushikdutta.ion.Ion;


public class BlogPageActivity extends AppCompatActivity {

    TextView BlogPageTitle;
    TextView BlogPageDescription;
    TextView BlogPageCategory;
    TextView BlogPageConsole;
    String BlogPageImageURL;
    ImageView BlogPageImage;
    ImageButton goBack;
    private String currentUser;
    DatabaseReference BLOG;
    DatabaseReference creator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blogpage);

        BlogPageImage = findViewById(R.id.blogPage_ImageBlog);
        BlogPageTitle = findViewById(R.id.post_detail_title);
        BlogPageDescription = findViewById(R.id.post_detail_descrip);
        BlogPageCategory = findViewById(R.id.post_detail_category);
        BlogPageConsole = findViewById(R.id.post_detail_console);
        goBack = findViewById(R.id.button_goBack);

        Intent BlogPage = getIntent();
        if (BlogPage != null){
            BlogPageTitle.setText(BlogPage.getStringExtra("title"));
            BlogPageDescription.setText(BlogPage.getStringExtra("descrip"));
            BlogPageCategory.setText(BlogPage.getStringExtra("category"));
            BlogPageConsole.setText(BlogPage.getStringExtra("console"));
            BlogPageImageURL = (BlogPage.getStringExtra("imageurl"));
            Ion.with(BlogPageImage)
                    .centerCrop(

                    )
                    .load(BlogPage.getStringExtra("imageurl"));
        }

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void givingAdmin(String currentUser){
        String creatorBlog;
        Intent BlogPage = getIntent();
        if(BlogPage != null){
            creatorBlog = BlogPage.getStringExtra("creator");
            Toast.makeText(this, "THE ADMIN IS>>>>>"+creatorBlog, Toast.LENGTH_SHORT).show();
        }
    }
}
