package com.zack.triviaapp.data;

import com.zack.triviaapp.model.Question;

import java.util.ArrayList;

public interface AnswerListAsyncResponse {
    void processFinished(ArrayList<Question> questionArrayList);
}
