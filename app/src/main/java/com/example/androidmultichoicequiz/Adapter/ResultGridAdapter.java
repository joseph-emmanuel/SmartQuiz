package com.example.androidmultichoicequiz.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidmultichoicequiz.Common.Common;
import com.example.androidmultichoicequiz.Model.CurrentQuestion;
import com.example.androidmultichoicequiz.R;

import java.util.List;

public class ResultGridAdapter extends RecyclerView.Adapter<ResultGridAdapter.MyViewHolder> {
    Context context;
    List<CurrentQuestion> currentQuestionList;

    public ResultGridAdapter(Context context, List<CurrentQuestion> currentQuestionList) {
        this.context = context;
        this.currentQuestionList = currentQuestionList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        Button btn_question;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            btn_question=(Button)itemView.findViewById(R.id.btn_question);
            btn_question.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //When user click to Question button , we will get back to QuestionActivity to show Question
                    LocalBroadcastManager.getInstance(context)
                            .sendBroadcast(new Intent(Common.KEY_BACK_FROM_RESULT).putExtra(Common.KEY_BACK_FROM_RESULT,
                                    currentQuestionList.get(getAdapterPosition()).getQuestionIndex()));
                }
            });
        }
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.layout_result_item,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Drawable img;
        //Change coloe base on result
        holder.btn_question.setText(new StringBuilder("Question ").append(currentQuestionList.get(position).getQuestionIndex()+1));
        if(currentQuestionList.get(position).getType()==Common.ANSWER_TYPE.RIGHT_ANSWER)
        {
            holder.btn_question.setBackgroundColor(Color.parseColor("#ff99cc00"));
            img=context.getResources().getDrawable(R.drawable.ic_baseline_check_white_24);
            holder.btn_question.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,img);
        }
        else if(currentQuestionList.get(position).getType()==Common.ANSWER_TYPE.WRONG_ANSWER)
        {
            holder.btn_question.setBackgroundColor(Color.parseColor("#ffcc0000"));
            img=context.getResources().getDrawable(R.drawable.ic_baseline_white_clear_24);
            holder.btn_question.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,img);
        }
        else
        {

            img=context.getResources().getDrawable(R.drawable.ic_baseline_error_outline_white_24);
            holder.btn_question.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,img);
        }
    }

    @Override
    public int getItemCount() {
        return currentQuestionList.size();
    }
}
