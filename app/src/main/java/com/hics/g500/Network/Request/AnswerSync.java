package com.hics.g500.Network.Request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by david.barrera on 2/11/18.
 */

public class AnswerSync {

    @SerializedName("pregunta_id")
    private long questionId;

    @SerializedName("tipo_id")
    private int typeId;

    @SerializedName("respuesta_a_opcion_id")
    private int answerCode;

    @SerializedName("respuesta_b_contanidolibre")
    private String answerString;

    public AnswerSync(long questionId, int typeId, int answerCode, String answerString) {
        this.questionId = questionId;
        this.typeId = typeId;
        this.answerCode = answerCode;
        this.answerString = answerString;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getAnswerCode() {
        return answerCode;
    }

    public void setAnswerCode(int answerCode) {
        this.answerCode = answerCode;
    }

    public String getAnswerString() {
        return answerString;
    }

    public void setAnswerString(String answerString) {
        this.answerString = answerString;
    }
}
