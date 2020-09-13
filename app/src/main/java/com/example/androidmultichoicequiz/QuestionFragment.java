package com.example.androidmultichoicequiz;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidmultichoicequiz.Common.Common;
import com.example.androidmultichoicequiz.Interface.IQuestion;
import com.example.androidmultichoicequiz.Model.CurrentQuestion;
import com.example.androidmultichoicequiz.Model.Question;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionFragment extends Fragment  implements IQuestion {

    TextView txt_question_text;
    CheckBox cbkA,cbkB,cbkC,cbkD;
    FrameLayout layout_image;
    ProgressBar progressBar;


    Question question;
    int questionIndex=-1;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public QuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionFragment newInstance(String param1, String param2) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView= inflater.inflate(R.layout.fragment_question, container, false);
        //Get question

        questionIndex=getArguments().getInt("index",-1);

        question= Common.questionList.get(questionIndex);
        layout_image = (FrameLayout) itemView.findViewById(R.id.layout_image);//To be gone before submission

        if (question != null) {


            progressBar =(ProgressBar)itemView.findViewById(R.id.progress_bar);
            if(question.getIsImageQuestion()){
                ImageView img_question=(ImageView)itemView.findViewById(R.id.img_question);
                Picasso.get().load(question.getQuestionImage()).into(img_question, new Callback() {
                    @Override
                    public void onSuccess() {

                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getContext(),""+e.getMessage(),Toast.LENGTH_SHORT ).show();
                    }
                });
            }

            else {

                layout_image.setVisibility(View.GONE);
            }


            //View
            txt_question_text = (TextView) itemView.findViewById(R.id.txt_question_text);
            txt_question_text.setText(question.getQuestionText());

            cbkA = (CheckBox) itemView.findViewById(R.id.ckbA);
            cbkA.setText(question.getAnswerA());
            cbkA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b)
                        Common.selectedValue.add(cbkA.getText().toString());
                    else
                        Common.selectedValue.remove(cbkA.getText().toString());
                }
            });

            cbkB = (CheckBox) itemView.findViewById(R.id.ckbB);
            cbkB.setText(question.getAnswerB());
            cbkB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b)
                        Common.selectedValue.add(cbkB.getText().toString());
                    else
                        Common.selectedValue.remove(cbkB.getText().toString());
                }
            });

            cbkC = (CheckBox) itemView.findViewById(R.id.ckbC);
            cbkC.setText(question.getAnswerC());
            cbkC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b)
                        Common.selectedValue.add(cbkC.getText().toString());
                    else
                        Common.selectedValue.remove(cbkC.getText().toString());
                }
            });

            cbkD = (CheckBox) itemView.findViewById(R.id.ckbD);
            cbkD.setText(question.getAnswerD());
            cbkD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b)
                        Common.selectedValue.add(cbkD.getText().toString());
                    else
                        Common.selectedValue.remove(cbkD.getText().toString());
                }
            });

            //layout_image = (FrameLayout) itemView.findViewById(R.id.layout_image);//to undo before submission
        }

        return  itemView;
    }

    @Override
    public CurrentQuestion getSelectedAnswer() {
        //This function will return state of answer
        //Right,wrong or normal
        CurrentQuestion currentQuestion=new CurrentQuestion(questionIndex,Common.ANSWER_TYPE.NO_ANSWER);//Default no answer
        StringBuilder result =new StringBuilder();
        if(Common.selectedValue.size()>1)
        {
            //if multichoice
            //Split answer to array
            //example:  //arrayAnswer[0]= A. New york
                        //arrayAnswer[1]= B. Paris
            Object[] arrayAnswer=Common.selectedValue.toArray();
            for (int i=0;i<arrayAnswer.length;i++){
                if(i<arrayAnswer.length-1)
                    result.append(new StringBuilder(((String)arrayAnswer[i])
                            .substring(0,1)).append(","));
                //Take first letter of Answer, like [A. New york],will be letter A
                else
                    result.append(new StringBuilder((String)arrayAnswer[i]).substring(0,1));

            }


        }
        else if(Common.selectedValue.size() ==1){
            //if only one choice
            Object[] arrayAnswer=Common.selectedValue.toArray();
            result.append((String)arrayAnswer[0]).substring(0,1);
        }
        if(question!=null){
            //compare the correct answer with user answer
            if(!TextUtils.isEmpty(result)){
                if(result.toString().equals(question.getCorrectAnswer()))
                    currentQuestion.setType(Common.ANSWER_TYPE.RIGHT_ANSWER);
                else
                    currentQuestion.setType(Common.ANSWER_TYPE.WRONG_ANSWER);
            }
            else
                currentQuestion.setType(Common.ANSWER_TYPE.NO_ANSWER);
        }
        else {
            Toast.makeText(getContext(), "Cannot get question", Toast.LENGTH_SHORT).show();
            currentQuestion.setType(Common.ANSWER_TYPE.NO_ANSWER);
        }
        Common.selectedValue.clear();//Always clear selected_value when comapre done

        return currentQuestion;
    }

    @Override
    public void showCorrectAnswer() {

        //Bold correct answer
        //pattern:A<B
        String[] correctAnswer=question.getCorrectAnswer().split(",");
        for (String answer:correctAnswer){
            if(answer.equals("A")){
                cbkA.setTypeface(null,Typeface.BOLD);
                cbkA.setTextColor(Color.RED);
            }
            else if(answer.equals("B")){
                cbkB.setTypeface(null,Typeface.BOLD);
                cbkB.setTextColor(Color.RED);
            }
            else if(answer.equals("C")){
                cbkC.setTypeface(null,Typeface.BOLD);
                cbkC.setTextColor(Color.RED);
            }
            else if(answer.equals("D")){
                cbkD.setTypeface(null,Typeface.BOLD);
                cbkD.setTextColor(Color.RED);
            }
        }
    }

    @Override
    public void disableAnswer() {
        cbkA.setEnabled(false);
        cbkB.setEnabled(false);
        cbkC.setEnabled(false);
        cbkD.setEnabled(false);

    }

    @Override
    public void resetQuestion() {

        //Enable CheckedBox
        cbkA.setEnabled(true);
        cbkB.setEnabled(true);
        cbkC.setEnabled(true);
        cbkD.setEnabled(true);
        //remove all selected
        cbkA.setChecked(false);
        cbkB.setChecked(false);
        cbkC.setChecked(false);
        cbkD.setChecked(false);
        //remove all bold on text

        cbkA.setTypeface(null, Typeface.NORMAL);
        cbkA.setTextColor(Color.BLACK);

        cbkB.setTypeface(null, Typeface.NORMAL);
        cbkB.setTextColor(Color.BLACK);

        cbkC.setTypeface(null, Typeface.NORMAL);
        cbkC.setTextColor(Color.BLACK);

        cbkD.setTypeface(null, Typeface.NORMAL);
        cbkD.setTextColor(Color.BLACK);

    }
}