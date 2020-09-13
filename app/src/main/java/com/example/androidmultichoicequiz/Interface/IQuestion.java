package com.example.androidmultichoicequiz.Interface;

import com.example.androidmultichoicequiz.Model.CurrentQuestion;

public interface IQuestion {

    CurrentQuestion getSelectedAnswer();//get selected answer from the user select
    void showCorrectAnswer();//Bold correct answer;
    void disableAnswer();//Disable all checked box;
    void resetQuestion();//Reset all function on question

}
