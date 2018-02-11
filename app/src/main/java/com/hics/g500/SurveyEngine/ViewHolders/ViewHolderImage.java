package com.hics.g500.SurveyEngine.ViewHolders;

import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hics.g500.R;
import com.hics.g500.SurveyEngine.ImageCallback;
import com.hics.g500.db.Preguntas;

/**
 * Created by david.barrera on 2/1/18.
 */

public class ViewHolderImage extends RecyclerView.ViewHolder {

    public LinearLayout linearLayout;
    public TextView titleTxt;
    public ImageView captureImg;

    public ViewHolderImage(View itemView, final Preguntas preguntas, final ImageCallback imageCallback) {
        super(itemView);

        titleTxt = (TextView)itemView.findViewById(R.id.item_survey_image_title);
        captureImg = (ImageView)itemView.findViewById(R.id.item_survey_image_capture);
        linearLayout = (LinearLayout)itemView.findViewById(R.id.item_survey_image_container);

        captureImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageCallback.takePhoto(preguntas);
            }
        });
    }
}
