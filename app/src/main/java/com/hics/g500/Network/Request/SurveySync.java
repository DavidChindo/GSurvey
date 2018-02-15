package com.hics.g500.Network.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by david.barrera on 2/11/18.
 */

public class SurveySync {

    @SerializedName("encuesta_id")
    private long surveyId;

    @SerializedName("email")
    private String email;

    @SerializedName("gasolinera_id")
    private long gasolineraId;

    @SerializedName("respuestas")
    private ArrayList<AnswerSync> answerSyncs;

    @Expose(serialize = false, deserialize = false)
    private long parentId;

    @Expose(serialize = false, deserialize = false)
    private boolean sync;

    public SurveySync(){

    }

    public SurveySync(long surveyId, String email, long gasolineraId, ArrayList<AnswerSync> answerSyncs, long parentId, boolean sync) {
        this.surveyId = surveyId;
        this.email = email;
        this.gasolineraId = gasolineraId;
        this.answerSyncs = answerSyncs;
        this.parentId = parentId;
        this.sync = sync;
    }

    public long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(long surveyId) {
        this.surveyId = surveyId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getGasolineraId() {
        return gasolineraId;
    }

    public void setGasolineraId(long gasolineraId) {
        this.gasolineraId = gasolineraId;
    }

    public ArrayList<AnswerSync> getAnswerSyncs() {
        return answerSyncs;
    }

    public void setAnswerSyncs(ArrayList<AnswerSync> answerSyncs) {
        this.answerSyncs = answerSyncs;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }
}
