package com.example.chatroom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.chatroom.Notification.Token;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChatRoom extends AppCompatActivity {

    TextView name;
    EditText message;
    FloatingActionButton send;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    ArrayList<MessagePojo> arrayList = new ArrayList<>();
    ArrayList<UserPojo> user = new ArrayList<>();
    Toolbar toolbar;
    SharedPreferences sharedPreferences;
    String senderId, receiverId,n;
    Intent intent;
    String RECEIVER_DOCTOR_ID = "123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = findViewById(R.id.toolbar_name);
        message = findViewById(R.id.editTextMessage);
        send = findViewById(R.id.image_send);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("messageinfo");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        sharedPreferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        senderId = sharedPreferences.getString("id", null);
       // n = sharedPreferences.getString("username", null);

        intent = getIntent();
        n = intent.getStringExtra("name");
        receiverId = getIntent().getStringExtra("passCode");

//        Intent intent = getIntent();
//        //   name = intent.getStringExtra("name");
//        passcode = intent.getStringExtra("passCode");
//        receiverId = intent.getStringExtra("id");
//        Log.d ("12345", "onCreate: "+receiverId);

        name.setText(n);

        getFirebaseData();

//        ValueEventListener eventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot ds : dataSnapshot.getChildren()) {
//                    MessagePojo messagePojo = ds.getValue(MessagePojo.class);
//
//                    if (( senderId.equals(messagePojo.getSenderId()) && receiverId.equals(messagePojo.getReceiverId())) || (senderId.equals(messagePojo.getReceiverId()) && receiverId.equals(messagePojo.getSenderId()))) {
//                        arrayList.add(messagePojo);
//                    }
//                }
//                MessageAdapter adapter = new MessageAdapter(arrayList, ChatRoom.this);
//                recyclerView.setAdapter(adapter);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(ChatRoom.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        };
//
//        databaseReference.addListenerForSingleValueEvent(eventListener);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = message.getText().toString();
                Calendar calendar = Calendar.getInstance();
                int hr = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);
                String time = hr + ":" +min;

                MessagePojo messagePojo = new MessagePojo();
                messagePojo.setMessage(msg);
                messagePojo.setReceiverId(receiverId);
                messagePojo.setSenderId(senderId);
                messagePojo.setTime(time);
                databaseReference.push().setValue(messagePojo);
                message.setText("");
            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());

    }
    private void getFirebaseData(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    MessagePojo messagePojo = snapshot.getValue(MessagePojo.class);
                    arrayList.add(messagePojo);

//                    if ((senderId.equals(messagePojo.getSenderId()) && receiverId.equals(messagePojo.getReceiverId())) || (senderId.equals(messagePojo.getReceiverId()) && receiverId.equals(messagePojo.getSenderId()))) {
//                        arrayList.add(messagePojo);
//                    }
                }

                MessageAdapter adapter = new MessageAdapter(arrayList, ChatRoom.this, receiverId);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChatRoom.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void updateToken(String token){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        databaseReference.child(firebaseUser.getUid()).setValue(token1);
    }

    public void setSupportActionBar(Toolbar supportActionBar) {
        this.toolbar = supportActionBar;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_sign_out:
//                SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCE",MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.clear();
//                editor.commit();
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Log Out", Toast.LENGTH_SHORT).show();
                Intent intent1 =new Intent(ChatRoom.this,MainActivity.class);
                startActivity(intent1);
                finish();
                break;
        }
        return true;
    }
}


