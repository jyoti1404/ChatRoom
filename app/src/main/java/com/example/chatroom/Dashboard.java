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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Dashboard extends AppCompatActivity {

    Button join_chat;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    ArrayList<UserPojo> arrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        join_chat = findViewById(R.id.button_join_chat);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());

        SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", null);
        String email = sharedPreferences.getString("email", null);
        final String password = sharedPreferences.getString("password", null);


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
                final EditText name = alertDialog.findViewById(R.id.edit_chat_name);
                Button ok = alertDialog.findViewById(R.id.button_ok);
                Button cancel = alertDialog.findViewById(R.id.button_cancel);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final String text_passCode = passcode.getText().toString();
                        final String id = databaseReference.push().getKey();
                        String text_name = name.getText().toString();
//                        final String pass = passcode.getText().toString();


//                        databaseReference.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
//                                    UserPojo userPojo = snapshot.getValue(UserPojo.class);
//                                    String name = userPojo.getUserName();
//                                    String id = userPojo.getId();
//                                    arrayList.add(userPojo);

//                                    if (!userPojo.getEmail().equals(getIntent().getStringExtra("email"))) {
//                                        arrayList.add(userPojo);
//                                    }
//                                UserPojo userPojo1 = new UserPojo();
                                        UserPojo userPojo1 = new UserPojo();

//                                        if (password.equals(text_passCode)){

                                        userPojo1.setId(id);
                                        userPojo1.setPassCode(text_passCode);
                                        userPojo1.setUserName(text_name);
                                        databaseReference.child(id).child(text_passCode).setValue(userPojo1);

                                        SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE);

                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.clear();
                                        editor.putString("id", userPojo1.getId());
                                        editor.putString("username", userPojo1.getUserName());
                                        //  editor.putString("passCode", userPojo.getPassCode());
                                        editor.commit();

                                        Intent intent = new Intent(Dashboard.this, ChatRoom.class);
                                        intent.putExtra("passCode", text_passCode);
                                        intent.putExtra("name", text_name);
                                        startActivity(intent);
                                        finish();

                                    }
//                                }
//                                }


                            });


            }
        });
    }
}
