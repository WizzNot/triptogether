package com.example.project.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class carsAdapter extends RecyclerView.Adapter<carsAdapter.ExampleViewHolder> {   //Адаптер прокручивающегося списка объявлений пользователя
    private List<String> mExampleList;               //Массив объявлений
    private SelectListenerString listener;            //Слушатель кликов
    private Activity activity;                         //Активность

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {  //Контейнеры для информации
        public TextView mTextView;
        public CardView cardView;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.main);
            mTextView = itemView.findViewById(R.id.carsName);
        }
    }

    //Конструктор адаптера
    public carsAdapter(List<String> exampleList, SelectListenerString listener, Activity activity) {
        mExampleList = exampleList;
        this.listener = listener;
        this.activity = activity;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, @SuppressLint("RecyclerView") int position) {
        thread thread = new thread(activity, mExampleList, position, holder);
        thread.start();

        //Слушатель кликов
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(mExampleList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    //Поиск по названию
    public void filteredList(List<String> filteredList) {
        mExampleList = filteredList;
        notifyDataSetChanged();
    }
}

//Загрузка информации в отдельном потоке
class thread extends Thread{

    Activity activity;
    List<String> mExampleList;
    int position;
    carsAdapter.ExampleViewHolder holder;

    public thread(Activity activity, List<String> mExampleList, int position, carsAdapter.ExampleViewHolder holder) {
        this.activity = activity;
        this.mExampleList = mExampleList;
        this.position = position;
        this.holder = holder;
    }

    @Override
    public void run(){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String currentItem = mExampleList.get(position);
                holder.mTextView.setText(currentItem);
            }
        });
    }
}