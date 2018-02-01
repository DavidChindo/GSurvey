package com.hics.g500.Library;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.hics.g500.R;

/**
 * Created by david.barrera on 2/1/18.
 */

public class AnnimationsBuilding {

    public static Animation getLeft(Context context){
        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.left_item);
        animation.setDuration(1000);

        return animation;
    }

    public static Animation getRigth(Context context){
        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.rigth_item);
        animation.setDuration(1000);

        return animation;
    }

    public static Animation getUp(Context context){
        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.up_item);
        animation.setDuration(1000);

        return animation;
    }

    public static Animation getDown(Context context){
        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.down_item);
        animation.setDuration(1000);

        return animation;
    }

}
