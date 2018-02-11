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
import android.widget.Toast;

import com.hics.g500.R;
import com.hics.g500.SurveyEngine.Views.SurveyActivity;
import com.hics.g500.db.Gasolineras;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by david.barrera on 2/1/18.
 */

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {

    List<Gasolineras> mGasos;
    Context mContext;

    public RouteAdapter(List<Gasolineras> mGasos, Context mContext) {
        this.mGasos = mGasos;
        this.mContext = mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route,parent,false);
        return new RouteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Bitmap mBitmap;
        mBitmap = BitmapFactory.decodeResource(mContext.getResources(), drawableResource(position));
        setCorner(holder.imgViewGas,mBitmap,mContext);
        Gasolineras gasolinera = mGasos.get(position);
        if (gasolinera != null){
            holder.mId.setText(String.valueOf(gasolinera.getGas_id()));
            holder.mName.setText(gasolinera.getNombre_gas());
            holder.mAddress.setText(capitalize(gasolinera.getDireccion()));

            holder.mSurvey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(new Intent(mContext, SurveyActivity.class));
                }
            });

            holder.mInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(new Intent(mContext, SurveyActivity.class));
                }
            });

            holder.imgViewOpenSurvey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(new Intent(mContext, SurveyActivity.class));
                }
            });

            holder.imgViewOpenMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "Muestrar en mapa", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mGasos == null ? 0 : mGasos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_route_id)TextView mId;
        @BindView(R.id.item_route_name)TextView mName;
        @BindView(R.id.item_route_address)TextView mAddress;
        @BindView(R.id.item_route_survey)LinearLayout mSurvey;
        @BindView(R.id.item_route_ln_info)LinearLayout mInfo;
        @BindView(R.id.item_route_img_gas)ImageView imgViewGas;
        @BindView(R.id.item_route_open_survey)ImageView imgViewOpenSurvey;
        @BindView(R.id.item_route_open_map)ImageView imgViewOpenMap;


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

    private int drawableResource(int position){
        if (position > -1){
            switch (position){
                case 0:
                    return R.drawable.foto01a;
                case 1:
                    return R.drawable.foto02a;
                case 2:
                    return R.drawable.foto04a;
                default:
                    return R.drawable.foto05a;
            }
        }else{
            return R.drawable.foto03a;
        }
    }

    private String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z-áéíóú])([a-z-áéíóú]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }

}
