package com.hics.g500.SurveyEngine.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.hics.g500.Library.Statics;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by david.barrera on 2/8/18.
 */

public abstract class CameraUtils {

    public static final int REQUEST_TAKE_PHOTO = 0x999;
    public static File lastPhotoPath;

    @SuppressLint("SimpleDateFormat")
    private static File createImageFile(String namePhoto) throws IOException {
        /* Create an image file name*/
        String imageFileName = namePhoto;
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Statics.NAME_FOLDER);
        if(!storageDir.exists()){
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        lastPhotoPath = image;
        return image;
    }

    public static void takePhoto(Activity context,String namePhoto) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile(namePhoto);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                context.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public static Bitmap fullBitmap() {
        try {


            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(lastPhotoPath.getAbsolutePath(), bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            /* Determine how much to scale down the image*/
            int scaleFactor = Math.min(photoW / 300, photoH / 300);

            /* Decode the image file into a Bitmap sized to fill the View*/
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;


            return BitmapFactory.decodeFile(lastPhotoPath.getAbsolutePath(), bmOptions);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getNamePicture(){
        //String timeStamp = new SimpleDateFormat("yyyy_MM_dd_T_HH_mm_ss").format(new Date());
        String timeStamp = getTimeNow();
        return  timeStamp;
    }

    public static String getTimeNow(){
        //String initialDate = new SimpleDateFormat("yyyy_MM_dd_T_HH_mm_ss").format(new Date());
        String year = new SimpleDateFormat("yyyy").format(new Date());
        String month = new SimpleDateFormat("MM").format(new Date());
        String day = new SimpleDateFormat("dd").format(new Date());
        String hour = new SimpleDateFormat("HH").format(new Date());
        String minutes = new SimpleDateFormat("mm").format(new Date());
        String seconds = new SimpleDateFormat("ss").format(new Date());
        return year + "_" + month + "_" + day + "_T_" + hour + "_" + minutes + "_" + seconds;
    }

}

