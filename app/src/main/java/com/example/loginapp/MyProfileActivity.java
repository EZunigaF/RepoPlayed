package com.example.loginapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.loginapp.Adapters.MyApplication;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyProfileActivity extends AppCompatActivity {
    private RecyclerView mResultListOwner;
    private DatabaseReference mPostReference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFireS;
    private TextView textView;
    private TextView textView2;
    private DatabaseReference mDataBase;
    private Button imageButtonAdd;
    private DatabaseReference mDataBaseDataUser;
    static int REQUESCODE = 1 ;
    static int PReqCode = 1 ;
    Uri pickedImgUri ;
    private EditText editTextUser;
    ImageView ImgUserPhoto;
    DatabaseReference databaseReference2;
    DatabaseReference databaseReference3;

    private TextView mTextViewData;
    private String TAG="MyProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editv2);
        editTextUser = (EditText) findViewById(R.id.edit_text_name);
        mPostReference = FirebaseDatabase.getInstance().getReference("Blog");

        mResultListOwner = (RecyclerView) findViewById(R.id.result_post);

        ImgUserPhoto = findViewById(R.id.regUserPhoto);
        TextView mTextViewData = (TextView) findViewById(R.id.text_password3);
        TextView mTextViewData2 = (TextView) findViewById(R.id.text_password2);
        imageButtonAdd = (Button) findViewById(R.id.button_save);

        mDataBaseDataUser = FirebaseDatabase.getInstance().getReference();

        imageButtonAdd.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                             updateUserInfo();
                                              }
                                          });
                mDataBaseDataUser.child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });






        mAuth=FirebaseAuth.getInstance();

        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.child("Users").addValueEventListener(new ValueEventListener() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    String id = mAuth.getCurrentUser().getEmail();

                    String i2d = mAuth.getCurrentUser().getDisplayName();

                    databaseReference2 = FirebaseDatabase.getInstance().getReference();
                    databaseReference3 = FirebaseDatabase.getInstance().getReference().child("Users");
                    mTextViewData.setText(id);
                    mTextViewData2.setText(i2d);

                }
                else {
                    mTextViewData.setText("Hola Invitado");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ImgUserPhoto = findViewById(R.id.image_view) ;

        ImgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 22) {

                    checkAndRequestForPermission();


                }
                else
                {
                    openGallery();
                }





            }
        });





    }

    private void firebaseSearch() {

        //Query firebaseSearchQueryBlog = mPostReference.orderByChild("title").startAt(searchText).endAt(searchText + "\uf8ff");
        Query admins = mPostReference.orderByChild("creator").equalTo(user.getUid());

        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class,
                R.layout.profile_owner_blogs,
                BlogViewHolder.class,
                admins


        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder blogViewHolder, Blog blog, int i) {

                //String listPostKey = getRef(i).getKey();
                blogViewHolder.setId(getRef(i).getKey());
                blogViewHolder.setCategoryBlog(blog.getCateg());
                blogViewHolder.setBlog(blog);
                blogViewHolder.setBlogTitle(blog.getTitle());
                blogViewHolder.setImgBlog(getApplicationContext(), blog.getimageUrl());

            }
        };

        mResultListOwner.setAdapter(firebaseRecyclerAdapter);






    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        View mPostView;
        public Blog blogAux;
        TextView blog_category;
        TextView blog_title;
        ImageView blog_image;
        String id;

        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);

            mPostView = itemView;

        }

        public void setCategoryBlog(String category) {
            blog_category = (TextView) mPostView.findViewById(R.id.category_post);
            blog_category.setText(category);
            blog_category.setOnClickListener(this);
        }

        public void setBlogTitle(String category) {
            blog_title = (TextView) mPostView.findViewById(R.id.title_post);
            blog_title.setText(category);
            blog_title.setOnClickListener(this);
        }

        public void setImgBlog(Context ctx, String image) {
            blog_image = (ImageView) mPostView.findViewById(R.id.icon_result);
            Glide.with(MyApplication.getAppContext()).load(image).into(blog_image);
            blog_image.setOnClickListener(this);
        }


        public void setBlog(Blog blog) {
            blogAux = blog;
        }

        public void setId(String id){
            this.id = id;
        }

        public String getBId(){
            return this.id;
        }

        @Override

        public void onClick(View v) {
            int position = getAdapterPosition();
            String id = getBId();
            Intent BlogPage = new Intent(MyApplication.getAppContext(), EditGame.class);
            BlogPage.putExtra("title",blogAux.getTitle());
            //BlogPage.putExtra("console", blogAux.getConso());
            BlogPage.putExtra("descrip", blogAux.getDesc());
            //BlogPage.putExtra("category", blogAux.getCateg());
            BlogPage.putExtra("imageurl", blogAux.getimageUrl());
            BlogPage.putExtra("vbid", getBId());

            //ERROR NOVIEMBRE 21    Calling startActivity() from outside of an Activity
            BlogPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //End

            MyApplication.getAppContext().startActivity(BlogPage);
        }

        }
        public void updateUserInfo(){
            String update=editTextUser.getText().toString();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(update)
                    .setPhotoUri((pickedImgUri))
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                updateUI();
                                updateUI2();
                                Log.d(TAG, "User profile updated.");
                            }
                        }
                    });
        }

    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }
    private void checkAndRequestForPermission() {


        if (ContextCompat.checkSelfPermission(MyProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MyProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(MyProfileActivity.this,"Please accept for required permission",Toast.LENGTH_SHORT).show();

            }

            else
            {
                ActivityCompat.requestPermissions(MyProfileActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        }
        else

            openGallery();


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData() ;
            ImgUserPhoto.setImageURI(pickedImgUri);


        }


    }
    private void updateUI() {

        Intent homeActivity = new Intent(getApplicationContext(), MyProfileActivity.class);
        startActivity(homeActivity);
        finish();


    }
    private void updateUI2() {

        Intent homeActivity = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(homeActivity);
        finish();


    }

}




