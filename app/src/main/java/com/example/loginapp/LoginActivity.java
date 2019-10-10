package com.example.loginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginapp.utils.AppPreferences;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText txtEmail;
    private EditText txtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtEmail = findViewById(R.id.editText);
        txtPassword = findViewById(R.id.editText2);
        Button btnLogin = findViewById(R.id.button);

        btnLogin.setOnClickListener(this);
        TextView txtGoToRegister = findViewById(R.id.go_to_register);
        txtGoToRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                makeLogin();
                break;

            case R.id.go_to_register:
                Intent intent= new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;

                //Toast.makeText(this, "Has hecho login", Toast.LENGTH_LONG).show();
                //break;
                }
        }

        private void makeLogin(){
            String email =txtEmail.getText().toString();
            String pass =txtPassword.getText().toString();

            if(email.isEmpty()){
                txtEmail.setError("El campo está vacio");
                return;
            }

            if(pass.isEmpty()){
                txtPassword.setError("El campo está vacio");
                return;
            }

            if(email.equalsIgnoreCase("admin")
                && pass.equalsIgnoreCase("123123123")){

                AppPreferences.getInstance(this).put(AppPreferences.Keys.IS_LOGGED_IN, true);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();

              Toast.makeText(this,"Has hecho login", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"No coincidimos", Toast.LENGTH_SHORT).show();
            }

        }
}
