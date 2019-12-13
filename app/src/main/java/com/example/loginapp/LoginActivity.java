package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.CompoundButtonCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements TextWatcher, CompoundButton.OnCheckedChangeListener, View.OnClickListener{

    private EditText txtEmail;
    private EditText txtPassword;
    private ProgressDialog mProgress;
    //--------------------------------------------Preferencias-----------------------//
    private EditText etUsername, etPass;
    private CheckBox rem_userpass;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private TextView forgotPass;
    private static final String PREF_NAME = "prefs";
    private static final String KEY_REMEMBER = "remember";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASS = "password";
    //--------------------------------------------Preferencias-----------------------//
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth =FirebaseAuth.getInstance();

        txtEmail = findViewById(R.id.editText);
        txtPassword = findViewById(R.id.editText2);
        forgotPass = findViewById(R.id.go_to_Forget);
        Button btnLogin = findViewById(R.id.button);

        btnLogin.setOnClickListener(this);
        TextView txtGoToRegister = findViewById(R.id.go_to_register);
        txtGoToRegister.setOnClickListener(this);

        mProgress = new ProgressDialog(this);
        //-------------------------------------------------------------------------------------------
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        etUsername = (EditText)findViewById(R.id.editText);
        etPass = (EditText)findViewById(R.id.editText2);
        rem_userpass = (CheckBox)findViewById(R.id.checkBox);
        if(sharedPreferences.getBoolean(KEY_REMEMBER, false))
            rem_userpass.setChecked(true);
        else
            rem_userpass.setChecked(false);
        etUsername.setText(sharedPreferences.getString(KEY_USERNAME,""));
        etPass.setText(sharedPreferences.getString(KEY_PASS,""));
        etUsername.addTextChangedListener(this);
        etPass.addTextChangedListener(this);
        rem_userpass.setOnCheckedChangeListener(this);

        //-------------------------------------------------------------------------------------------

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
                finish();
            }
        });
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
            mProgress.setMessage("Making Login. Please wait a second...");
            mProgress.show();

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
            else{

                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            mProgress.dismiss();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                        }
                        else{
                            Toast.makeText(LoginActivity.this,"Error de datos...", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        managePrefs();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        managePrefs();
    }

    private void managePrefs(){
        if(rem_userpass.isChecked()){
            editor.putString(KEY_USERNAME, etUsername.getText().toString().trim());
            editor.putString(KEY_PASS, etPass.getText().toString().trim());
            editor.putBoolean(KEY_REMEMBER, true);
            editor.apply();
        }else{
            editor.putBoolean(KEY_REMEMBER, false);
            editor.remove(KEY_PASS);//editor.putString(KEY_PASS,"");
            editor.remove(KEY_USERNAME);//editor.putString(KEY_USERNAME, "");
            editor.apply();
        }
    }
}
