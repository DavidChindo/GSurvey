package com.hics.g500.Views.Fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.hics.g500.Dal.Dal;
import com.hics.g500.Library.AnnimationsBuilding;
import com.hics.g500.Library.DesignUtils;
import com.hics.g500.Library.LogicUtils;
import com.hics.g500.Network.Request.AnswerSync;
import com.hics.g500.Network.Request.SurveySync;
import com.hics.g500.Network.Response.SentDataReponse;
import com.hics.g500.Network.Response.UploadFileResponse;
import com.hics.g500.Presenter.Callbacks.SyncCallback;
import com.hics.g500.Presenter.Implementations.SyncPresenter;
import com.hics.g500.R;
import com.hics.g500.SurveyEngine.Utils.Utils;
import com.hics.g500.SurveyEngine.Views.SurveyActivity;
import com.hics.g500.Views.Adapter.SavedAdapter;
import com.hics.g500.Views.Adapter.SyncAdapter;
import com.hics.g500.db.Respuesta;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SyncFragment extends Fragment implements SyncCallback{

    @BindView(R.id.fr_sync_txt_error)TextView txtError;
    @BindView(R.id.fr_sync_recycler)RecyclerView recyclerView;
    @BindView(R.id.fr_sync_record)FloatingActionButton btnRecord;
    @BindView(R.id.animation_error_open)LottieAnimationView animationView;

    Activity mActivity;
    List<SurveySync> sync;
    SyncPresenter syncPresenter;
    ProgressDialog mProgressDialog;
    SurveySync mSurveySync;
    UploadFileResponse mUploadFileResponse;
    int cont = 0;

    private RecyclerView.LayoutManager mLayoutManager;

    public SyncFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SyncfRAGMENT" , "oncreate");

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        syncPresenter = new SyncPresenter(this,mActivity);
        Log.d("SyncFragment","entro");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("aUI","ASDA");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sync, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    private void initView(){
        mActivity = getActivity();
        btnRecord.setAnimation(AnnimationsBuilding.getDown(mActivity));
        sync = Dal.surveysSync();

        initRecycler();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(SyncFragment.class.getSimpleName(),"onResume");
    }



    private void initRecycler(){
        if (sync != null && sync.size() > 0) {
            mLayoutManager = new LinearLayoutManager(mActivity);
            recyclerView.setLayoutManager(mLayoutManager);

            recyclerView.setAdapter(new SyncAdapter(sync, mActivity,this));
        }else{
            txtError.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            btnRecord.setVisibility(View.GONE);
            txtError.setText("No hay encuestas para sincronizar");
        }
    }

    @Override
    public void onSentData(SurveySync respuesta) {
        if (respuesta != null){
            mProgressDialog = ProgressDialog.show(mActivity, null, "Enviando...");
            mProgressDialog.setCancelable(false);
            mSurveySync = respuesta;
            syncPresenter.uploadFile(respuesta);
        }else{
            DesignUtils.errorMessage(mActivity,"","Por el momento no se pueden enviar las respuestas");
        }
    }

    @Override
    public void onUploadFile(SurveySync respuesta) {

    }

    @Override
    public void onSyncDataSuccess(SentDataReponse sentDataReponse) {
        if (sentDataReponse != null){
            Respuesta respuesta = Dal.getAnsweParentById(mSurveySync.getParentId());
            if (respuesta != null) {
                Dal.updateRespuestaParent(respuesta.getEncuesta_id(),respuesta.getGas_id(),
                        respuesta.getCompletada(),true,respuesta.getFechaFin(), LogicUtils.getCurrentHour());
                sync = Dal.surveysSync();
                initRecycler();
            }
            mProgressDialog.dismiss();
            DesignUtils.showDialog("Sincronización",sentDataReponse.getMessage() +" y archivos recibidos",mActivity);
        }
    }

    @Override
    public void onSyncDataError(String msg) {
        mProgressDialog.dismiss();
        DesignUtils.errorMessage(mActivity,"",msg);
    }

    @Override
    public void onUploadFileSuccess(UploadFileResponse uploadFileResponse) {
        if (uploadFileResponse != null){
            mUploadFileResponse = uploadFileResponse;
            syncPresenter.syncData(mSurveySync);
        }
    }

    @Override
    public void onUploadFileErro(String msg) {
        mProgressDialog.dismiss();
        DesignUtils.errorMessage(mActivity,"",msg);
    }


}
