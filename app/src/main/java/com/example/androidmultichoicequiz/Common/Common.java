package com.example.androidmultichoicequiz.Common;

import android.os.CountDownTimer;

import com.example.androidmultichoicequiz.Model.Category;
import com.example.androidmultichoicequiz.Model.CurrentQuestion;
import com.example.androidmultichoicequiz.Model.Question;
import com.example.androidmultichoicequiz.QuestionFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Common {
    public static final int TOTAL_TIME =20*60*1000 ;
    public static List<Question> questionList=new ArrayList<>();
    public static Category selectedCategory=new Category();
    public  static List<CurrentQuestion > answerSheetList=new ArrayList<>();


    public static CountDownTimer countDownTimer;
    public static int right_answer_count=0;
    public static int wrong_answer_count=0;
    public static ArrayList<QuestionFragment> fragmentsList=new ArrayList<>();
    public static TreeSet<String> selectedValue=new TreeSet<>();

    public enum ANSWER_TYPE{
        NO_ANSWER,
        WRONG_ANSWER,
        RIGHT_ANSWER
    }
}
