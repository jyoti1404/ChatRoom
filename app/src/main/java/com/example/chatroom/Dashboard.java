package com.example.chatroom;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class Dashboard extends AppCompatActivity {

    Button join_chat;
    Toolbar toolbar;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    ArrayList<UserPojo> arrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("DashBoard");

        join_chat = findViewById(R.id.button_join_chat);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());

        SharedPreferences sharedPreferences = getSharedPreferences("myprf", MODE_PRIVATE);
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
                       // DatabaseReference pass = databaseReference.child("password");

                        //if (pass.equals(text_passCode)) {

                            UserPojo userPojo1 = new UserPojo();
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
                            intent.putExtra("passCode", userPojo1.getPassCode());
                            intent.putExtra("name", userPojo1.getUserName());
                            startActivity(intent);
                            finish();

                        }

//


//                                }


                            });


            }
        });
    }

    private void setSupportActionBar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_sign_out:
                SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCE",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(this, "Log Out", Toast.LENGTH_SHORT).show();
                Intent intent1 =new Intent(Dashboard.this,MainActivity.class);
                startActivity(intent1);
                break;
        }
        return true;
    }
}
