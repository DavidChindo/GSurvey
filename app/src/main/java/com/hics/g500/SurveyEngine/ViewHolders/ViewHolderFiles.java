package com.hics.g500.SurveyEngine.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hics.g500.R;
import com.hics.g500.db.Preguntas;

/**
 * Created by david.barrera on 2/1/18.
 */

public class ViewHolderFiles extends RecyclerView.ViewHolder {

    public TextView nameTxt;
    public ImageView fileBtn;

    public ViewHolderFiles(View itemView, Preguntas preguntas) {
        super(itemView);
        nameTxt = (TextView)itemView.findViewById(R.id.item_survey_file_title);
        fileBtn = (ImageView)itemView.findViewById(R.id.item_survey_file_capture);

        fileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open file
            }
        });
    }
}
