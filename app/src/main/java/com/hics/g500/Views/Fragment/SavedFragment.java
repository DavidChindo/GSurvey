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
import com.hics.g500.Library.AnnimationsBuilding;
import com.hics.g500.R;
import com.hics.g500.Views.Adapter.RouteAdapter;
import com.hics.g500.Views.Adapter.SavedAdapter;
import com.hics.g500.db.Gasolineras;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SavedFragment extends Fragment {

    @BindView(R.id.fr_saved_txt_error)TextView txtError;
    @BindView(R.id.fr_saved_recycler)RecyclerView recyclerView;
    @BindView(R.id.fr_saved_record)FloatingActionButton btnRecord;
    @BindView(R.id.animation_error_open)LottieAnimationView animationView;

    Activity mActivity;
    List<String> saved;

    private RecyclerView.LayoutManager mLayoutManager;


    public SavedFragment() {
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
        View view = inflater.inflate(R.layout.fragment_saved, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    private void initView(){
        mActivity = getActivity();
        btnRecord.setAnimation(AnnimationsBuilding.getDown(mActivity));
        /*Dummy*/
        saved = new ArrayList<String>();

        /**Gasolineras gas1 = new Gasolineras(1,"GasG500 Noorte","19.222222,99.22020202","Av. Insurgentes #24, Benito Juarez, CDMX",false,"2018/22/02");
        Gasolineras gas2 = new Gasolineras(2,"GasG500 Suur","19.222222,99.22020202","Eje 6, CDMX",false,"2018/22/04");
        Gasolineras gas3 = new Gasolineras(3,"GasG500 Esste","19.222222,99.22020202","Av. Central, Benito Juarez, CDMX",false,"2018/27/01");
*/
        saved.add("1");
        saved.add("2");
        saved.add("3");

        mLayoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(new SavedAdapter(saved,mActivity));
    }

}
