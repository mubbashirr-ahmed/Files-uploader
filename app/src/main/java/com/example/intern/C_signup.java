package com.example.intern;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class C_signup extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText uname,email,fullname,Phonenumber,password,cpassword;
    Button Regist;
    FirebaseAuth fAuth;
    ImageView back;
    FirebaseFirestore fstore;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csignup);

        uname = findViewById(R.id.loguname);
        fullname = findViewById(R.id.fname);
        email = findViewById(R.id.remail);
        Phonenumber = findViewById(R.id.phone);
        password = findViewById(R.id.logpass);
        cpassword = findViewById(R.id.conpassword);
        Regist = findViewById(R.id.Registbtn);
        back = findViewById(R.id.ubackbtn);
        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();


        Regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String Username = uname.getText().toString();
                String Fullname = fullname.getText().toString();
                String Phone = Phonenumber.getText().toString();
                String confirmpass = cpassword.getText().toString();



                if (TextUtils.isEmpty(Email)) {
                    email.setError("Email is empty");
                    email.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                    email.setError("Enter the valid email address");
                    email.requestFocus();
                    return;
                }

                if (Username.isEmpty()) {
                    uname.setError("username is empty");
                    uname.requestFocus();
                    return;
                }

                if (Fullname.isEmpty()) {
                    fullname.setError("Fullname is empty");
                    fullname.requestFocus();
                    return;
                }

                if (Email.isEmpty()) {
                    email.setError("Email is empty");
                    email.requestFocus();
                    return;
                }
                if (Phone.isEmpty()) {
                    Phonenumber.setError("Phone number is empty");
                    Phonenumber.requestFocus();
                    return;
                }

                if (pass.length() < 8) {
                    password.setError("Length of password is more than 8");
                    password.requestFocus();
                    return;
                }

                if (!password.getText().toString().equals(cpassword.getText().toString())){

                    Toast.makeText(C_signup.this,"Password Not matching",Toast.LENGTH_SHORT).show();
                }




                fAuth.createUserWithEmailAndPassword(Email,pass).addOnCompleteListener(task -> {
                     if (task.isSuccessful()) {
                         Toast.makeText(C_signup.this, "You are successfully Registered", Toast.LENGTH_SHORT).show();
                         userID = fAuth.getCurrentUser().getUid();
                         DocumentReference documentReference = fstore.collection("CompanyUsers").document(userID);
                         Map<String,Object> user = new HashMap<>();
                         user.put("Username",Username);
                         user.put("FullName",Fullname);
                         user.put("Email",Email);
                         user.put("PhoneNumber",Phone);
                         user.put("Password",pass);
                         documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void unused) {
                                 Log.d(TAG, "onSuccess: user profile is created for"+ userID);
                             }
                         });
                         startActivity(new Intent(getApplicationContext(), C_login.class));

                     } else {
                         Toast.makeText(C_signup.this, "You are not Registered! Try again", Toast.LENGTH_SHORT).show();
                      }

                });



                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(getApplicationContext(), MyDocuments.class));

                    }
                });

            }

        });
    };

}
