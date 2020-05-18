package com.zack.triviaapp.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.zack.triviaapp.controller.AppController;
import com.zack.triviaapp.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {
    private final String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";
    ArrayList<Question> questionArrayList = new ArrayList<>();

    public List<Question> getQuestions(final AnswerListAsyncResponse callBack) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d("QuestionBankReq", "onResponse: " + response);
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONArray jsonArray = response.getJSONArray(i);
                                questionArrayList.add(new Question(
                                        jsonArray.getString(0),
                                        jsonArray.getBoolean(1)
                                ));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (callBack != null) {
                            callBack.processFinished(questionArrayList);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("QuestionBankReq", "onErrorResponse: " + error);
                    }
                }
        );
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return questionArrayList;
    }
}
