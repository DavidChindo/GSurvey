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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hics.g500.R;
import com.hics.g500.SurveyEngine.Views.SurveyActivity;
import com.hics.g500.db.Gasolineras;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by david.barrera on 2/1/18.
 */

public class SavedAdapter extends RecyclerView.Adapter<SavedAdapter.ViewHolder> {

    List<String> mGasos;
    Context mContext;

    public SavedAdapter(List<String> mGasos, Context mContext) {
        this.mGasos = mGasos;
        this.mContext = mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved,parent,false);
        return new SavedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Bitmap mBitmap;
        mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.foto01a);
        setCorner(holder.imgViewGas,mBitmap,mContext);

    }

    @Override
    public int getItemCount() {
        return mGasos == null ? 0 : mGasos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_saved_id)TextView mId;
        @BindView(R.id.item_saved_name)TextView mName;
        @BindView(R.id.item_saved_address)TextView mAddress;
        @BindView(R.id.item_saved_survey)LinearLayout mSurvey;
        @BindView(R.id.item_saved_img_gas)ImageView imgViewGas;

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
