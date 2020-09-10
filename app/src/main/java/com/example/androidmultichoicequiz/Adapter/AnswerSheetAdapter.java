package com.example.androidmultichoicequiz.Adapter;

import android.content.ContentUris;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidmultichoicequiz.Model.CurrentQuestion;
import com.example.androidmultichoicequiz.R;

import java.util.List;

public class AnswerSheetAdapter extends RecyclerView.Adapter<AnswerSheetAdapter.MyViewHolder> {

    Context context;
    List< CurrentQuestion> currentQuestions;

    public AnswerSheetAdapter(Context context, List<CurrentQuestion> currentQuestions) {
        this.context = context;
        this.currentQuestions = currentQuestions;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        View question_item;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            question_item=itemView.findViewById(R.id.question_item);
        }
    }



    @NonNull
    @Override
    public AnswerSheetAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_grid_answer_sheet_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerSheetAdapter.MyViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return currentQuestions.size();
    }


}