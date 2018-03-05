package com.hics.g500.SurveyEngine.ViewHolders;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hics.g500.R;
import com.hics.g500.SurveyEngine.Presenter.SurveySaveCallback;
import com.hics.g500.SurveyEngine.Views.Adapter.CarruselAdapter;
import com.hics.g500.db.Opciones;
import com.hics.g500.db.Preguntas;

import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by david.barrera on 3/4/18.
 */

public class ViewHolderCarrusel extends RecyclerView.ViewHolder {

    public RecyclerView recyclerView;
    public LinearLayout mLinearSp;
    public TextView txtRequired;
    public TextView txtTitle;
    SurveySaveCallback mSurveyCallback;
    RecyclerView.LayoutManager layoutManager;
    public List<Opciones> listValues;
    public Preguntas question;
    public Opciones selectedOption;

    public ViewHolderCarrusel(final View v, SurveySaveCallback surveyCallback,Preguntas questiont, List<Opciones> values,Opciones opciones) {
        super(v);
        recyclerView = (RecyclerView) v.findViewById(R.id.item_survey_carrusel_recycler);
        mLinearSp = (LinearLayout) v.findViewById(R.id.container_carrusel);
        txtRequired = (TextView) v.findViewById(R.id.item_survey_carrusel_required);
        txtTitle = (TextView)v.findViewById(R.id.item_survey_carrusel_txt_title);
        this.mSurveyCallback = surveyCallback;
        this.listValues = values;
        this.question = questiont;
        this.selectedOption = opciones;
        //int spanCount = listValues.size() / 6;
        int spanCount = 4;

        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(v.getContext(),spanCount, LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(new CarruselAdapter(listValues,v.getContext(),R.layout.item_carrusel,mSurveyCallback,selectedOption,question));

    }

    public int returnPostion(List<Opciones> opciones, int respuestaID){
        int position = 0;
        int cont = 0;
        if (opciones != null && opciones.size() > 0){
            for (Opciones opcion : opciones){
                if (opcion.getOpcion_id() == respuestaID){
                    position = cont;
                    break;
                }
                cont++;
            }
        }
        return position;
    }

}


