package com.hics.g500.SurveyEngine.ViewHolders;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hics.g500.Dal.Dal;
import com.hics.g500.Library.DesignUtils;
import com.hics.g500.R;
import com.hics.g500.SurveyEngine.Utils.Utils;
import com.hics.g500.db.Opciones;
import com.hics.g500.db.Preguntas;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by david.barrera on 2/6/18.
 */

public class ViewHolderSearch extends RecyclerView.ViewHolder {

    public EditText editText;
    public ListView lvResults;
    public List<Opciones> listValues;
    public Preguntas question;
    private Context mContext;
    public boolean searching = true;
    public TextView txtTitle;
    public TextView txtRequired;
    public LinearLayout lnContainer;
    public RecyclerView mRecyclerView;


    public ViewHolderSearch(View v, Preguntas questiont, List<Opciones> values, Context context,RecyclerView recyclerView) {
        super(v);
        editText = (EditText) v.findViewById(R.id.item_catalog_edt_search);
        lvResults = (ListView) v.findViewById(R.id.item_catalog_list_results);
        txtTitle = (TextView) v.findViewById(R.id.item_survey_catalog_txt_title);
        txtRequired = (TextView) v.findViewById(R.id.item_survey_catalog_txt_required);
        lnContainer = (LinearLayout) v.findViewById(R.id.catalog_linear_layout);
        question = questiont;
        mContext = context;
        mRecyclerView = recyclerView;


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (searching) {
                    String word = editText.getText().toString().toLowerCase();
                    if (word != null && !word.isEmpty()) {
                        listValues = new ArrayList<Opciones>();
                        listValues = Dal.getValueByName(word, question.getPregunta_id());
                        if (listValues != null && listValues.size() > 0) {
                            lvResults.setVisibility(View.VISIBLE);

                            lvResults.setAdapter(new ArrayAdapter<Opciones>(mContext,
                                    android.R.layout.simple_list_item_1, listValues));
                            Utils.setListViewHeightBasedOnChildrenAdapter(lvResults);
                            lnContainer.setPadding(0,0,0,0);
                            setMargins();
                        } else {
                            listValues.clear();
                            lvResults.setAdapter(null);
                            lvResults.setAdapter(new ArrayAdapter<Opciones>(mContext,
                                    android.R.layout.simple_list_item_1, listValues));
                            lvResults.setVisibility(View.GONE);
                            DesignUtils.showToast(mContext,"No existen resultados");

                        }
                    }
                }

            }
        });

        lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Utils.hideKeyboard((Activity) mContext);
                searching = false;
                editText.setText(((Opciones) lvResults.getItemAtPosition(position)).getOpcion_contenido());
                Log.d("VIEWHOLDERSEARCH", editText.getText().toString());
                //ENVIAR LA RESPUESTA TO SAVED
                /*EventBus.getDefault().postSticky(new EventSaveAnswer(question, editText.getText().toString(),
                        question.getRespuestadetalle()));*/
                listValues.clear();
                lnContainer.setPadding(32,8,32,8);
                lvResults.setAdapter(null);
                lvResults.setAdapter(new ArrayAdapter<Opciones>(mContext,
                        android.R.layout.simple_list_item_1, listValues));
                lvResults.setVisibility(View.GONE);
                searching = true;
            }
        });
    }

    private void setMargins(){
        if (mRecyclerView != null) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            int left = getPixelValue(mContext, mContext.getResources().getDimension(R.dimen.padding_gral));
            int top = getPixelValue(mContext, mContext.getResources().getDimension(R.dimen.padding_gral));
            int right = getPixelValue(mContext, mContext.getResources().getDimension(R.dimen.padding_gral));
            int bottom = getPixelValue(mContext, mContext.getResources().getDimension(R.dimen.padding_gral));
            layoutParams.setMargins(left, top, right, bottom);
            mRecyclerView.setLayoutParams(layoutParams);
        }
    }

    public static int getPixelValue(Context context, float dimenId) {
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dimenId,
                resources.getDisplayMetrics()
        );
    }

}
