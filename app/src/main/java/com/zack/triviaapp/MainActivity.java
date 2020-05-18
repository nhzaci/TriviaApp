package com.zack.triviaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zack.triviaapp.data.AnswerListAsyncResponse;
import com.zack.triviaapp.data.QuestionBank;
import com.zack.triviaapp.model.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView questionTextView;
    private TextView questionCounterText;
    private Button trueButton;
    private ImageView nextButton;
    private ImageView prevButton;
    private Button falseButton;
    private int currentQuestionIndex;
    private List<Question> questionList;
    private final String MESSAGE_ID = "question_index";
    private int scoreNumber;
    private TextView scoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        trueButton = findViewById(R.id.true_button)
        ;
        falseButton = findViewById(R.id.false_button);
        questionCounterText = findViewById(R.id.counter_text);
        questionTextView = findViewById(R.id.question_textview);
        scoreView = findViewById(R.id.scoring_system);

        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);

        //Pick up from last qn
        SharedPreferences getSharedPrefs = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
        currentQuestionIndex = getSharedPrefs.getInt("index", 0);
        scoreNumber = getSharedPrefs.getInt("score", 0);

        scoreView.setText(String.format(Locale.ENGLISH, "%d", scoreNumber));

        questionList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                questionTextView.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
                questionCounterText.setText(String.format(Locale.ENGLISH, "%d / %d", currentQuestionIndex + 1, questionArrayList.size()));
            }
        });
        Log.d("MainOnCreate", "onCreate: " + questionList);
    }

    private void updateQuestion() {
        String question = questionList.get(currentQuestionIndex).getAnswer();
        SharedPreferences sharedPreferences = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("index", currentQuestionIndex);
        editor.apply();

        questionTextView.setText(question);
        questionCounterText.setText(String.format(Locale.ENGLISH, "%d / %d", currentQuestionIndex + 1, questionList.size()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prev_button:
                currentQuestionIndex = (currentQuestionIndex - 1) % questionList.size();
                updateQuestion();
                break;
            case R.id.next_button:
                currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
                updateQuestion();
                break;
            case R.id.true_button:
                if (questionList.get(currentQuestionIndex).isAnswerTrue()) {
                    Toast.makeText(MainActivity.this, "Well done!", Toast.LENGTH_SHORT).show();
                    fadeView();
                    currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
                    updateQuestion();
                    scoreNumber += 1;
                    scoreView.setText(String.format(Locale.ENGLISH, "%d", scoreNumber));
                    SharedPreferences sharedPreferences = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("score", scoreNumber);
                    editor.apply();
                } else {
                    Toast.makeText(MainActivity.this, "Try again!", Toast.LENGTH_SHORT).show();
                    shakeAnimation();
                    updateQuestion();
                }
                break;
            case R.id.false_button:
                if (questionList.get(currentQuestionIndex).isAnswerTrue()) {
                    Toast.makeText(MainActivity.this, "Try again!", Toast.LENGTH_SHORT).show();
                    shakeAnimation();
                    updateQuestion();
                } else {
                    Toast.makeText(MainActivity.this, "Well done!", Toast.LENGTH_SHORT).show();
                    fadeView();
                    currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
                    updateQuestion();
                    scoreNumber += 1;
                    scoreView.setText(String.format(Locale.ENGLISH, "%d", scoreNumber));
                    SharedPreferences sharedPreferences = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("score", scoreNumber);
                    editor.apply();
                }
                break;
        }
    }

    private void fadeView() {
        final CardView cardView = findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);

        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);
        final CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
