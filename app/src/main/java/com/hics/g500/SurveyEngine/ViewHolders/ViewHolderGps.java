package com.hics.g500.SurveyEngine.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hics.g500.R;
import com.hics.g500.SurveyEngine.Presenter.MapCallback;
import com.hics.g500.db.Preguntas;


/**
 * Created by david.barrera on 2/1/18.
 */

public class ViewHolderGps extends RecyclerView.ViewHolder {

    public TextView titleTxt;
    public ImageView mapButton;

    public ViewHolderGps(View itemView, final Preguntas preguntas, final MapCallback mapCallback) {
        super(itemView);

        titleTxt = (TextView)itemView.findViewById(R.id.item_survey_map_title);
        mapButton = (ImageView)itemView.findViewById(R.id.item_survey_map_capture);

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapCallback.mapCoordinate(preguntas);
            }
        });
    }
}
