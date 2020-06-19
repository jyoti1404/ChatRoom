package com.example.chatroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {

    EditText email, password, username;
    Button login;
    boolean flag=false;
    FirebaseAuth auth;
    SharedPreferences sharedPreferences;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());

        sharedPreferences = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE);

        boolean loginstatus = sharedPreferences.getBoolean("LoginStatus",false);
        if (loginstatus == true){

            Intent intent = new Intent(login.this,Dashboard.class);
            startActivity(intent);
            finish();
        }

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                final String mail = email.getText().toString();
                final String pa = password.getText().toString();

                if (TextUtils.isEmpty(mail) || TextUtils.isEmpty(pa)) {
                    Toast.makeText(login.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(mail, pa)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        databaseReference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                                    UserPojo userPojo = dataSnapshot1.getValue(UserPojo.class);

                                                    String femail = userPojo.getEmail();
                                                    String fpassword = userPojo.getPassword();

                                                    if ((mail.equals(femail)) && (pa.equals(fpassword))) {

                                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                                        editor.putString("name", userPojo.getUserName());
                                                        editor.putString("id", userPojo.getId());
                                                        editor.putString("email", userPojo.getEmail());
                                                        editor.putString("password", userPojo.getPassword());
                                                        editor.putBoolean("LoginStatus", true);
                                                        editor.commit();
                                                        flag = true;
                                                        break;
                                                    }
                                                }
                                                if (flag == true) {
                                                    Intent intent = new Intent(login.this, Dashboard.class);
                                                    intent.putExtra("password", pa);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    password.setText("");
                                                    Toast.makeText(login.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                Toast.makeText(login.this, "Database Error", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            });
                }
            }
        });
    }
}
