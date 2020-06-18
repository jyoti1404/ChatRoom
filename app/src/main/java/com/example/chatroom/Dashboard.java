package com.example.chatroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Dashboard extends AppCompatActivity {

    Button join_chat;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        join_chat = findViewById(R.id.button_join_chat);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());

        join_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog, viewGroup, false);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                final EditText passcode = alertDialog.findViewById(R.id.edit_chat_passcode);
                Button ok = alertDialog.findViewById(R.id.button_ok);
                Button cancel = alertDialog.findViewById(R.id.button_cancel);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String text_passCode = passcode.getText().toString();
                        final String id = databaseReference.push().getKey();
                        final String pass = passcode.getText().toString();

                        if (passcode.getText().toString().equals("123")) {

                            UserPojo userPojo = new UserPojo();
                            userPojo.setId(id);
                            databaseReference.child(id).setValue(userPojo);

                            SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.putString("id", userPojo.getId());
                            editor.putString("passCode", userPojo.getPassCode());;
                            editor.commit();

                                Intent intent = new Intent(Dashboard.this, ChatRoom.class);
                                intent.putExtra("passCode", text_passCode);
                                startActivity(intent);
                                finish();


                        }

                                }
                            });


            }
        });
    }
}
