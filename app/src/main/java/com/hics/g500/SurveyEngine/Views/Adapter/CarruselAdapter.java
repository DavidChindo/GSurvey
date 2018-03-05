package com.hics.g500.SurveyEngine.Views.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Path;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hics.g500.R;
import com.hics.g500.SurveyEngine.Presenter.SurveySaveCallback;
import com.hics.g500.db.Opciones;
import com.hics.g500.db.Preguntas;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david.barrera on 3/4/18.
 */

public class CarruselAdapter extends RecyclerView.Adapter<CarruselAdapter.CarruselViewHolder>{

    List<Opciones> mValues = new ArrayList<>();
    Context context = null;
    int resource = 0;
    SurveySaveCallback mSurveyCallback;
    Opciones mOption;
    Preguntas mQuestion;


    public CarruselAdapter(List<Opciones> values, Context context, int resource, SurveySaveCallback surveySaveCallback, Opciones opcion, Preguntas question) {
        this.mValues = values;
        this.context = context;
        this.resource = resource;
        this.mSurveyCallback = surveySaveCallback;
        this.mOption = opcion;
        this.mQuestion = question;
    }

    @Override
    public CarruselViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrusel,parent,false);

        return new CarruselViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CarruselViewHolder holder, int position) {

        Opciones opciones = mValues.get(position);

        if (opciones != null){
            if (opciones.getOpcion_url() != null && !opciones.getOpcion_url().isEmpty()){
                try {
                    holder.imgView.setVisibility(View.VISIBLE);
                    holder.txt.setVisibility(View.GONE);
                    Glide.with(context).load(opciones.getOpcion_url())
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .error(R.drawable.placeholder_image)
                            .placeholder(R.drawable.placeholder_image)
                            .into(holder.imgView);
                    if (mOption != null){
                        if (mOption.getOpcion_id() == opciones.getOpcion_id()){
                            holder.view.setVisibility(View.VISIBLE);
                        }else{
                            holder.view.setVisibility(View.GONE);
                        }
                    }else {
                        holder.view.setVisibility(View.GONE);
                    }
                    holder.imgView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mSurveyCallback.onSaveAnswer(null,((Opciones)view.getTag()).getOpcion_id().intValue(),mQuestion,
                                    Integer.parseInt(mQuestion.getPregunta_tipo()),mQuestion.getRespuestaDetalle());
                            mOption = ((Opciones)view.getTag());
                            notifyDataSetChanged();
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
                holder.imgView.setVisibility(View.GONE);
                holder.txt.setVisibility(View.VISIBLE);
                holder.txt.setText(opciones.getOpcion_contenido());
                holder.txt.setTag(opciones);
                if (mOption != null){
                    if (mOption.getOpcion_id() == opciones.getOpcion_id()){
                        holder.view.setVisibility(View.VISIBLE);
                    }else{
                        holder.view.setVisibility(View.GONE);
                    }
                }else {
                    holder.view.setVisibility(View.GONE);
                }
                holder.txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mSurveyCallback.onSaveAnswer(null,((Opciones)view.getTag()).getOpcion_id().intValue(),mQuestion,
                                Integer.parseInt(mQuestion.getPregunta_tipo()),mQuestion.getRespuestaDetalle());
                        mOption = ((Opciones)view.getTag());
                        notifyDataSetChanged();
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class CarruselViewHolder extends RecyclerView.ViewHolder {

        ImageView imgView;
        TextView txt;
        View view;
        public CarruselViewHolder(View itemView) {
            super(itemView);

            imgView = itemView.findViewById(R.id.item_carrusel_img);
            txt = itemView.findViewById(R.id.item_carrusel_text);
            view = itemView.findViewById(R.id.item_carrusel_view);
        }
    }


}

