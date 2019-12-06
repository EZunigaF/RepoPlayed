package com.example.loginapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class GamesActivity extends AppCompatActivity {

    ImageButton imageButtonAdd;
    FloatingActionButton buttonUp;
    Spinner spinner1, spinnerC;
    private Uri pickedImgUri = null;
    private static final int Gallery_request = 1;
    static int PReqCode = 1;
    static int REQUESCODE = 1;

    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    private EditText title;
    private EditText desc;
    private EditText categ;
    private Boolean imageUploaded;

    Button mSubmit;

    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");

        setContentView(R.layout.activity_games);
        imageButtonAdd = (ImageButton) findViewById(R.id.imageSelectedBlog);
        buttonUp = (FloatingActionButton) findViewById(R.id.btn_up);

        title = (EditText) findViewById(R.id.DescTitle);
        desc = (EditText) findViewById(R.id.DescFild);
        categ = (EditText) findViewById(R.id.DescFild);

        imageUploaded = false;


        mProgress = new ProgressDialog(this);
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Estrategia");
        spinnerArray.add("Autos");
        spinnerArray.add("Mundo Libre");
        spinnerArray.add("Deportes");
        spinnerArray.add("Plataformas");
        spinnerArray.add("Peleas");
        spinnerArray.add("Online");
        spinnerArray.add("Aventura");
        spinnerArray.add("MMO");
        spinnerArray.add("MMO-RPG");
        spinnerArray.add("MOBA");
        spinnerArray.add("Shooter");
        spinnerArray.add("Simulador");
        spinnerArray.add("Party games");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setAdapter(adapter);


        List<String> spinnerArray2 = new ArrayList<String>();
        spinnerArray2.add("PC");
        spinnerArray2.add("PS4");
        spinnerArray2.add("Xbox 360");
        spinnerArray2.add("Switch");
        spinnerArray2.add("PS3");
        spinnerArray2.add("PS2");
        spinnerArray2.add("PS1");
        spinnerArray2.add("Xbox");
        spinnerArray2.add("Wii u");
        spinnerArray2.add("Wii");
        spinnerArray2.add("GameCube");
        spinnerArray2.add("N64");
        spinnerArray2.add("Super Nintendo");
        spinnerArray2.add("Nintendo");
        spinnerArray2.add("Moviles");
        spinnerArray2.add("Consolas portatiles");


        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerArray2);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerC = (Spinner) findViewById(R.id.spinner2);
        spinnerC.setAdapter(adapter2);


        imageButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkAndRequestForPermission();

            }
        });

        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Log.println(Log.ERROR,"mas ","malo ");
                if (imageUploaded) {
                    starPosting();
                }
            }
        });
    }

    private void starPosting() {

        mProgress.setMessage("Posting...");
        mProgress.show();

        final String title_val = title.getText().toString().trim();
        final String desc_val = desc.getText().toString().trim();
        final String catg_val = spinner1.getSelectedItem().toString();
        final String conso_val = spinnerC.getSelectedItem().toString();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String creator = user.getUid();


        if (title == null && desc == null && pickedImgUri == null && spinner1 == null && spinnerC == null) {
            if (title.equals("") || desc.equals("") || pickedImgUri.equals("") || spinner1.equals("") || spinnerC.equals("")) {
                Log.println(Log.ERROR, "Error", "La informacion esta vacia");
                Toast.makeText(this, "INFO VACIA", Toast.LENGTH_SHORT).show();
            }
        } else {
            StorageReference filepath = mStorage.child("Blog_img").child(pickedImgUri.getLastPathSegment());
            filepath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    Uri downloadUrl = urlTask.getResult();
                    mProgress.dismiss();
                    DatabaseReference newPost = mDatabase.push();
                    newPost.child("title").setValue(title_val);
                    newPost.child("Desc").setValue(desc_val);
                    newPost.child("categ").setValue(catg_val);
                    newPost.child("conso").setValue(conso_val);
                    newPost.child("imageUrl").setValue(downloadUrl.toString());
                    newPost.child("creator").setValue(creator);
                    imageUploaded = false;
                    startActivity(new Intent(GamesActivity.this, HomeActivity.class));
                    finish();

                }
            });
        }
    }

    private void checkAndRequestForPermission() {

        Toast.makeText(this, "Opened the gallery", Toast.LENGTH_SHORT).show();

        if (ContextCompat.checkSelfPermission(GamesActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(GamesActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(GamesActivity.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(GamesActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);


            } else {
                ActivityCompat.requestPermissions(GamesActivity.this,
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

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData();
            imageButtonAdd.setImageURI(pickedImgUri);
            imageUploaded = true;


        }
    }

    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);


    }


}
