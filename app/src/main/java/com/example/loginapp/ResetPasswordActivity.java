package com.example.loginapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ResetPasswordActivity";
    private Button button_reset_password;
    private EditText getEmailField;
    private String text_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        button_reset_password = findViewById(R.id.button_reset_password);
        getEmailField = findViewById(R.id.text_email);

        button_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetpass();
            }
        });
    }

    public void forgetpass(){
        text_email = getEmailField.getText().toString().trim();
        FirebaseAuth.getInstance().sendPasswordResetEmail(text_email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            updateUI();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_reset_password:

                break;

    }


}
    private void updateUI() {
        Toast.makeText(getApplicationContext(),"Revise su correo electronico",Toast.LENGTH_LONG).show();
        Intent homeActivity = new Intent(getApplicationContext(), ResetPasswordActivity.class);
        startActivity(homeActivity);
        finish();


    }
}
