package com.hics.g500.Dal;

import android.util.Log;

import com.hics.g500.Dal.Model.Coordinates;
import com.hics.g500.G500App;
import com.hics.g500.Network.Response.OptionResponse;
import com.hics.g500.Network.Response.QuestionResponse;
import com.hics.g500.Network.Response.SurveyResponse;
import com.hics.g500.SurveyEngine.Model.SurveyComplete;
import com.hics.g500.db.Encuesta;
import com.hics.g500.db.Gasolineras;
import com.hics.g500.db.Opciones;
import com.hics.g500.db.OpcionesDao;
import com.hics.g500.db.Preguntas;
import com.hics.g500.db.PreguntasDao;
import com.hics.g500.db.User;

import org.greenrobot.greendao.query.QueryBuilder;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by david.barrera on 1/31/18.
 */

public class Dal {

    //regionInserts
    public static User insertUser(User user){
        try {
            if (user != null){
                G500App.getDaoSession().getUserDao().insertOrReplaceInTx(user);
                return user;
            }else{
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void insertGasolineras(ArrayList<Gasolineras> gasolinerases){
        try {
            if (gasolinerases != null && gasolinerases.size() > 0){
                G500App.getDaoSession().getGasolinerasDao().insertOrReplaceInTx(gasolinerases);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static long insertSurvey(SurveyResponse surveyResponse){
        long idEncuesta = -1;
        try {
            if (surveyResponse != null && surveyResponse.getQuestionResponses() != null && surveyResponse.getQuestionResponses().size() > 0){
                Encuesta encuesta = new Encuesta(new Long(surveyResponse.getSurveyId()),surveyResponse.getName(),surveyResponse.getDescription());
                idEncuesta = G500App.getDaoSession().getEncuestaDao().insertOrReplace(encuesta);
                if (idEncuesta > -1){
                    for (QuestionResponse questionResponse : surveyResponse.getQuestionResponses()){
                        Preguntas preguntas = new Preguntas(new Long(questionResponse.getQuesionId()), idEncuesta, questionResponse.getMandatory(),
                                questionResponse.getTitle(), questionResponse.getQuestionType(), questionResponse.getMinLength(), questionResponse.getMaxLegth(),
                                questionResponse.getDataType(), questionResponse.getOptionesNum());
                        long idQuestion = G500App.getDaoSession().getPreguntasDao().insertOrReplace(preguntas);
                        if (questionResponse.getOptionResponses() != null && questionResponse.getOptionResponses().size() > 0) {
                            for (OptionResponse optionResponse : questionResponse.getOptionResponses()) {
                                Opciones opciones = new Opciones();
                                opciones.setEncuenta_id(idEncuesta);
                                opciones.setPregunta_id(new Long(questionResponse.getQuesionId()));
                                opciones.setOpcion_id(new Long(optionResponse.getOptionId()));
                                opciones.setOpcion_contenido(optionResponse.getOptionDescription());
                                G500App.getDaoSession().getOpcionesDao().insertOrReplace(opciones);
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return idEncuesta;
    }
//endregion

    //regionSelect
    public static User user(){
        try{
            List<User> user = G500App.getDaoSession().getUserDao().queryBuilder().list();
            return user != null && user.size() > 0 ? user.get(0) : null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static String token(){
        try {
            User user = user();
            return user != null ? user.getToken() : "";
        }catch (Exception e){
            return "";
        }
    }

    public static List<Gasolineras> gasolinerasAll(){
        try {
            List<Gasolineras> gasolinerasTemp = G500App.getDaoSession().getGasolinerasDao().queryBuilder().list();
            return gasolinerasTemp != null && gasolinerasTemp.size() > 0 ? gasolinerasTemp : null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<Coordinates> positionsGasolineras(){
        try {
            List<Gasolineras> gasolinerasTemp = G500App.getDaoSession().getGasolinerasDao().queryBuilder().list();
            if (gasolinerasTemp != null && gasolinerasTemp.size() > 0 ){
                List<Coordinates> coordinates = new ArrayList<>();
                for (Gasolineras gasolinera : gasolinerasTemp){
                    String[] coordinatesString = gasolinera.getCoordenadas().split(",");
                    coordinates.add(new Coordinates(new Long(coordinatesString[0]),new Long(coordinatesString[1])));
                }
                return coordinates;
            }else{
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static SurveyComplete surveyComplete(){
        try {
            SurveyComplete surveyComplete = new SurveyComplete();
            List<Encuesta> encuestas = G500App.getDaoSession().getEncuestaDao().queryBuilder().list();
            if (encuestas != null && encuestas.size() > 0){
                List<Preguntas> preguntas = G500App.getDaoSession().getPreguntasDao().queryBuilder().
                        where(PreguntasDao.Properties.Encuesta_id.eq(encuestas.get(0).getEncuesta_id())).list();
                if (preguntas != null && preguntas.size() > 0){
                    for (Preguntas pregunta : preguntas){
                        List<Opciones> opciones = G500App.getDaoSession().getOpcionesDao().queryBuilder().
                                where(OpcionesDao.Properties.Pregunta_id.eq(pregunta.getPregunta_id())).list();
                        if (opciones != null && opciones.size() > 0 ){
                            pregunta.setOpciones(opciones);
                        }
                    }
                    encuestas.get(0).setPreguntas(preguntas);
                }
                surveyComplete.setEncuesta(encuestas.get(0));
                return surveyComplete;
            }else{
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
    }

    public static List<Opciones> getValueByName(String name,long xidPregunta){
        try {
            QueryBuilder qb = G500App.getDaoSession().getOpcionesDao().queryBuilder();
            List<Opciones> valueList = qb.where(qb.and(OpcionesDao.Properties.Opcion_contenido.like(name + "%"),
                    OpcionesDao.Properties.Pregunta_id.eq(xidPregunta))).list();

            return valueList != null && valueList.size() > 0 ? valueList : new ArrayList<Opciones>();

        }catch (Exception e){
            e.printStackTrace();
            Log.d("DAL", "Error en getValueByName " + e.getMessage());
            return null;
        }
    }

    //endregion

    //regionDelete
    public static void deleteRoutes(){
        try {
            G500App.getDaoSession().getGasolinerasDao().deleteAll();
        }catch (Exception e){
            e.printStackTrace();
            Log.d("DAL","Error en deleteRoutes "+e.getMessage());

        }
    }
    //endregion
}
