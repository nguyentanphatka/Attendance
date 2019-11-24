package com.project.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText userName, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button)findViewById(R.id.btn_login);
        userName = (EditText)findViewById(R.id.editText);
        password = (EditText)findViewById(R.id.editText2);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userName.getText().toString().equals("teacher") &&
                        password.getText().toString().equals("teacher"))
                {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
                else if (userName.getText().toString().equals("student") &&
                        password.getText().toString().equals("student"))
                {
                    startActivity(new Intent(LoginActivity.this, StudentActivity.class));
                }
            }
        });
    }
}
