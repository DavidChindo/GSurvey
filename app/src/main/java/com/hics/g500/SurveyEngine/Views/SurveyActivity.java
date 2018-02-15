package com.hics.g500.SurveyEngine.Views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hics.g500.Dal.Dal;
import com.hics.g500.Library.DesignUtils;
import com.hics.g500.Library.LogicUtils;
import com.hics.g500.Library.Statics;
import com.hics.g500.Presenter.Callbacks.GasolinerasCallback;
import com.hics.g500.R;
import com.hics.g500.SurveyEngine.Adapters.QuestionAdapter;
import com.hics.g500.SurveyEngine.Enums.QuestionType;
import com.hics.g500.SurveyEngine.ImageCallback;
import com.hics.g500.SurveyEngine.Model.SurveyComplete;
import com.hics.g500.SurveyEngine.Presenter.MapCallback;
import com.hics.g500.SurveyEngine.Presenter.SurveySaveCallback;
import com.hics.g500.SurveyEngine.Utils.CameraUtils;
import com.hics.g500.SurveyEngine.Utils.Constants;
import com.hics.g500.SurveyEngine.Utils.RecyclerScroll;
import com.hics.g500.db.Gasolineras;
import com.hics.g500.db.Preguntas;
import com.hics.g500.db.Respuesta;
import com.hics.g500.db.RespuestaDetalle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.internal.Utils;

public class SurveyActivity extends AppCompatActivity implements ImageCallback, MapCallback,SurveySaveCallback {

    @BindView(R.id.act_survey_recycler)RecyclerView recyclerView;
    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.act_survey_toolbar_container)LinearLayout toolbarLayout;
    @BindView(R.id.act_survey_finish)FloatingActionButton done;
    int toolbarHeight;
    QuestionAdapter mQuestionAdapter;
    private List<Preguntas> mQuestions;
    SurveyComplete surveyComplete;
    boolean fadeToolbar = true;
    Respuesta mAnswerParent;
    private final String TAG = SurveyActivity.class.getSimpleName();
    String namePhoto = "";
    Preguntas question = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        ButterKnife.bind(this);
        Intent ActivityIntent = getIntent();
        Bundle GetData=ActivityIntent.getExtras();
        String jsonContent=GetData.getString("answerParent");
        mAnswerParent = (new Gson()).fromJson(jsonContent,Respuesta.class);
        mQuestionAdapter = new QuestionAdapter(this, getSupportFragmentManager(), mQuestions, false,"CveRoute"
                ,recyclerView,surveyComplete,this,mAnswerParent);
        setupLayout();
        loadViews();
    }

    private void setupLayout() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);

        toolbarHeight = DesignUtils.getToolbarHeight(this);

        recyclerView.setPadding(recyclerView.getPaddingLeft(), toolbarHeight,
                recyclerView.getPaddingRight(), recyclerView.getPaddingBottom()*3);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mQuestionAdapter);
        recyclerView.addOnScrollListener(new RecyclerScroll() {
            @Override
            public void show() {
                toolbarLayout.animate().translationY(0)
                        .setInterpolator(new DecelerateInterpolator(2)).start();
                if (fadeToolbar)
                    toolbarLayout.animate().alpha(1)
                            .setInterpolator(new DecelerateInterpolator(1)).start();
            }

            @Override
            public void hide() {
                toolbarLayout.animate().translationY(-toolbarHeight)
                        .setInterpolator(new AccelerateInterpolator(2)).start();
                if (fadeToolbar)
                    toolbarLayout.animate().alpha(0)
                            .setInterpolator(new AccelerateInterpolator(1)).start();

            }
        });

        toolbar.setTitle("Encuesta");
        setSupportActionBar(toolbar);
    }

    private void loadViews(){
        surveyComplete = Dal.surveyComplete(mAnswerParent.getId());
        mQuestionAdapter.setmSurveyComplete(surveyComplete);
        mQuestionAdapter.setQuestionsList(surveyComplete.getEncuesta().getPreguntas());
        mQuestionAdapter.setImageCallback(this);
        mQuestionAdapter.setmMapCallback(this);
        mQuestionAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mQuestionAdapter);
    }

    @Override
    public void takePhoto(Preguntas pregunta) {
        question = pregunta;
        namePhoto = UUID.randomUUID().toString();
        CameraUtils.takePhoto(SurveyActivity.this,namePhoto);
    }

    @Override
    public void mapCoordinate(Preguntas pregunta) {
        startActivity(new Intent(this,MapsViewActivity.class));
    }

    public void onSavePhoto( String answer){
        Preguntas pregunta = question;
        long id = Dal.insertRespuestaDetalle(mAnswerParent.getId(),pregunta.getPregunta_id(),Integer.valueOf(pregunta.getPregunta_tipo()),-1,answer,pregunta.getRespuestaDetalle());
        if(id >= 0){
            RespuestaDetalle respuestaDetalle = Dal.getRespuestaDetalleByIdLong(id);
            pregunta.setRespuestaDetalle(respuestaDetalle);
        }
    }

    @Override
    public void onSaveAnswer(String answer, int answerid, Preguntas pregunta, int typeQuestion, RespuestaDetalle answerDetail) {
        long id = Dal.insertRespuestaDetalle(mAnswerParent.getId(),pregunta.getPregunta_id(),typeQuestion,answerid,answer,answerDetail);
        if (id > 0){
            RespuestaDetalle respuestadetalle = Dal.getRespuestaDetalleByIdLong(id);
            pregunta.setRespuestaDetalle(respuestadetalle);
        }
    }

    @Override
    public void onSaveAnswerMultiOption(boolean isChecked, String answer, int answerid, Preguntas pregunta, int typeQuestion, RespuestaDetalle answerDetail) {
        long id = Dal.insertRespuestaDetalleMultiOption(isChecked,mAnswerParent.getId(),pregunta.getPregunta_id(),typeQuestion,answerid,answerDetail);
        if (id > 0){
            RespuestaDetalle respuestadetalle = Dal.getRespuestaDetalleByIdLong(id);
            pregunta.setRespuestaDetalle(respuestadetalle);
        }
    }

    @OnClick(R.id.act_survey_finish)
    void onSaveSurveyClick(){
        try {
            if (mAnswerParent != null){
                Dal.insertRespuestaParent(mAnswerParent.getEncuesta_id(),mAnswerParent.getGas_id(),true,false, LogicUtils.getCurrentHour(),"");
                Gasolineras gasolinera = Dal.gasolineraById(mAnswerParent.getGas_id());
                if (gasolinera != null){
                    Dal.insertOrUpdateGasolinera(gasolinera,true);
                    finish();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d("SurveyActivity","Excepcion saveSurvey "+e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CameraUtils.REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                Bitmap imageBitmap = CameraUtils.fullBitmap();
                File storagePath = new File(Environment.getExternalStorageDirectory(), Statics.NAME_FOLDER);
                boolean created = storagePath.mkdirs();
                if(created){
                    //The folder has been created succesfully
                    Log.d(TAG, "The folder G500 has been created succesfully");
                } else {
                    //The folder hasn't been created or had been created previously
                    Log.d(TAG, "The folder G500 hasn't been created or had been created previously");
                }

                final String namePicture = namePhoto;

                File f1 = new File(Environment.getExternalStorageDirectory() + "/" +Statics.NAME_FOLDER+"/" +CameraUtils.lastPhotoPath.getName());
                File f2 = new File(Environment.getExternalStorageDirectory() + "/" +Statics.NAME_FOLDER+"/" +namePicture+".jpg");
                f1.renameTo(f2);

                onSavePhoto(f2.getAbsolutePath());
            }
        }
    }
}
