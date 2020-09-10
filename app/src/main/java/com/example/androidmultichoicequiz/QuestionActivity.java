package com.example.androidmultichoicequiz;

 import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.androidmultichoicequiz.Adapter.AnswerSheetAdapter;
import com.example.androidmultichoicequiz.Common.Common;
import com.example.androidmultichoicequiz.DBHelper.DBHelper;
 import com.example.androidmultichoicequiz.Model.CurrentQuestion;
 import com.example.androidmultichoicequiz.Model.Question;
 import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.TimeUnit;

public class QuestionActivity extends AppCompatActivity
{
    int time_play= Common.TOTAL_TIME;
    boolean isAnswerModeView=false;

    TextView txt_right_answer,txt_timer;


    RecyclerView answer_sheet_view;

    AnswerSheetAdapter answerSheetAdapter;

    @Override
    protected void onDestroy() {
        if(Common.countDownTimer!=null)
           Common.countDownTimer.cancel();
        super.onDestroy();
    }

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle((Common.selectedCategory.getName()));
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);


        //getting questions from DB
        takeQuestion();

        if(Common.questionList.size()>0) {


            //Show TextView right answer and text view Timer
            txt_right_answer=(TextView)findViewById(R.id.txt_question_right);
            txt_timer=(TextView)findViewById(R.id.txt_timer);

            txt_timer.setVisibility(View.VISIBLE);
            txt_right_answer.setVisibility(View.VISIBLE);

            txt_right_answer.setText((new StringBuilder(String.format("%d/%d",Common.right_answer_count,Common.questionList.size()))));
            countDownTimer();


            //Generate answerSheet item from question
            //totoal 30 question and 30 answerSheets item
            //1 question =i answer sheet item
            
//            getAnswerSheetItems();

            //view
            answer_sheet_view = (RecyclerView) findViewById(R.id.grid_answer );
            answer_sheet_view.setHasFixedSize(true);
            if (Common.questionList.size() > 5)
                answer_sheet_view.setLayoutManager(new GridLayoutManager(this, Common.questionList.size() / 2));
            answerSheetAdapter = new AnswerSheetAdapter(this, Common.answerSheetList);
            answer_sheet_view.setAdapter(answerSheetAdapter);
        }

    }

//    private void getAnswerSheetItems() {
//    }

    private void countDownTimer() {
       if(Common.countDownTimer==null){


           Common.countDownTimer=new CountDownTimer(Common.TOTAL_TIME,1000) {
               @Override
               public void onTick(long l) {
                    txt_timer.setText(String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(l),
                            TimeUnit.MILLISECONDS.toSeconds(l) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))));
                    time_play-=1000;
               }

               @Override
               public void onFinish() {
                    //game finiishes here
               }
           }.start();
       }
       else{
           Common.countDownTimer.cancel();
           Common.countDownTimer=new CountDownTimer(Common.TOTAL_TIME,1000) {
               @Override
               public void onTick(long l) {
                   txt_timer.setText(String.format("%02d:%02d",
                           TimeUnit.MILLISECONDS.toMinutes(l),
                           TimeUnit.MILLISECONDS.toSeconds(l) -
                                   TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))));
                   time_play-=1000;
               }

               @Override
               public void onFinish() {
                   //game finiishes here
               }
           }.start();


       }

    }

    private void takeQuestion() {
        Common.questionList= DBHelper.getInstance(this).getQuestionsByCategory(Common.selectedCategory.getId());
        if(Common.questionList.size()==0){
            new MaterialStyledDialog.Builder(this)
                    .setTitle("Oops").setIcon(R.drawable.ic_baseline_sentiment_very_dissatisfied_24)
                    .setDescription("We don't have any questions in the category"+Common.selectedCategory.getName())
                    .setPositiveText("OK")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).show();
        }
        else{
            if(Common.answerSheetList.size()>0)
                Common.answerSheetList.clear();
            //Generate answerSheet item from question
            //totoal 30 question and 30 answerSheets item
            //1 question =i answer sheet item

            for(int i=0;i<Common.questionList.size();i++){
                Common.answerSheetList.add(new CurrentQuestion(i,Common.ANSWER_TYPE.NO_ANSWER));
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.question, menu);
        return true;
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}