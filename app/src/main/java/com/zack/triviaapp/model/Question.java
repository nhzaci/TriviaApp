package com.zack.triviaapp.model;

import androidx.annotation.NonNull;

public class Question {
    private String answer;
    private boolean answerTrue;

    Question() {}

    public Question(String answer, boolean answerTrue) {
        this.answer = answer;
        this.answerTrue = answerTrue;
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isAnswerTrue() {
        return this.answerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        this.answerTrue = answerTrue;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("Question{answer='%s', answerTrue=%b}", this.answer, this.answerTrue);
    }
}
