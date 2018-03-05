package com.hics.g500.SurveyEngine.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hics.g500.R;

/**
 * Created by david.barrera on 2/1/18.
 */

public class ViewHolderText extends RecyclerView.ViewHolder {
    public  TextView textView;

    public ViewHolderText(View itemView) {
        super(itemView);

        textView = (TextView)
                itemView.findViewById(R.id.item_survey_list_disclaimer_text_view);
    }
}
