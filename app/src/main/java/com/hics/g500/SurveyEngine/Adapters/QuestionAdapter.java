package com.hics.g500.SurveyEngine.Adapters;

import android.app.Activity;
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
import com.hics.g500.SurveyEngine.ViewHolders.ViewHolderTime;
import com.hics.g500.db.Opciones;
import com.hics.g500.db.Preguntas;
import com.hics.g500.db.Respuesta;

import java.util.ArrayList;
import java.util.List;

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
                    return new ViewHolderEditTextNumber(v,question);
                }
                case (QuestionType.OPEN_SEARCH):{
                    List<Opciones> values = question.getOpciones();
                    View v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_survey_catalog,parent,false);
                    return new ViewHolderSearch(v,question,values,parent.getContext(),mRecyclerView);
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
                    return new ViewHolderRadIoGroup(v,parent.getContext());
                }
                case (QuestionType.MULTI_CHOICE_CHECK): {
                    View v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_survey_choice_check, parent, false);
                    return new ViewHolderMultiChoiceCheck(v);
                }
                case (QuestionType.MULTI_CHOICE_SPINNER): {
                    View v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_suvey_choice_spinner, parent, false);
                    return new ViewHolderMultiChoiceSpinner(v);
                }

                default:
                    View v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_suvey_choice_spinner, parent, false);
                    return new ViewHolderMultiChoiceSpinner(v);
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
               /* if (question.getRespuestadetalle() != null) {
                    holderOpenEnded.editText.setText(Validations.validNotNull(question.getRespuestadetalle().getRespuestacodigo()));
                    //AQUI VIENE LA VALIDACION PARA LA RESPUESTA
                }*/
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
               /* if (question.getRespuestadetalle() != null) {
                    holderOpenEnded.editText.setText(Validations.validNotNull(question.getRespuestadetalle().getRespuestacodigo()));
                    //AQUI VIENE LA VALIDACION PARA LA RESPUESTA
                }*/
                break;
            }
            case (QuestionType.OPEN_SEARCH): {
                List<Opciones> values = question.getOpciones();

                ViewHolderSearch holderCatalog = (ViewHolderSearch) holder;
                holderCatalog.lvResults.setTag(question);
                holderCatalog.editText.setTag(question);
                holderCatalog.txtTitle.setText(question.getPregunta_encabezado());

                /*if (question.getRespuestadetalle() != null) {   AQUI LLEVA LA VALIDACION DE LA RESPUESTA
                    holderCatalog.searching = false;
                    holderCatalog.editText.setText(Validations.validNotNull(question.getRespuestadetalle().getRespuestacodigo()));
                    holderCatalog.searching = true;
                }else{*/
                holderCatalog.editText.setText("");
                //}

                break;
            }
            case (QuestionType.GPS): {
                ViewHolderGps viewHolderMap = (ViewHolderGps) holder;
                viewHolderMap.titleTxt.setText(question.getPregunta_encabezado());
                viewHolderMap.titleTxt.setTag(question);

                //Valida si ya tiene guaradado una ubicacion
                /*if (question.getRespuestadetalle() != null) {
                    viewHolderMap.linearLayout.setVisibility(View.VISIBLE);
                }*/
                break;
            }
            case (QuestionType.IMAGE): {
                ViewHolderImage viewHolderImage = (ViewHolderImage) holder;
                viewHolderImage.titleTxt.setText(question.getPregunta_encabezado());
                viewHolderImage.titleTxt.setTag(question);
                /* AQUI VALIDA SI TIENE RESPUESTA
                if (question.getRespuestadetalle() != null && !question.getRespuestadetalle().getRespuestavalor().isEmpty()) {
                    viewHolderImage.linearLayout.setVisibility(View.VISIBLE);
                } else {
                    viewHolderImage.linearLayout.setVisibility(View.GONE);
                }*/
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
                    //if (question.getRespuestadetalle() != null) {
                    //AQUI SE RELLENA LA RESPUESTA
                    //holderMultiChoiceSpinner.mSpinner.setSelection(Integer.valueOf(question.getRespuestadetalle().getRespuestacodigo()) );
                    //}
                }
                            /*holderMultiChoiceSpinner.mSpinner.setHint(question.getTitulo());
                            holderMultiChoiceSpinner.mSpinner.setBaseColor(context.getResources()
                                    .getColor(R.color.primary_dark));
                            holderMultiChoiceSpinner.spinner.setFloatingLabelText(question.getTitulo());*/
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
                        radioButton.setId(View.generateViewId());
                        radioButton.setText(value.getOpcion_contenido());
                        radioButton.setTag(value);

                        //SET RADIO CHECKED
                        /*if (value.getSeleccionado()) {
                            radioButton.setChecked(true);
                            vh.setCheckedDefault(value, radioButton.isChecked(), question, i, vh, question.getRespuestadetalle() == null);
                        }*/
                        radioButton.setOnCheckedChangeListener(vh.getCheckedChangeListener(radioButton, question, i));
                        vh.radioGroup.addView(radioButton);
                    }
                }

                //PINTA EL RADIO SELECCIONADO
               /* try {
                    List<Opciones> OpcionesRadio = new ArrayList<Opciones>(); //GreenDao.getOpciones(question.getXid()); hacer consulta para traer opciones
                    if (OpcionesRadio != null && OpcionesRadio.size() > 0) {
                        if (question.getRespuestadetalle() != null) {
                            try {
                                int id = Integer.valueOf(question.getRespuestadetalle().getRespuestacodigo());
                                RadioButton selectedRadio = (RadioButton) vh.radioGroup.getChildAt(id);
                                if (id >= 0) {
                                    vh.radioGroup.check(selectedRadio.getId());
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Log.d(getClass().getName(), "Error en spinner " + ex.getMessage());
                }*/
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
                        final CheckBox checkBox = new CheckBox(context);
                        checkBox.setId(View.generateViewId());
                        checkBox.setText(value.getOpcion_contenido());
                        checkBox.setTag(value);

                        //CHECK BOX guardado
                       /* if (value.getSeleccionado()) {
                            checkBox.setChecked(true);
                        }*/
                        checkBox.setOnCheckedChangeListener(vh1.getCheckedChangeListener(checkBox, question, i));
                        vh1.linearLayout.addView(checkBox);
                    }
                }
                
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
