package com.example.loginapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginapp.Adapters.MyApplication;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.koushikdutta.ion.Ion;

import java.util.Map;


public class EditGame extends AppCompatActivity {

    static int PReqCode = 1;
    static int REQUESCODE = 1;
    private Uri pickedImgUri = null;
    private Boolean imageUploaded;
    ImageButton imageButtonAdd;
    ImageView uploadedFrame;
    private StorageReference mStorage;
    String BlogImageURL;
    ImageView BlogPageImage;
    ImageButton goBack;
    EditText upTitle;
    EditText upDescrip;
    Button saveUpdate;
    Button cancelUpdate;
    ImageButton deleteBlog;
    private String theVBlogID;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgress;
    private Blog auxBlog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_game);


        goBack = findViewById(R.id.button_goBack);
        uploadedFrame = findViewById(R.id.imageUpdatedUpload);
        upTitle = findViewById(R.id.updatedTitleField);
        upDescrip = findViewById(R.id.updatedDescripField);
        cancelUpdate = findViewById(R.id.cancelUpdate);
        saveUpdate = findViewById(R.id.saveUpdated);
        deleteBlog = findViewById(R.id.deleteBlogAction);
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");




        buttonsListener();
        mProgress = new ProgressDialog(this);




//
//        Post post = new Post(userId, username, title, body);
//        Blog blog = new Blog();
//        public Blog(String title, String Desc, String imageUrl, String conso,String categ) {
//        Map<String, Object> postValues = post.toMap();

        Intent BlogPage = getIntent();
        if (BlogPage != null){
            upTitle.setText(BlogPage.getStringExtra("title"));
            upDescrip.setText(BlogPage.getStringExtra("descrip"));
            //BlogPageCategory.setText(BlogPage.getStringExtra("category"));
            //BlogPageConsole.setText(BlogPage.getStringExtra("console"));
            //BlogImageURL = (BlogPage.getStringExtra("imageurl"));
            theVBlogID = BlogPage.getStringExtra("vbid");
            BlogImageURL = BlogPage.getStringExtra("imageurl");
            Ion.with(uploadedFrame)
                    .centerCrop(

                    )
                    .load(BlogPage.getStringExtra("imageurl"));
        }
        //Toast.makeText(this, "infoIntentLoaded" + theVBlogID, Toast.LENGTH_SHORT).show();


    }
    private void getInfoBlog(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userCreator= user.getUid();

        final String title_val = upTitle.getText().toString().trim();
        final String desc_val = upDescrip.getText().toString().trim();
        final String imageUpload = BlogImageURL;
        final String creator = userCreator;
        auxBlog = new Blog(title_val,desc_val,imageUpload,"consoDefuault","categDefault",creator);

    }

    private void EmpackInfoUpdated(){

        getInfoBlog();
        mDatabase.child(theVBlogID).setValue(auxBlog);
        Toast.makeText(this, "Update complete!", Toast.LENGTH_SHORT).show();
        finish();


    }

    private void buttonsListener(){
        cancelUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mProgress.setMessage("Posting...");
                //mProgress.show();
                EmpackInfoUpdated();
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        uploadedFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestForPermission();
            }
        });

        deleteBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarDelete();
            }
        });
    }


    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(EditGame.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditGame.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(MyApplication.getAppContext(), "Please accept for required permission", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(EditGame.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);


            } else {
                ActivityCompat.requestPermissions(EditGame.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        } else
            openGallery();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {
            Toast.makeText(EditGame.this, "Wait until the image loads into screen", Toast.LENGTH_SHORT).show();
            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData();
            uploadedFrame.setImageURI(pickedImgUri);
            ImagesString();
            resetImagedUp();
            //imageUploaded = true;

        }
    }

    private void ImagesString(){
        StorageReference filepath = mStorage.child("Blog_img").child(pickedImgUri.getLastPathSegment());
        filepath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful()) ;
                Uri downloadUrl = urlTask.getResult();
                BlogImageURL = downloadUrl.toString();
                Ion.with(uploadedFrame)
                        .centerCrop(
                        )
                        .load(BlogImageURL);
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    private void resetImagedUp(){
        String aux= "";
        Ion.with(uploadedFrame)
                .fitCenter(
                )
                .load(aux);
    }

    public void confirmarDelete() {
        AlertDialog.Builder exit = new AlertDialog.Builder(this);
        exit.setMessage(getString(R.string.deleteBlogMessage))
                .setCancelable(true)
                .setPositiveButton("Yes, delete it",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDatabase.child(theVBlogID).removeValue();
                        finish();
                    }
                }).setNegativeButton("No, let me back", new DialogInterface.OnClickListener() {
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
