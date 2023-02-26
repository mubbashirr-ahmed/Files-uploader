package com.example.intern;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class C_login extends AppCompatActivity {

    EditText uname, password;
    Button loginbtn;
    TextView forgotbtn, registerbtn;
    ImageView back;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uname = findViewById(R.id.loguname);
        password = findViewById(R.id.logpass);
        loginbtn = findViewById(R.id.login);
        forgotbtn = findViewById(R.id.forgotPass);
        back = findViewById(R.id.ubackbtn);
        registerbtn = findViewById(R.id.regist);
        fAuth = FirebaseAuth.getInstance();

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = uname.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    uname.setError("username is empty");
                    uname.requestFocus();
                    return;
                }


                if (TextUtils.isEmpty(pass)) {
                    password.setError("Password is empty");
                    password.requestFocus();
                    return;
                }

                if (pass.length() < 8) {
                    password.setError("Length of password must be more than 8");
                    password.requestFocus();
                    return;
                }

                fAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                             Toast.makeText(C_login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                             startActivity(new Intent(getApplicationContext(), MyDocuments.class));


                        }else {
                            Toast.makeText(C_login.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }



        });

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), C_login.class));

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), MyDocuments.class));

            }
        });

    }

}