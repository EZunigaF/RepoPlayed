package com.example.loginapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.loginapp.utils.AppPreferences;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView mTextViewData;
    private DatabaseReference mDataBase;
    private DatabaseReference mDataBase2;
    private RecyclerView mBlogList;
    FirebaseUser firebaseUser;
    ImageView imgUser;
    DatabaseReference databaseReference2;
    private DatabaseReference databaseReference3;
    private FirebaseAuth firebaseAuth;
    private  FirebaseDatabase firebaseDatabase;
    private ImageView imgUserPost;

    //ImageButtons Top menu
    private ImageButton logoutTopBar;
    private ImageButton postTopBar;
    private ImageButton searchTopBar;
    //End Top menu buttons


    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mBlogList = (RecyclerView) findViewById(R.id.blog_list);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        imgUser = findViewById(R.id.imagePerf2);


        TextView textView = (TextView) findViewById(R.id.idMyname);
        TextView textView2 = (TextView) findViewById(R.id.idMemberD);


        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Gameplay.ttf");
        textView.setTypeface(typeface);


        textView2.setTypeface(typeface);



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        mAuth=FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        mTextViewData = (TextView) findViewById(R.id.idMyname);

        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase2 = FirebaseDatabase.getInstance().getReference().child("Blog");

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference photoReference= storageReference.child("users_photo/");


        ///TOP MENU IMAGE BUTTONS (LOG OUT / ADD TIPS / SEARCH TIPS)
        logoutTopBar = findViewById(R.id.logoutTopBar_Home);
        postTopBar = findViewById(R.id.postTopBar_Home);
        searchTopBar = findViewById(R.id.searchTopBar_Home);

        //Add Clicks

        checkUserStatus();
        topBarListeners();

        mDataBase.child("Users").addValueEventListener(new ValueEventListener() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    String id = mAuth.getCurrentUser().getEmail();
                    Uri i2d = mAuth.getCurrentUser().getPhotoUrl();

                    databaseReference2 = FirebaseDatabase.getInstance().getReference();
                    databaseReference3 = FirebaseDatabase.getInstance().getReference().child("Blog");
                    mTextViewData.setText(id);

                }
                else {
                    mTextViewData.setText("Hola Invitado");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void checkUserStatus(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            Uri photoUrl = user.getPhotoUrl();
            Glide.with(this).load(photoUrl).into(imgUser);
            String uid = user.getUid();
            Toast.makeText(this, "METODO CHECK User CORRECTO", Toast.LENGTH_SHORT).show();
            //Picasso.with(context).load(photoUrl).into(img);
        } else {
            //Usuario no conectado, debe ser redirigido hacia el inicio
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class,
                R.layout.blog_row,
                BlogViewHolder.class,
                mDataBase2


        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder blogViewHolder, Blog blog, int i) {
                blogViewHolder.setTitle(blog.getTitle());
                blogViewHolder.setDesc(blog.getDesc());
                blogViewHolder.setImg(getApplicationContext(), blog.getimageUrl());
            }
        };
        mBlogList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }


        public  void setTitle(String title){
            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(title);


        }
        public void setDesc(String Desc){
            TextView post_desc = (TextView) mView.findViewById(R.id.post_text);
            post_desc.setText(Desc);
        }
        public void setImg(Context ctx, String imgae){
            ImageView    post_image= (ImageView) mView.findViewById(R.id.post_img);
            Picasso.with(ctx).load(imgae).into(post_image);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void topBarListeners(){
        logoutTopBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Doing a button", Toast.LENGTH_SHORT).show();
            }
        });

        searchTopBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SearchActivity.class));
            }
        });

        postTopBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, GamesActivity.class));
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnSingOut:
                AppPreferences.getInstance(this).clear();
                /*
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;*/
                mAuth.signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();

            default:
                return super.onOptionsItemSelected(item);

            case R.id.addPost:
                startActivity(new Intent(HomeActivity.this, GamesActivity.class));
                return super.onOptionsItemSelected(item);

            case R.id.messages:
                startActivity(new Intent(HomeActivity.this, SearchActivity.class));
                return super.onOptionsItemSelected(item);
        }

    }


}
