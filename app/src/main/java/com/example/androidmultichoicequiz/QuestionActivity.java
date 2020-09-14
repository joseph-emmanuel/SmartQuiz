package com.example.androidmultichoicequiz;

 import android.app.Activity;
 import android.content.Intent;
 import android.os.Bundle;
import android.os.CountDownTimer;
 import android.text.TextUtils;
 import android.util.Log;
 import android.view.MenuItem;
 import android.view.View;
import android.view.Menu;
 import android.widget.TableLayout;
 import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.androidmultichoicequiz.Adapter.AnswerSheetAdapter;
 import com.example.androidmultichoicequiz.Adapter.QuestionFragmentAdapter;
 import com.example.androidmultichoicequiz.Common.Common;
import com.example.androidmultichoicequiz.DBHelper.DBHelper;
 import com.example.androidmultichoicequiz.Model.CurrentQuestion;
 import com.example.androidmultichoicequiz.Model.Question;
 import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
 import com.google.android.material.tabs.TabLayout;
 import com.google.gson.Gson;

 import androidx.annotation.NonNull;
 import androidx.annotation.Nullable;
 import androidx.constraintlayout.widget.ConstraintLayout;
 import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
 import androidx.viewpager.widget.ViewPager;

 import java.util.concurrent.TimeUnit;

public class QuestionActivity extends AppCompatActivity
{
    private static final int CODE_GET_RESULT =9999 ;
    int time_play= Common.TOTAL_TIME;
    boolean isAnswerModeView=false;

    TextView txt_right_answer,txt_timer,txt_wrong_answer ;


    RecyclerView answer_sheet_view;

    AnswerSheetAdapter answerSheetAdapter;

    ViewPager viewPager;
    TabLayout tabLayout;


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


        ///to be deleted


        ///till here
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

            viewPager=(ViewPager)findViewById(R.id.viewpager);
            tabLayout=(TabLayout)findViewById(R.id.sliding_tabs);

            genFragmentList();

            QuestionFragmentAdapter    questionFragmentAdapter=new QuestionFragmentAdapter(getSupportFragmentManager(),
                    this,
                    Common.fragmentsList);
            viewPager.setAdapter(questionFragmentAdapter);

            tabLayout.setupWithViewPager(viewPager);

            //Event
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                int SCROLLING_RIGHT=0;
                int SCROLLING_LEFT=1;
                int SCROLLING_UNDETERMINED=2;


                int currentScrollDirection=2;
                private  void setScrollingDirection(float postionOffset){
                    if((1-postionOffset)>=0.5)
                        this.currentScrollDirection=SCROLLING_RIGHT;
                    else if((1-postionOffset)<=0.5)
                        this.currentScrollDirection=SCROLLING_LEFT;
                }

                private boolean isScrollDiectionUndetermined(){
                    return currentScrollDirection==SCROLLING_UNDETERMINED;
                }

                private  boolean isScrollingRight(){
                    return  currentScrollDirection==SCROLLING_RIGHT;
                }
                private boolean isScrollingLeft(){
                    return currentScrollDirection==SCROLLING_LEFT;
                }

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    if(isScrollDiectionUndetermined())
                        setScrollingDirection(positionOffset);
                }

                @Override
                public void onPageSelected(int i) {

                    QuestionFragment questionFragment;
                    int positoin=0;
                    if(i>0){
                        if(isScrollingRight()){
                            //if user scroll to right,get previous fragment to calculate result
                            questionFragment=(Common.fragmentsList).get(i-1);
                            positoin=i-1;
                        }
                        else if(isScrollingLeft()){
                            //if user scroll to left,get next fragment to calculate result
                            questionFragment=(Common.fragmentsList).get(i+1);
                            positoin=i+1;
                        }
                        else {
                            questionFragment=Common.fragmentsList.get(positoin);
                        }
                    }
                    else{
                        questionFragment=Common.fragmentsList.get(0);
                        positoin=0;

                    }

                    //if you want to show currrect answer, just call function here
                    CurrentQuestion question_state=questionFragment.getSelectedAnswer();
                    Common.answerSheetList.set(positoin,question_state);//set question answer for asnwer sheet
                    answerSheetAdapter.notifyDataSetChanged();//Change color in asnwer sheet
                    countCurrectAnswer();

                    txt_right_answer.setText(new StringBuilder(String.format("%d",Common.right_answer_count))
                                    .append("/")
                                    .append(String.format("%d",Common.questionList.size())).toString());
                    txt_wrong_answer.setText(String.valueOf(Common.wrong_answer_count));

                    if(question_state.getType()!=Common.ANSWER_TYPE.NO_ANSWER){
                        questionFragment.showCorrectAnswer();
                        questionFragment.disableAnswer();

                    }


                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if(state==ViewPager.SCROLL_STATE_IDLE){
                        this.currentScrollDirection=SCROLLING_UNDETERMINED;
                    }

                }
            });



        }

    }

    private void finishGame() {

        int position =viewPager.getCurrentItem();
        QuestionFragment questionFragment=Common.fragmentsList.get(position);

        CurrentQuestion question_state=questionFragment.getSelectedAnswer();
        Common.answerSheetList.set(position,question_state);//set question answer for asnwer sheet
        answerSheetAdapter.notifyDataSetChanged();//Change color in asnwer sheet

        countCurrectAnswer();

        txt_right_answer.setText(new StringBuilder(String.format("%d",Common.right_answer_count))
                .append("/")
                .append(String.format("%d",Common.questionList.size())).toString());
        txt_wrong_answer.setText(String.valueOf(Common.wrong_answer_count));

        if(question_state.getType()!=Common.ANSWER_TYPE.NO_ANSWER){
            questionFragment.showCorrectAnswer();
            questionFragment.disableAnswer();

        }
        //we will navigate to new Result activity here

        Intent intent=new Intent(QuestionActivity.this,ResultActivity.class);
        Common.timer=Common.TOTAL_TIME-time_play;
        Common.no_answer_count=Common.questionList.size()-(Common.wrong_answer_count+Common.right_answer_count);
        Common.data_question=new StringBuilder(new Gson().toJson(Common.answerSheetList));

        startActivityForResult(intent,CODE_GET_RESULT);

    }
    private void countCurrectAnswer() {
        //Result varisbale
        Common.right_answer_count=Common.wrong_answer_count=0;
        for(CurrentQuestion item:Common.answerSheetList){
            if(item.getType()==Common.ANSWER_TYPE.RIGHT_ANSWER)
                Common.right_answer_count++;
            else if(item.getType()==Common.ANSWER_TYPE.WRONG_ANSWER)
                Common.wrong_answer_count++;

        }
    }

    private void genFragmentList() {
        for (int i=0;i<Common.questionList.size();i++){
            Bundle bundle=new Bundle();
            bundle.putInt("index",i);
            QuestionFragment fragment=new QuestionFragment();
            fragment.setArguments(bundle);


            Common.fragmentsList.add(fragment);
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
                   finishGame();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.menu_finish_game){

            if(!isAnswerModeView){
                new MaterialStyledDialog.Builder(this)
                        .setTitle("Finish ?").setIcon(R.drawable.ic_baseline_mood_24)
                .setDescription("Do you really want to finish ?")
                .setNegativeText("No")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveText("Yes")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        Log.e("TAG", "onClick: Yes yahoo" );
                        finishGame();
                    }
                }).show();
                ;
            }

            else
                finishGame();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){

        MenuItem item = menu.findItem(R.id.menu_Wrong_answer);
         ConstraintLayout constraintLayout= (ConstraintLayout) item.getActionView();
        txt_wrong_answer=(TextView)constraintLayout.findViewById(R.id.txt_wrong_answer);
        txt_wrong_answer.setText(String.valueOf(0));


        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CODE_GET_RESULT){
            if(resultCode== Activity.RESULT_OK)
            {
                String action=data.getStringExtra("action");
                if(action==null|| TextUtils.isEmpty(action)){
                    int questionNum=data.getIntExtra(Common.KEY_BACK_FROM_RESULT,-1);
                    viewPager.setCurrentItem(questionNum);

                    isAnswerModeView=true;
                    Common.countDownTimer.cancel();

                    txt_wrong_answer.setVisibility(View.GONE);
                    txt_right_answer.setVisibility(View.GONE);
                    txt_timer.setVisibility(View.GONE);
                }else
                {
                    if(action.equals("viewquizanswer"))
                    {
                        viewPager.setCurrentItem(0);

                        isAnswerModeView=true;

                        Common.countDownTimer.cancel();

                        txt_wrong_answer.setVisibility(View.GONE);
                        txt_right_answer.setVisibility(View.GONE);
                        txt_timer.setVisibility(View.GONE);

                        for(int i=0;i<Common.fragmentsList.size();i++){
                            Common.fragmentsList.get(i).showCorrectAnswer();
                            Common.fragmentsList.get(i).disableAnswer();
                        }
                    }else if(action.equals("doitagain")){

                        viewPager.setCurrentItem(0);

                        isAnswerModeView=false;
                        countDownTimer();

                        txt_wrong_answer.setVisibility(View.VISIBLE);
                        txt_right_answer.setVisibility(View.VISIBLE);
                        txt_timer.setVisibility(View.VISIBLE);

                        for (CurrentQuestion item:Common.answerSheetList){
                            item.setType(Common.ANSWER_TYPE.NO_ANSWER);//Reset all questions

                        }
                        answerSheetAdapter.notifyDataSetChanged();

                        for(int i=0;i<Common.fragmentsList.size();i++)
                            Common.fragmentsList.get(i).resetQuestion();

                    }


                }
            }
        }
    }

    //    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}