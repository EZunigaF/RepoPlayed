package com.example.loginapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.loginapp.Adapters.MyApplication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;


public class BlogPageActivity extends AppCompatActivity {

    TextView BlogPageTitle;
    TextView BlogPageDescription;
    TextView BlogPageCategory;
    TextView BlogPageConsole;
    String BlogPageImageURL;
    ImageView BlogPageImage;
    ImageButton goBack;
    ImageButton goseguir;
    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;
    private TextView mTextViewData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextViewData = (TextView) findViewById(R.id.edit_text_name);
        setContentView(R.layout.activity_blogpage);
        mAuth=FirebaseAuth.getInstance();
        BlogPageImage = findViewById(R.id.blogPage_ImageBlog);
        BlogPageTitle = findViewById(R.id.post_detail_title);
        BlogPageDescription = findViewById(R.id.post_detail_descrip);
        BlogPageCategory = findViewById(R.id.post_detail_category);
        BlogPageConsole = findViewById(R.id.post_detail_console);
        goBack = findViewById(R.id.button_goBack);




        mAuth = FirebaseAuth.getInstance();

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
}
