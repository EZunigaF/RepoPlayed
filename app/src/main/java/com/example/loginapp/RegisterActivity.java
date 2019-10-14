package com.example.loginapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.loginapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{


    private TextInputEditText edName;
    private TextInputEditText edEmail;
    private TextInputEditText edPassword;
    private ArrayList<User> users;

    //Datos a entrar
    private String name= "";
    private String email= "";
    private String password= "";

    DatabaseReference databaseReference;
    //
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //animacion
        final LottieAnimationView animationView = (LottieAnimationView) findViewById (R.id.animation_view);
        animationView.setVisibility(View.INVISIBLE);
        animationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animationView.playAnimation();

            }
        });


        edName = findViewById(R.id.txtName);
        edEmail = findViewById(R.id.txtEmail);
        edPassword = findViewById(R.id.txtPassword);


        Button btnRegister = findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(this);


        TextView txtGotoLogin = findViewById(R.id.go_to_login);
        txtGotoLogin.setOnClickListener(this);
        users = new ArrayList<>();




    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){
            case R.id.btn_register:

                makeRegister();


                break;

            case R.id.go_to_login:
                Intent intent= new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void makeRegister(){
        Log.d("myTag", "Entra al registro...");

        final String name = edName.getText().toString();

        final String email = edEmail.getText().toString();

        final String password = edPassword.getText().toString();


        if(name.isEmpty()){
            edName.setError("Require correct information ");

            LottieAnimationView animationView=findViewById (R.id.animation_view);
            animationView.setVisibility(View.VISIBLE);
            animationView.playAnimation();
            return;

        }

        if(email.isEmpty()){
            edEmail.setError("Require correct information");
            LottieAnimationView animationView=findViewById (R.id.animation_view);
            animationView.setVisibility(View.VISIBLE);
            animationView.playAnimation();

            return;
        }

        if(password.isEmpty()){
            edPassword.setError("Require correct information");
            LottieAnimationView animationView=findViewById (R.id.animation_view);
            animationView.setVisibility(View.VISIBLE);
            animationView.playAnimation();

            return;
        }
        else
        {



            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>()

            {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {


                        Map<String, Object> map = new HashMap<>();

                        map.put("email", email);
                        map.put("password", password);
                        map.put("DisplayName", name);
                        map.put("name", name);



                        String id = mAuth.getCurrentUser().getUid();


                        databaseReference.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task2){
                                if (task2.isSuccessful()){


                                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                                    finish();

                                }
                                else {
                                    Toast.makeText(RegisterActivity.this,"Error 2", Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                    }
                    else{
                        Toast.makeText(RegisterActivity.this,"No es posible Registrar este usario: "+name, Toast.LENGTH_LONG).show();


                    }
                }
            });
        }


    }


}
