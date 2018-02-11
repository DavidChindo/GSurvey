package com.hics.g500.SurveyEngine.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by david.barrera on 2/8/18.
 */

public class Utils {

    @SuppressLint("SimpleDateFormat")
    public static String getDayDateString(String dateStr) {
        DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");//Se cambio 14-12-015 anterior yyyy-dd-MM
        String formatted = null;
        try {
            Date date = formatDate.parse(dateStr);
            DateFormat format = new SimpleDateFormat("EEEE, dd MMM yyyy");
//            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
            format.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getID()));
            formatted = format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatted;
    }


    //TENGO QUE AGREGAR LA LIB DE DATETIME
  /*  public static long getDayDateService(String dateStr) {

        DateTimeFormatter formatter = DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'")
                .withZoneUTC().withLocale(Locale.US);
        DateTime dt = new DateTime() ;

        try {

            dt = formatter.parseDateTime(dateStr);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt.getMillis() ;
    }

    public static String formatterDayString(String date){
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat( "dd 'de' MMMM");
        String dayParser = "";
        try {
            dt = formatter.parseLocalDate(date).toDate();
            dayParser = sdf.format(dt);
        }catch (Exception e){
            e.printStackTrace();
        }
        return dayParser;
    }

    public static String formatterDayhourString(String date){
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy'T'HH:mm");
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM HH:mm 'hrs'");
        String dayParser = "";
        try {
            dt = formatter.parseLocalDateTime(date).toDate();

            dayParser = sdf.format(dt);

        }catch (Exception e){
            e.printStackTrace();
        }
        return dayParser;
    }

    public static Date formatterDayhourStringToDate(String date){
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");//2017-01-27T15:19:47
        Date dt = new Date();
        try {
            dt = formatter.parseLocalDateTime(date).toDate();
        }catch (Exception e){
            e.printStackTrace();
        }
        return dt;
    }
*/

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
//        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
//        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                // This next line is needed before you call measure or else you won't get measured height at all. The listitem needs to be drawn first to know the height.
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
//                view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static void setListViewHeightBasedOnChildrenAdapter(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                // This next line is needed before you call measure or else you won't get measured height at all. The listitem needs to be drawn first to know the height.
                view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    public static String getCurretDate(){

        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
        String date = sdf.format(new Date());

        return date+"T00:00:00";
    }


    public static String getCurrentTime(){
        DateFormat df = DateFormat.getTimeInstance();
        df.setTimeZone(TimeZone.getTimeZone("gmt"));
        String gmtTime = df.format(new Date());

        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" );
// or SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy KK:mm:ss a Z" );
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Log.d("FECHAINICIO", "VALOR " + sdf.format(new Date()));
/*
        String currentTime;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String currentDateandTime = sdf.format(new Date());
        currentTime = currentDateandTime;
*/
        return sdf.format(new Date());
    }

    public static String getCurrentTimeFabric(){
        DateFormat df = DateFormat.getTimeInstance();

        String gmtTime = df.format(new Date());

        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" );

        Log.d("FECHAINICIO", "VALOR " + sdf.format(new Date()));

        return sdf.format(new Date());
    }



    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
