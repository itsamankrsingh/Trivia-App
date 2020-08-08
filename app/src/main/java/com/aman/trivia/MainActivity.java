package com.aman.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aman.trivia.data.AnswerListAsyncResponse;
import com.aman.trivia.data.QuestionBank;
import com.aman.trivia.model.Question;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView questionTextView, questionCounterTextView;
    private ImageButton nextButton, prevButton;
    private Button trueButton, falseButton;
    private int currentQuestionIndex = 0;
    private List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionTextView=findViewById(R.id.txtQuestion);
        questionCounterTextView=findViewById(R.id.txtCounter);
        nextButton=findViewById(R.id.imgBtnNext);
        prevButton=findViewById(R.id.imgBtnPrev);
        trueButton=findViewById(R.id.btnTrue);
        falseButton=findViewById(R.id.btnFalse);

        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);;
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);

        questionList = new QuestionBank().getQuestion(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                questionTextView.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
                questionCounterTextView.setText(currentQuestionIndex+" / "+questionArrayList.size());
                Log.d("Inside", "processFinished: " + questionArrayList);
            }
        });
        //Log.d("Main","onCreqate: "+questionList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgBtnPrev:
                if (currentQuestionIndex>0){
                    currentQuestionIndex=(currentQuestionIndex-1)%questionList.size();
                    updateQuestion();
                }
                break;
            case R.id.imgBtnNext:
                currentQuestionIndex=(currentQuestionIndex+1)%questionList.size();
                updateQuestion();
                break;
            case R.id.btnTrue:
                checkAnswer(true);
                updateQuestion();
                break;
            case R.id.btnFalse:
                checkAnswer(false);
                updateQuestion();
                break;
        }
    }

    private void checkAnswer(boolean userChoice) {
        boolean answerIsTrue=questionList.get(currentQuestionIndex).isAnswerTrue();
        int toastMessageId=0;
        if(answerIsTrue==userChoice){
            toastMessageId=R.string.correct_answer;
        }else{
            rotateAnimation();
            toastMessageId=R.string.wrong_answer;
        }
        Toast.makeText(this, toastMessageId, Toast.LENGTH_SHORT).show();
    }



    private void updateQuestion() {
        String question=questionList.get(currentQuestionIndex).getAnswer();
        questionTextView.setText(question);
        questionCounterTextView.setText(currentQuestionIndex+" / "+questionList.size());
    }

    private void rotateAnimation(){
        Animation rotate= AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake_animation);
        CardView cardView=findViewById(R.id.cardView);
        cardView.setAnimation(rotate);;
    }
}