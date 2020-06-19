package com.example.chatroom;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    Context context;
    ArrayList<MessagePojo> arrayList = new ArrayList<>();

    FirebaseUser fuser;

    public MessageAdapter(ArrayList<MessagePojo> arrayList, Context context){
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_TYPE_RIGHT) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View list_item = layoutInflater.inflate(R.layout.chat_item_right, parent, false);
            MessageAdapter.ViewHolder viewHolder = new MessageAdapter.ViewHolder(list_item);
            return viewHolder;
        }
        else {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View list_item = layoutInflater.inflate(R.layout.chat_item_left, parent, false);
            MessageAdapter.ViewHolder viewHolder = new MessageAdapter.ViewHolder(list_item);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        MessagePojo messagePojo = arrayList.get(position);
        holder.textViewMessage.setText(messagePojo.getMessage());
        holder.textViewTime.setText(messagePojo.getTime());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewMessage;
        TextView textViewTime;

        public ViewHolder(View list_item) {
            super(list_item);

            textViewTime = list_item.findViewById(R.id.textViewTime);
            textViewMessage = list_item.findViewById(R.id.textViewMessage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        if (arrayList.get(position).getSenderId().equals(fuser.getUid())){
            Log.d("123", arrayList.get(position).getSenderId());
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }
}
