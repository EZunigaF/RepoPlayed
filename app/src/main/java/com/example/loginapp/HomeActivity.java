package com.example.loginapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.loginapp.Adapters.MyApplication;
import com.example.loginapp.utils.AppPreferences;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.j256.ormlite.stmt.query.In;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    private TextView mTextViewData;
    private DatabaseReference mDataBase;
    private DatabaseReference mDataBase2;
    private RecyclerView mBlogList;
    private ImageButton loginOut;


    ImageView imgUser;
    DatabaseReference databaseReference2;

   private FirebaseFirestore mFireS;

    private DatabaseReference databaseReference3;

    private FirebaseAuth firebaseAuth;

    private  FirebaseDatabase firebaseDatabase;

    private ImageView imgUserPost;

    //ImageButtons Top menu
    private ImageButton VideoTopBar;
    private ImageButton postTopBar;
    private ImageButton searchTopBar;
    private String TAG="HomeActivity";
    //End Top menu buttons


    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mBlogList = (RecyclerView) findViewById(R.id.blog_list);

        loginOut = findViewById(R.id.btnShutOffTopBar_Home);

        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        mFireS = FirebaseFirestore.getInstance();
        FirebaseDatabase.getInstance().getReference();

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
        //-----------------------------------------Objeto de consulta----------------------------//
        Map<String, Object> data1 = new HashMap<>();
        data1.put("Users", mAuth.getCurrentUser().getEmail());
        //-----------------------------------------Objeto de consulta----------------------------//

        CollectionReference emails = mFireS.collection(mAuth.getCurrentUser().getEmail());
        //Fijarse en este sf
        emails.document("SF").set(data1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTextViewData = (TextView) findViewById(R.id.idMyname);
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase2 = FirebaseDatabase.getInstance().getReference().child("Blog");

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference = storageReference.child("users_photo/");


        ///TOP MENU IMAGE BUTTONS (LOG OUT / ADD TIPS / SEARCH TIPS)
        VideoTopBar = findViewById(R.id.VideoTopBar_Home);
        loginOut = findViewById(R.id.btnShutOffTopBar_Home);
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
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
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
                blogViewHolder.setBlog(blog);
                blogViewHolder.setTitle(blog.getTitle());
                blogViewHolder.setDesc(blog.getDesc());
                blogViewHolder.setConso(blog.getConso());
                blogViewHolder.setImg(getApplicationContext(), blog.getimageUrl());
            }
        };
        mBlogList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //Variables de los Blogs ubicados en el RecyclerView del HomeActivity (Titulo, Descripcion, Categoria y Imagen)
        View mView;
        TextView post_title;
        TextView post_desc,post_conso,post_categ;
        ImageView post_image;
        Blog blogAux;

        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setConso(String conso){
            post_conso = (TextView) mView.findViewById(R.id.category_post);
            post_conso.setText(conso);
            post_conso.setOnClickListener(this);
        }
        public void setCateg(String categ){
            post_categ = (TextView) mView.findViewById(R.id.post_text);
            post_categ.setText(categ);
        }


        public  void setTitle(String title){
            post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(title);
            post_title.setOnClickListener(this);

        }
        public void setDesc(String Desc){
            post_desc = (TextView) mView.findViewById(R.id.post_text);
            post_desc.setText(Desc);
            post_desc.setOnClickListener(this);
        }
        public void setImg(Context ctx, String imgae){
            post_image= (ImageView) mView.findViewById(R.id.post_img);
            Picasso.with(ctx).load(imgae).into(post_image);
            post_image.setOnClickListener(this);
        }

        public void setBlog(Blog blog){
            blogAux = blog;
        }

        @Override
        public void onClick(View v) {

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



    public void topBarListeners(){
        VideoTopBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, VideoActivity.class));

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

        loginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarExit();

            }
        });

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseUser.getPhotoUrl();
                Uri photoUrl = firebaseUser.getPhotoUrl();

                ////////////////////////////////
                String bioUser = mAuth.getCurrentUser().getEmail();
                String bioDisplay = mAuth.getCurrentUser().getDisplayName();
                String bioImage = "";

                    if (photoUrl == null){
                        bioImage="";
                    }
                    else{
                        bioImage = photoUrl.toString();
                    };
                Intent bioUserGames = new Intent(MyApplication.getAppContext(), MyBlogsActivity.class);
                bioUserGames.putExtra("user", bioUser);
                bioUserGames.putExtra("bioImg", bioImage);
                bioUserGames.putExtra("display", bioDisplay);
                bioUserGames.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApplication.getAppContext().startActivity(bioUserGames);
            }
        });

    }

    public void confirmarExit() {
        AlertDialog.Builder exit = new AlertDialog.Builder(this);
        exit.setMessage(getString(R.string.exitConfirm))
                .setCancelable(true)
                .setPositiveButton("Yeah",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        Intent intent = new Intent(MyApplication.getAppContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("No, return me", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog title = exit.create();
        title.setTitle("Waiting orders . .");
        title.show();

    }
}
