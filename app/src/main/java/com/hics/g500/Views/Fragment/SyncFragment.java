package com.hics.g500.Views.Fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.hics.g500.Dal.Dal;
import com.hics.g500.Library.AnnimationsBuilding;
import com.hics.g500.Network.Request.AnswerSync;
import com.hics.g500.Network.Request.SurveySync;
import com.hics.g500.R;
import com.hics.g500.SurveyEngine.Views.SurveyActivity;
import com.hics.g500.Views.Adapter.SavedAdapter;
import com.hics.g500.Views.Adapter.SyncAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SyncFragment extends Fragment {

    @BindView(R.id.fr_sync_txt_error)TextView txtError;
    @BindView(R.id.fr_sync_recycler)RecyclerView recyclerView;
    @BindView(R.id.fr_sync_record)FloatingActionButton btnRecord;
    @BindView(R.id.animation_error_open)LottieAnimationView animationView;

    Activity mActivity;
    List<SurveySync> sync;

    private RecyclerView.LayoutManager mLayoutManager;


    public SyncFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
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
        /*Dummy*/
        sync = Dal.surveysSync();

        /*sync.add(new SurveySync(new Long(1),"user1.@qa.com",new Long(1),new ArrayList<AnswerSync>(),new Long(1),false));
        sync.add(new SurveySync(new Long(1),"user1.@qa.com",new Long(1),new ArrayList<AnswerSync>(),new Long(1),false));
        sync.add(new SurveySync(new Long(1),"user1.@qa.com",new Long(1),new ArrayList<AnswerSync>(),new Long(1),false));
        sync.add(new SurveySync(new Long(1),"user1.@qa.com",new Long(1),new ArrayList<AnswerSync>(),new Long(1),false));*/

        if (sync != null && sync.size() > 0) {
            mLayoutManager = new LinearLayoutManager(mActivity);
            recyclerView.setLayoutManager(mLayoutManager);

            recyclerView.setAdapter(new SyncAdapter(sync, mActivity));
        }else{
            txtError.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            btnRecord.setVisibility(View.GONE);
            txtError.setText("No hay encuestas para sincronizar");
        }
    }


}
