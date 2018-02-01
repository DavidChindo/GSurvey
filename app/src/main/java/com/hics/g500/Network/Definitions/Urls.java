package com.hics.g500.Network.Definitions;

import android.content.Context;

import com.hics.g500.R;

/**
 * Created by david.barrera on 1/31/18.
 */

public class Urls {

    public static final int STAGE_QA = 1;
    public static final int STAGE_PRODUCTION = 2;

    public static String initStatics(Context context, int  mode){
        if (mode == STAGE_QA) {
            return context.getResources().getString(R.string.url_qa);
        }else if(mode == STAGE_PRODUCTION) {
            return context.getResources().getString(R.string.url_prod);
        }else{
            return context.getResources().getString(R.string.url_prod);
        }
    }

}
