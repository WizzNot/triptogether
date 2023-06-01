package com.example.project.util;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

import java.util.List;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.ExampleViewHolder> {   //Адаптер прокручивающегося списка объявлений пользователя
    private List<Elements> mExampleList;               //Массив объявлений
    private Activity activity;                         //Активность

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {  //Контейнеры для информации
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView4;
        public TextView mTextView5;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.text1);
            mTextView2 = itemView.findViewById(R.id.text2);
            mTextView3 = itemView.findViewById(R.id.text3);
            mTextView4 = itemView.findViewById(R.id.text4);
            mTextView5 = itemView.findViewById(R.id.text5);
        }
    }

    //Конструктор адаптера
    public RecAdapter(List<Elements> exampleList, Activity activity) {
        mExampleList = exampleList;
        this.activity = activity;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_element_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, @SuppressLint("RecyclerView") int position) {
        threadRec thread = new threadRec(activity, mExampleList, position, holder);
        thread.start();
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    //Поиск по названию
    public void filteredList(List<Elements> filteredList) {
        mExampleList = filteredList;
        notifyDataSetChanged();
    }
}

//Загрузка информации в отдельном потоке
class threadRec extends Thread{

    Activity activity;
    List<Elements> mExampleList;
    int position;
    RecAdapter.ExampleViewHolder holder;

    public threadRec(Activity activity, List<Elements> mExampleList, int position, RecAdapter.ExampleViewHolder holder) {
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
                Elements currentItem = mExampleList.get(position);
                holder.mTextView1.setText("Телефон: " + currentItem.getCoast());
                holder.mTextView2.setText(currentItem.getOnePoint());
                holder.mTextView3.setText("Откуда:" + (currentItem.getTwoPoint()));
                holder.mTextView4.setText("Куда:" + currentItem.getTime());
                holder.mTextView5.setText(currentItem.getTelephone());
            }
        });
    }
}
