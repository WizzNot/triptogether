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
import com.example.project.main.SelectListener;

import java.util.List;

public class RecAdapterSuggests extends RecyclerView.Adapter<RecAdapterSuggests.ExampleViewHolder> {   //Адаптер прокручивающегося списка объявлений пользователя
    private List<Suggests> mExampleList;               //Массив объявлений
    private Activity activity;                         //Активность
    private SelectSuggest listener;


    public static class ExampleViewHolder extends RecyclerView.ViewHolder {  //Контейнеры для информации
        public TextView mTextView1;
        public TextView mTextView2;
        public CardView cardView;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.text1);
            mTextView2 = itemView.findViewById(R.id.text2);
            cardView = itemView.findViewById(R.id.main_cardview);
        }
    }

    //Конструктор адаптера
    public RecAdapterSuggests(List<Suggests> exampleList, Activity activity, SelectSuggest lis) {
        mExampleList = exampleList;
        this.activity = activity;
        this.listener = lis;
    }


    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_element_suggests, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, @SuppressLint("RecyclerView") int position) {
        threadRecSuggests thread = new threadRecSuggests(activity, mExampleList, position, holder);
        thread.start();
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
    public void filteredList(List<Suggests> filteredList) {
        mExampleList = filteredList;
        notifyDataSetChanged();
    }
}

//Загрузка информации в отдельном потоке
class threadRecSuggests extends Thread{

    Activity activity;
    List<Suggests> mExampleList;
    int position;
    RecAdapterSuggests.ExampleViewHolder holder;

    public threadRecSuggests(Activity activity, List<Suggests> mExampleList, int position, RecAdapterSuggests.ExampleViewHolder holder) {
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
                if (!mExampleList.isEmpty()){
                    Suggests currentItem = mExampleList.get(position);
                    holder.mTextView1.setText(currentItem.getGeoName());
                    holder.mTextView2.setText(currentItem.getAddress());
                }
            }
        });
    }
}
