package com.hics.g500.Views.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hics.g500.Network.Request.SurveySync;
import com.hics.g500.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by david.barrera on 2/11/18.
 */

public class NotesVoiceAdapter  extends RecyclerView.Adapter<NotesVoiceAdapter.ViewHolder> {

    List<SurveySync> mSync;
    Context mContext;

    public NotesVoiceAdapter(List<SurveySync> mSync, Context mContext) {
        this.mSync = mSync;
        this.mContext = mContext;
    }


    @Override
    public NotesVoiceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voz,parent,false);
        return new NotesVoiceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotesVoiceAdapter.ViewHolder holder, int position) {
        SurveySync sync = mSync.get(position);

    }

    @Override
    public int getItemCount() {
        return mSync == null ? 0 : mSync.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_voz_name)TextView mName;
        @BindView(R.id.item_voz_crea)TextView mCreacion;
        @BindView(R.id.item_voz_sync)TextView mSincro;
        @BindView(R.id.item_voz_survey)LinearLayout mSurvey;
        @BindView(R.id.item_voz_open_survey)ImageView mSyncBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }


}

