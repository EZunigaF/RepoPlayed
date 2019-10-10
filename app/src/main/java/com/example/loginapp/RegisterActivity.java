package com.example.loginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.loginapp.model.User;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{


    private TextInputEditText edName;
    private TextInputEditText edEmail;
    private TextInputEditText edPassword;
    private ArrayList<User> users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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

        String name = edName.getText().toString();
        String email = edEmail.getText().toString();
        String password = edPassword.getText().toString();

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





        //Verificar que el usuario no exista
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        //Almacenar todos los usuarios
        users.add(user);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
