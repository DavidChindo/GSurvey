package com.hics.g500.Views.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hics.g500.R;
import com.hics.g500.Views.Adapter.SyncParentAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.galaxyofandroid.awesometablayout.AwesomeTabBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class SyncParentFragment extends Fragment {

    @BindView(R.id.tabBar)AwesomeTabBar tabBar;
    @BindView(R.id.pager)ViewPager pager;
    @BindView(R.id.nestedScrollView)NestedScrollView nestedScrollView;


    public SyncParentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nestedScrollView.setFillViewport (true);
        pager.setAdapter(new SyncParentAdapter(getFragmentManager()));
        tabBar.setupWithViewPager(pager);
        pager.setCurrentItem(1);
        pager.setCurrentItem(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_sync_parent, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

}
