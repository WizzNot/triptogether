package com.example.project.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class newAdapter extends RecyclerView.Adapter<recVh>{

    ArrayList<String> items;
    Context context;

    public newAdapter(ArrayList<String> items, Context context){
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public recVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_cars, parent, false);

        return new recVh(view, context).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull recVh holder, int position) {
        String string = items.get(position);
        holder.textView.setText(string);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

class recVh extends RecyclerView.ViewHolder{

    TextView textView;

    private newAdapter adapter;

    public recVh(@NonNull View itemView, Context context) {
        super(itemView);

        textView = itemView.findViewById(R.id.carsName);

        itemView.findViewById(R.id.close).setOnClickListener(view -> {
            adapter.items.remove(getAdapterPosition());
            adapter.notifyItemRemoved(getAdapterPosition());

            SharedPreferences sharedPreferences = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            Gson gson = new Gson();
            String s = gson.toJson(adapter.items);
            editor.putString("key", s);
            editor.commit();
        });
    }

    public recVh linkAdapter(newAdapter adapter){
        this.adapter = adapter;
        return this;
    }
}
