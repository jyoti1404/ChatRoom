package com.example.chatroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ChatRoom extends AppCompatActivity {

    TextView name;
    EditText message;
    ImageButton send;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
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

        sharedPreferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        senderId = sharedPreferences.getString("id", null);
       // n = sharedPreferences.getString("username", null);

        intent = getIntent();
        n = intent.getStringExtra("user");
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
                MessageAdapter adapter = new MessageAdapter(arrayList, ChatRoom.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChatRoom.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void setSupportActionBar(Toolbar supportActionBar) {
        this.toolbar = supportActionBar;
    }
}


