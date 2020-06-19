package com.example.chatroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class register extends AppCompatActivity {

    EditText userName, mail, pass;
    Button register;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    String no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = findViewById(R.id.register_username);
        mail = findViewById(R.id.register_email);
        pass = findViewById(R.id.register_password);
        register = findViewById(R.id.register_button);

        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text_user = userName.getText().toString();
                String text_email = mail.getText().toString();
                String text_pass = pass.getText().toString();
                if (TextUtils.isEmpty(text_user) || TextUtils.isEmpty(text_email) || TextUtils.isEmpty(text_pass)){
                    Toast.makeText(register.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else {
                    register(text_user, text_email, text_pass);
                }

            }

        });
    }

    private void register(final String username, final String email, final String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            try {
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                final String userId = firebaseUser.getUid();

                                databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);

//                            HashMap<String, String> hashMap = new HashMap<>();
//                            hashMap.put("id", userId);
//                            hashMap.put("name", username);
//                            hashMap.put("email", email);
//                            hashMap.put("password", password);

                                final UserPojo userPojo = new UserPojo();
                                userPojo.setUserName(userName.getText().toString().trim());
                                userPojo.setEmail(mail.getText().toString().trim());
                                userPojo.setPassword(pass.getText().toString().trim());

                                databaseReference.setValue(userPojo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Intent intent = new Intent(register.this, MainActivity.class);
                                            intent.putExtra("name", username);
                                            intent.putExtra("email", userPojo.getEmail());
                                            intent.putExtra("password", userPojo.getPassword());
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else {
                                            Toast.makeText(register.this, "Invalid info", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException existEmail)
                            {
                                Toast.makeText(register.this, "Email Already Exists", Toast.LENGTH_SHORT).show();

                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
    }

