package com.hics.g500.Views.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hics.g500.Dal.Dal;
import com.hics.g500.Network.Request.SurveySync;
import com.hics.g500.Presenter.Callbacks.SyncCallback;
import com.hics.g500.R;
import com.hics.g500.SurveyEngine.Views.SurveyActivity;
import com.hics.g500.db.DaoMaster;
import com.hics.g500.db.Gasolineras;
import com.hics.g500.db.Respuesta;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by david.barrera on 2/1/18.
 */

public class SyncAdapter extends RecyclerView.Adapter<SyncAdapter.ViewHolder> {

    List<SurveySync> mSync;
    Context mContext;
    SyncCallback mSyncCallback;

    public SyncAdapter(List<SurveySync> mSync, Context mContext,SyncCallback syncCallback) {
        this.mSync = mSync;
        this.mContext = mContext;
        this.mSyncCallback = syncCallback;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sync,parent,false);
        return new SyncAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Bitmap mBitmap;
        mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.foto01a);
        setCorner(holder.imgViewGas,mBitmap,mContext);

        SurveySync sync = mSync.get(position);
        if (sync != null){
            Respuesta answer = Dal.getAnsweParentById(sync.getParentId());
            if (answer != null) {
                holder.mId.setText(String.valueOf(sync.getGasolineraId()));
                holder.mName.setText(answer.getName_gas());
                holder.mCreacion.setText("Creación:\t"+answer.getFechaFin());
                holder.mSincro.setText("Sincronización:\t"+answer.getFechaSyn());
                holder.imgSync.setTag(sync);
                if (answer.getEnviada()){
                    holder.mStatus.setBackground(mContext.getResources().getDrawable(R.drawable.shape_success));
                    holder.imgSync.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_ico_sync));
                    holder.imgSync.setColorFilter(R.color.divider);
                }else{
                    holder.imgSync.setColorFilter(R.color.icon_color);
                    holder.imgSync.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mSyncCallback.onSentData((SurveySync)view.getTag());
                        }
                    });
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return mSync == null ? 0 : mSync.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_sync_id)TextView mId;
        @BindView(R.id.item_sync_name)TextView mName;
        @BindView(R.id.item_sync_creacion)TextView mCreacion;
        @BindView(R.id.item_sync_sincro)TextView mSincro;
        @BindView(R.id.item_sync_survey)LinearLayout mSurvey;
        @BindView(R.id.item_sync_img_gas)ImageView imgViewGas;
        @BindView(R.id.item_sync_rl_container)RelativeLayout mStatus;
        @BindView(R.id.item_sync_open_survey)ImageView imgSync;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }

    private void setCorner(ImageView imgView,Bitmap bitmap,Context context){
        float cornerRadius = 25.0f;
        Resources  mResources = context.getResources();
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(
                mResources,
                bitmap
        );

        roundedBitmapDrawable.setCornerRadius(cornerRadius);

        roundedBitmapDrawable.setAntiAlias(true);

        imgView.setImageDrawable(roundedBitmapDrawable);
    }
}
