package com.example.loginapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.loginapp.Adapters.MyApplication;
import com.squareup.picasso.Picasso;


public class BlogPageActivity extends AppCompatActivity {

    TextView BlogPageTitle;
    TextView BlogPageDescription;
    TextView BlogPageCategory;
    TextView BlogPageConsole;
    String BlogPageImageURL;
    ImageView BlogPageImage;
    ImageButton goBack;



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
            Picasso.with(MyApplication.getAppContext())
                    .load(BlogPageImageURL)

                    .into(BlogPageImage);
        }

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }
}
