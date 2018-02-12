package com.hics.g500.SurveyEngine.Views;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hics.g500.Dal.Dal;
import com.hics.g500.Library.DesignUtils;
import com.hics.g500.R;
import com.hics.g500.SurveyEngine.Adapters.QuestionAdapter;
import com.hics.g500.SurveyEngine.Enums.QuestionType;
import com.hics.g500.SurveyEngine.ImageCallback;
import com.hics.g500.SurveyEngine.Model.SurveyComplete;
import com.hics.g500.SurveyEngine.Presenter.MapCallback;
import com.hics.g500.SurveyEngine.Utils.CameraUtils;
import com.hics.g500.SurveyEngine.Utils.RecyclerScroll;
import com.hics.g500.db.Preguntas;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.internal.Utils;

public class SurveyActivity extends AppCompatActivity implements ImageCallback, MapCallback {

    @BindView(R.id.act_survey_recycler)RecyclerView recyclerView;
    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.act_survey_toolbar_container)LinearLayout toolbarLayout;
    @BindView(R.id.act_survey_finish)FloatingActionButton done;
    int toolbarHeight;
    QuestionAdapter mQuestionAdapter;
    private List<Preguntas> mQuestions;
    SurveyComplete surveyComplete;
    boolean fadeToolbar = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        ButterKnife.bind(this);
        mQuestionAdapter = new QuestionAdapter(this, getSupportFragmentManager(), mQuestions, false,"CveRoute"
                ,recyclerView,surveyComplete);
        setupLayout();
        loadViews();


    }

    private void setupLayout() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);

        //  FAB margin needed for animation

        toolbarHeight = DesignUtils.getToolbarHeight(this);

        /* Set top padding= toolbar height.
         So there is no overlap when the toolbar hides.
         Avoid using 0 for the other parameters as it resets padding set via XML!*/
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
        surveyComplete = Dal.surveyComplete();
        mQuestionAdapter.setmSurveyComplete(surveyComplete);
        mQuestionAdapter.setQuestionsList(surveyComplete.getEncuesta().getPreguntas());
        mQuestionAdapter.setImageCallback(this);
        mQuestionAdapter.setmMapCallback(this);
        mQuestionAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mQuestionAdapter);
    }

    @Override
    public void takePhoto(Preguntas pregunta) {
        CameraUtils.takePhoto(SurveyActivity.this);
    }

    @Override
    public void mapCoordinate(Preguntas pregunta) {
        startActivity(new Intent(this,MapsViewActivity.class));
    }
}