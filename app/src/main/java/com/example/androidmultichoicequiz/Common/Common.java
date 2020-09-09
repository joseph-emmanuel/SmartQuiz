package com.example.androidmultichoicequiz.Common;

import com.example.androidmultichoicequiz.Model.Category;

public class Common {
    public static Category selectedCategory=new Category();

    public enum ANSWER_TYPE{
        NO_ANSWER,
        WRONG_ANSWER,
        RIGHT_ANSWER
    }
}
