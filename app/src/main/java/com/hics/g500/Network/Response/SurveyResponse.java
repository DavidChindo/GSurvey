package com.hics.g500.Network.Response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by david.barrera on 2/8/18.
 */

public class SurveyResponse {

    @SerializedName("encuesta_id")
    private int surveyId;
    @SerializedName("encuesta_nombre")
    private String name;
    @SerializedName("encuesta_desc")
    private String description;
    @SerializedName("encuesta_preguntas")
    private ArrayList<QuestionResponse> questionResponses;

    public SurveyResponse(int surveyId, String name, String description, ArrayList<QuestionResponse> questionResponses) {
        this.surveyId = surveyId;
        this.name = name;
        this.description = description;
        this.questionResponses = questionResponses;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<QuestionResponse> getQuestionResponses() {
        return questionResponses;
    }

    public void setQuestionResponses(ArrayList<QuestionResponse> questionResponses) {
        this.questionResponses = questionResponses;
    }
}
