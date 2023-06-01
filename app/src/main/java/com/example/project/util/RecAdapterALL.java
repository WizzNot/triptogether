package com.example.project.util;

import com.example.project.main.allTrips;
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

public class RecAdapterALL extends RecyclerView.Adapter<RecAdapterALL.ExampleViewHolder> {   //Адаптер прокручивающегося списка объявлений пользователя
    private List<Elemensall> mExampleList;               //Массив объявлений
    private Activity activity;
    private SelectListener listener;
    //Активность

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {  //Контейнеры для информации
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView4;
        public TextView mTextView5;
        public ImageView mTextView6;
        public TextView mTextView7;
        public CardView cardView;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.text1);
            mTextView2 = itemView.findViewById(R.id.text2);
            mTextView3 = itemView.findViewById(R.id.text3);
            mTextView4 = itemView.findViewById(R.id.text4);
            mTextView5 = itemView.findViewById(R.id.text5);
            mTextView6 = itemView.findViewById(R.id.text6);
            mTextView7 = itemView.findViewById(R.id.text7);
            cardView = itemView.findViewById(R.id.main_cardview);
        }
    }

    //Конструктор адаптера
    public RecAdapterALL(List<Elemensall> exampleList, Activity activity, SelectListener lis) {
        mExampleList = exampleList;
        this.activity = activity;
        this.listener = lis;
    }


    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_element_all_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, @SuppressLint("RecyclerView") int position) {
        threadRecALL thread = new threadRecALL(activity, mExampleList, position, holder);
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
    public void filteredList(List<Elemensall> filteredList) {
        mExampleList = filteredList;
        notifyDataSetChanged();
    }
}

//Загрузка информации в отдельном потоке
class threadRecALL extends Thread{

    Activity activity;
    List<Elemensall> mExampleList;
    int position;
    RecAdapterALL.ExampleViewHolder holder;

    public threadRecALL(Activity activity, List<Elemensall> mExampleList, int position, RecAdapterALL.ExampleViewHolder holder) {
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
                Elemensall currentItem = mExampleList.get(position);
                holder.mTextView1.setText("Цена: " + currentItem.getPrices());
                holder.mTextView2.setText("Откуда:" + currentItem.getFromAddresses());
                holder.mTextView3.setText("Куда:" + currentItem.getToAddresses());
                holder.mTextView4.setText("Количество пассажиров: " + currentItem.getCountPass());
                holder.mTextView5.setText("Телефон:" + currentItem.getDriversName());
                if(currentItem.getVerification().equals("0")) holder.mTextView6.setImageResource(R.drawable.grey_tick);
                if(currentItem.getVerification().equals("1")) holder.mTextView6.setImageResource(R.drawable.verif_vk_tick);
                if(currentItem.getVerification().equals("2")) holder.mTextView6.setImageResource(R.drawable.nast_verif_vk_tick);
                holder.mTextView7.setText(currentItem.getData());
            }
        });
    }
}
