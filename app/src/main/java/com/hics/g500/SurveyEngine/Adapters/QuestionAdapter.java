package com.hics.g500.SurveyEngine.Adapters;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.provider.SyncStateContract;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.hics.g500.R;
import com.hics.g500.SurveyEngine.Enums.QuestionType;
import com.hics.g500.SurveyEngine.ImageCallback;
import com.hics.g500.SurveyEngine.Model.SurveyComplete;
import com.hics.g500.SurveyEngine.Presenter.MapCallback;
import com.hics.g500.SurveyEngine.Presenter.SurveySaveCallback;
import com.hics.g500.SurveyEngine.Utils.Constants;
import com.hics.g500.SurveyEngine.Utils.Validations;
import com.hics.g500.SurveyEngine.ViewHolders.ViewHolderCarrusel;
import com.hics.g500.SurveyEngine.ViewHolders.ViewHolderDate;
import com.hics.g500.SurveyEngine.ViewHolders.ViewHolderEditText;
import com.hics.g500.SurveyEngine.ViewHolders.ViewHolderEditTextNumber;
import com.hics.g500.SurveyEngine.ViewHolders.ViewHolderFiles;
import com.hics.g500.SurveyEngine.ViewHolders.ViewHolderGps;
import com.hics.g500.SurveyEngine.ViewHolders.ViewHolderImage;
import com.hics.g500.SurveyEngine.ViewHolders.ViewHolderMultiChoiceCheck;
import com.hics.g500.SurveyEngine.ViewHolders.ViewHolderMultiChoiceSpinner;
import com.hics.g500.SurveyEngine.ViewHolders.ViewHolderRadIoGroup;
import com.hics.g500.SurveyEngine.ViewHolders.ViewHolderSearch;
import com.hics.g500.SurveyEngine.ViewHolders.ViewHolderText;
import com.hics.g500.SurveyEngine.ViewHolders.ViewHolderTime;
import com.hics.g500.db.Opciones;
import com.hics.g500.db.Preguntas;
import com.hics.g500.db.Respuesta;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by david.barrera on 2/1/18.
 */

public class QuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    public static final String TAG = "question_adapter";
    private final Activity context;
    private final FragmentManager fm;
    int i = 0;
    int cont = 0, sizerg = 0;
    private List<Preguntas> questions;
    private boolean isNew;

    private boolean isTable = false;
    String cve;
    private RecyclerView mRecyclerView;
    int sum = 0;
    SurveyComplete mSurveyComplete;
    ImageCallback mImageCallback;
    MapCallback mMapCallback;
    SurveySaveCallback mSurveySaveCallback;
    Respuesta mAnswerParent;


    public QuestionAdapter(Activity context, FragmentManager fm, List<Preguntas> questions, boolean isTable, String cve, RecyclerView recyclerView, SurveyComplete surveyComplete, SurveySaveCallback surveySaveCallback, Respuesta answerParent) {
        this.questions = questions;
        this.context = context;
        this.fm = fm;
        this.isTable = isTable;
        this.cve = cve;
        this.mRecyclerView = recyclerView;
        this.mSurveyComplete = surveyComplete;
        this.mSurveySaveCallback = surveySaveCallback;
        this.mAnswerParent = answerParent;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Preguntas question = mSurveyComplete.getEncuesta().getPreguntas().get(viewType);
            switch (Integer.parseInt(question.getPregunta_tipo())) {
                case (QuestionType.OPEN_ENDED): {
                    View v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_survey_edittext, parent, false);
                    return new ViewHolderEditText(v,question,mSurveySaveCallback);
                }
                case (QuestionType.OPEN_ENDED_NUMBER):{
                    View v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_survey_number,parent,false);
                    return new ViewHolderEditTextNumber(v,question,mSurveySaveCallback);
                }
                case (QuestionType.OPEN_SEARCH):{
                    List<Opciones> values = question.getOpciones();
                    View v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_survey_catalog,parent,false);
                    return new ViewHolderSearch(v,question,values,parent.getContext(),mRecyclerView,mSurveySaveCallback);
                }
                case (QuestionType.GPS):{
                    View v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_survey_gps,parent,false);
                    return new ViewHolderGps(v,question,mMapCallback);
                }
                case (QuestionType.IMAGE): {
                    View v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_survey_image, parent, false);
                    return new ViewHolderImage(v,question,mImageCallback);
                }
                case (QuestionType.MULTI_CHOICE_RADIO): {
                    View v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_survey_radiogroup, parent, false);
                    return new ViewHolderRadIoGroup(v,parent.getContext(),mSurveySaveCallback);
                }
                case (QuestionType.MULTI_CHOICE_CHECK): {
                    View v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_survey_choice_check, parent, false);
                    return new ViewHolderMultiChoiceCheck(v,mSurveySaveCallback);
                }
                case (QuestionType.MULTI_CHOICE_SPINNER): {
                    View v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_suvey_choice_spinner, parent, false);
                    return new ViewHolderMultiChoiceSpinner(v,mSurveySaveCallback);
                }
                case (QuestionType.CARRUSEL):{
                    List<Opciones> values = question.getOpciones();
                    View v = LayoutInflater.from(parent.getContext()).
                            inflate(R.layout.item_survey_carrusel,parent,false);
                     Opciones selectedOption = null;
                    for (int i = 0; i < values.size(); i++) {
                        final Opciones value = values.get(i);

                        try {
                            if (question.getRespuestaDetalle() != null) {
                                try {
                                    int id = Integer.valueOf(question.getRespuestaDetalle().getRespuestacodigo());
                                    if (value.getOpcion_id() == id){
                                        selectedOption = value;
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.d(getClass().getName(), "Error en spinner " + ex.getMessage());
                        }
                    }
                    return new ViewHolderCarrusel(v,mSurveySaveCallback,question,values,selectedOption);
                }
                case (QuestionType.DISCLAIMER): {
                    View v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_survey_text, parent, false);
                    return new ViewHolderText(v);
                }
                default:
                    View v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_suvey_choice_spinner, parent, false);
                    return new ViewHolderMultiChoiceSpinner(v,mSurveySaveCallback);
            }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Preguntas question = questions.get(position);
        switch (Integer.parseInt(question.getPregunta_tipo())) {
            case (QuestionType.OPEN_ENDED): {
                final ViewHolderEditText holderOpenEnded = (ViewHolderEditText) holder;
                holderOpenEnded.editText.setTag(question);
                holderOpenEnded.editText.setVisibility(View.VISIBLE);
                holderOpenEnded.txtRequired.setVisibility(question.getPregunta_obligatoria() == 1 ? View.VISIBLE : View.INVISIBLE);
                int maxLength = question.getTipo_max() != null ? question.getTipo_max() : Constants.MAX_LENGTH_DEFAULT;
                InputFilter[] FilterArray = new InputFilter[1];
                FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                holderOpenEnded.editText.setFilters(FilterArray);
                holderOpenEnded.txtTitle.setText(question.getPregunta_encabezado());
                if (question.getRespuestaDetalle() != null) {
                    holderOpenEnded.editText.setText(Validations.validNotNull(question.getRespuestaDetalle().getRespuestatexto()));
                }
                break;
            }
            case (QuestionType.OPEN_ENDED_NUMBER): {
                final ViewHolderEditTextNumber holderOpenEndedNumber = (ViewHolderEditTextNumber) holder;
                holderOpenEndedNumber.editText.setTag(question);
                holderOpenEndedNumber.editText.setVisibility(View.VISIBLE);
                holderOpenEndedNumber.txtRequired.setVisibility(question.getPregunta_obligatoria() == 1 ? View.VISIBLE : View.INVISIBLE);
                int maxLengthNumber = question.getTipo_max() != null ? question.getTipo_max() : Constants.MAX_LENGTH_DEFAULT;
                InputFilter[] FilterArrayN = new InputFilter[1];
                FilterArrayN[0] = new InputFilter.LengthFilter(maxLengthNumber);
                holderOpenEndedNumber.editText.setFilters(FilterArrayN);
                holderOpenEndedNumber.txtTitle.setText(question.getPregunta_encabezado());
                if (question.getRespuestaDetalle() != null) {
                    holderOpenEndedNumber.editText.setText(Validations.validNotNull(question.getRespuestaDetalle().getRespuestatexto()));
                }
                break;
            }
            case (QuestionType.OPEN_SEARCH): {
                List<Opciones> values = question.getOpciones();

                ViewHolderSearch holderCatalog = (ViewHolderSearch) holder;
                holderCatalog.lvResults.setTag(question);
                holderCatalog.editText.setTag(question);
                holderCatalog.txtTitle.setText(question.getPregunta_encabezado());
                if (question.getRespuestaDetalle() != null) {
                    holderCatalog.searching = false;
                    holderCatalog.editText.setText(Validations.validNotNull(question.getRespuestaDetalle().getRespuestatexto()));
                    holderCatalog.searching = true;
                }else{
                holderCatalog.editText.setText("");
                }

                break;
            }
            case (QuestionType.GPS): {
                ViewHolderGps viewHolderMap = (ViewHolderGps) holder;
                viewHolderMap.titleTxt.setText(question.getPregunta_encabezado());
                viewHolderMap.titleTxt.setTag(question);

                //Valida si ya tiene guaradado una ubicacion
                /*if (question.getRespuestaDetalle() != null) {
                    viewHolderMap.linearLayout.setVisibility(View.VISIBLE);
                }*/
                break;
            }
            case (QuestionType.IMAGE): {
                ViewHolderImage viewHolderImage = (ViewHolderImage) holder;
                viewHolderImage.titleTxt.setText(question.getPregunta_encabezado());
                viewHolderImage.titleTxt.setTag(question);

                if (question.getRespuestaDetalle() != null && !question.getRespuestaDetalle().getRespuestatexto().isEmpty()) {
                    viewHolderImage.captureImg.setBackground(context.getResources().getDrawable(R.drawable.shape_rectangle));
                    viewHolderImage.captureImg.setImageDrawable(context.getResources().getDrawable(R.drawable.camera_on));
                    viewHolderImage.captureImg.setColorFilter(R.color.colorAccent);
                } else {
                    viewHolderImage.captureImg.setBackground(context.getResources().getDrawable(R.drawable.shape_rectangle));
                    viewHolderImage.captureImg.setImageResource(R.drawable.ic_camera);
                    viewHolderImage.captureImg.setColorFilter(R.color.colorAccent);
                }
                break;
            }
            case (QuestionType.MULTI_CHOICE_SPINNER): {
                List<Opciones> valuesSpinner = question.getOpciones();
                ViewHolderMultiChoiceSpinner holderMultiChoiceSpinner = (ViewHolderMultiChoiceSpinner) holder;
                holderMultiChoiceSpinner.mSpinner.setTag(question);

                holderMultiChoiceSpinner.txtTitle.setText(question.getPregunta_encabezado());
                ArrayAdapter<Opciones> valueAdapter = new ArrayAdapter<Opciones>(context,
                        android.R.layout.simple_spinner_item, valuesSpinner);
                valueAdapter.setDropDownViewResource(R.layout.simple_dropdown_item_customized);

                if (valuesSpinner != null && valuesSpinner.size() > 0) {
                    holderMultiChoiceSpinner.mSpinner.setAdapter(valueAdapter);
                    if (question.getRespuestaDetalle() != null) {
                    holderMultiChoiceSpinner.mSpinner.setSelection(holderMultiChoiceSpinner.returnPostion(valuesSpinner,question.getRespuestaDetalle().getRespuestacodigo()));
                    }
                }
                Log.d(getClass().getName(), "Position precharged: " + position);

                break;
            }
            case (QuestionType.MULTI_CHOICE_RADIO): {
                ViewHolderRadIoGroup vh = (ViewHolderRadIoGroup) holder;
                vh.textView.setText(question.getPregunta_encabezado());
                vh.radioGroup.removeAllViews();
                vh.linearContainer.removeAllViews();
                List<Opciones> valuesRadio = question.getOpciones();
                if (valuesRadio != null && valuesRadio.size() > 0) {
                    for (int i = 0; i < valuesRadio.size(); i++) {
                        final Opciones value = valuesRadio.get(i);
                        final RadioButton radioButton = new RadioButton(context);
                        radioButton.setId(value.getOpcion_id().intValue());
                        radioButton.setText(value.getOpcion_contenido());
                        radioButton.setTextSize(18);
                        radioButton.setTag(value);
                        radioButton.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorAccent)));

                        //SET RADIO CHECKED
                        /*if (value.getSeleccionado()) {
                            radioButton.setChecked(true);
                            vh.setCheckedDefault(value, radioButton.isChecked(), question, i, vh, question.getRespuestaDetalle() == null);
                        }*/
                        try {
                                if (question.getRespuestaDetalle() != null) {
                                    try {
                                        int id = Integer.valueOf(question.getRespuestaDetalle().getRespuestacodigo());
                                        if (radioButton.getId() == id) {
                                            radioButton.setChecked(true);
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.d(getClass().getName(), "Error en spinner " + ex.getMessage());
                        }
                        radioButton.setOnCheckedChangeListener(vh.getCheckedChangeListener(radioButton, question, i));
                        vh.radioGroup.addView(radioButton);
                    }
                }

                //PINTA EL RADIO SELECCIONADO

                break;
            }
            case  (QuestionType.MULTI_CHOICE_CHECK): {
                Log.d(TAG, String.valueOf(getItemViewType(position)));
                ViewHolderMultiChoiceCheck vh1 = (ViewHolderMultiChoiceCheck) holder;
                vh1.textView.setText(question.getPregunta_encabezado());
                vh1.linearLayout.removeAllViews();
                List<Opciones> values = question.getOpciones();
                if (values != null && values.size() > 0) {
                    for (int i = 0; i < values.size(); i++) {
                        final Opciones value = values.get(i);

                        CheckBox checkBox = (CheckBox)((LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.check_right_checkbox,null);
                        checkBox.setId(value.getOpcion_id().intValue());
                        checkBox.setText(value.getOpcion_contenido());
                        checkBox.setTag(value);
                        checkBox.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorAccent)));

                        if (question.getRespuestaDetalle() != null) {
                            if (question.getRespuestaDetalle().getRespuestacodigo() >= 0){
                                checkBox.setChecked(true);
                            }else{
                                checkBox.setChecked(false);
                            }
                        }

                        checkBox.setOnCheckedChangeListener(vh1.getCheckedChangeListener(checkBox, question, i));
                        vh1.linearLayout.addView(checkBox);
                    }
                }
                break;
            }
            case (QuestionType.CARRUSEL):{
                Log.d(TAG, String.valueOf(getItemViewType(position)));
                ViewHolderCarrusel viewHolderCarrusel = (ViewHolderCarrusel) holder;
                ((ViewHolderCarrusel) holder).txtTitle.setText(question.getPregunta_encabezado());
                List<Opciones> valuesRadio = question.getOpciones();
                for (int i = 0; i < valuesRadio.size(); i++) {
                    final Opciones value = valuesRadio.get(i);

                    try {
                        if (question.getRespuestaDetalle() != null) {
                            try {
                                int id = Integer.valueOf(question.getRespuestaDetalle().getRespuestacodigo());
                                if (value.getOpcion_id() == id){
                                    viewHolderCarrusel.selectedOption = value;
                                    viewHolderCarrusel.recyclerView.notify();
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Log.d(getClass().getName(), "Error en spinner " + ex.getMessage());
                    }
                }

                break;
            }
            case (QuestionType.DISCLAIMER): {
                ViewHolderText viewHolderDisclaimer = (ViewHolderText) holder;
                viewHolderDisclaimer.textView.setText(question.getPregunta_encabezado());
                break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return questions.size();

    }

    public void setQuestionsList(List<Preguntas> questionsList) {
        this.questions = questionsList;
    }

    public void setmSurveyComplete(SurveyComplete surveyComplete){this.mSurveyComplete = surveyComplete;}

    public void setImageCallback(ImageCallback imageCallback){
        this.mImageCallback = imageCallback;
    }

    public void setmMapCallback(MapCallback mapCallback){
        this.mMapCallback = mapCallback;
    }


}
