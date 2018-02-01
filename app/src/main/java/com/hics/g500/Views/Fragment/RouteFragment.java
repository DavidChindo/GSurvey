package com.hics.g500.Views.Fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
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
import com.hics.g500.db.Gasolineras;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.RecyclerView.HORIZONTAL;

/**
 * A simple {@link Fragment} subclass.
 */
public class RouteFragment extends Fragment {

    @BindView(R.id.fr_route_txt_error)TextView txtError;
    @BindView(R.id.fr_route_recycler)RecyclerView recyclerView;
    @BindView(R.id.fr_route_record)FloatingActionButton btnRecord;
    @BindView(R.id.animation_error_open)LottieAnimationView animationView;

    Activity mActivity;
    List<Gasolineras> gasos;

    private RecyclerView.LayoutManager mLayoutManager;

    public RouteFragment() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_route, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    private void initView(){
        mActivity = getActivity();
        btnRecord.setAnimation(AnnimationsBuilding.getDown(mActivity));
        /*Dummy*/
        gasos = new ArrayList<Gasolineras>();

        Gasolineras gas1 = new Gasolineras(1,"GasG500 Norte","19.222222,99.22020202","Av. Insurgentes #24, Benito Juarez, CDMX",false,"2018/22/02");
        Gasolineras gas2 = new Gasolineras(2,"GasG500 Sur","19.222222,99.22020202","Eje 6, CDMX",false,"2018/22/04");
        Gasolineras gas3 = new Gasolineras(3,"GasG500 Este","19.222222,99.22020202","Av. Central, Benito Juarez, CDMX",false,"2018/27/01");

        gasos.add(gas1);
        gasos.add(gas2);
        gasos.add(gas3);

        mLayoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(new RouteAdapter(gasos,mActivity));
    }
}
