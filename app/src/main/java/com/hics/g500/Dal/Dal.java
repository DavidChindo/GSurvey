package com.hics.g500.Dal;

import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.hics.g500.Dal.Model.Coordinates;
import com.hics.g500.G500App;
import com.hics.g500.Library.LogicUtils;
import com.hics.g500.Library.Validators;
import com.hics.g500.Network.Request.AnswerSync;
import com.hics.g500.Network.Request.SurveySync;
import com.hics.g500.Network.Response.OptionResponse;
import com.hics.g500.Network.Response.QuestionResponse;
import com.hics.g500.Network.Response.SurveyResponse;
import com.hics.g500.SurveyEngine.Enums.QuestionType;
import com.hics.g500.SurveyEngine.Model.SurveyComplete;
import com.hics.g500.db.Encuesta;
import com.hics.g500.db.Gasolineras;
import com.hics.g500.db.GasolinerasDao;
import com.hics.g500.db.Opciones;
import com.hics.g500.db.OpcionesDao;
import com.hics.g500.db.Preguntas;
import com.hics.g500.db.PreguntasDao;
import com.hics.g500.db.Respuesta;
import com.hics.g500.db.RespuestaDao;
import com.hics.g500.db.RespuestaDetalle;
import com.hics.g500.db.RespuestaDetalleDao;
import com.hics.g500.db.User;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by david.barrera on 1/31/18.
 */

public class Dal {

    //regionInserts
    public static User insertUser(Context mContext, User user){
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
                for (Gasolineras gasolinera : gasolinerases){
                    Gasolineras gasolineraTemp = Dal.gasolineraById(gasolinera.getGas_id());
                    if (gasolineraTemp == null){
                        G500App.getDaoSession().getGasolinerasDao().insertOrReplaceInTx(gasolinera);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void insertOrUpdateGasolinera(Gasolineras gasolineras,boolean visited){
        try {
            if (gasolineras != null){
                gasolineras.setVisited(visited);
                gasolineras.setFecha(LogicUtils.getCurrentHour());
                G500App.getDaoSession().getGasolinerasDao().insertOrReplaceInTx(gasolineras);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void updateGasolinera(Gasolineras gasolineras){
        try {
            if (gasolineras != null){
                G500App.getDaoSession().getGasolinerasDao().insertOrReplaceInTx(gasolineras);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static long insertSurvey(Context mContex,SurveyResponse surveyResponse){
        long idEncuesta = -1;
        try {
            deleteSurvey();
            if (surveyResponse != null && surveyResponse.getQuestionResponses() != null && surveyResponse.getQuestionResponses().size() > 0){
                Encuesta encuesta = new Encuesta(new Long(surveyResponse.getSurveyId()),surveyResponse.getName(),surveyResponse.getDescription());
                idEncuesta = G500App.getDaoSession().getEncuestaDao().insertOrReplace(encuesta);
                if (idEncuesta > -1){
                    for (QuestionResponse questionResponse : surveyResponse.getQuestionResponses()){
                        Preguntas preguntas = new Preguntas(new Long(questionResponse.getQuesionId()), idEncuesta, questionResponse.getMandatory(),
                                questionResponse.getTitle(), questionResponse.getQuestionType(), questionResponse.getMinLength(), questionResponse.getMaxLegth(),
                                questionResponse.getDataType(), questionResponse.getOptionesNum(),questionResponse.getOrder());
                        long idQuestion = G500App.getDaoSession().getPreguntasDao().insertOrReplace(preguntas);
                        if (questionResponse.getOptionResponses() != null && questionResponse.getOptionResponses().size() > 0) {
                            for (OptionResponse optionResponse : questionResponse.getOptionResponses()) {
                                Opciones opciones = new Opciones();
                                opciones.setEncuenta_id(idEncuesta);
                                opciones.setPregunta_id(new Long(questionResponse.getQuesionId()));
                                opciones.setOpcion_id(new Long(optionResponse.getOptionId()));
                                opciones.setOpcion_contenido(optionResponse.getOptionDescription());
                                opciones.setOpcion_url(Validators.validateString(optionResponse.getUrl()));
                                G500App.getDaoSession().getOpcionesDao().insertOrReplace(opciones);
                            }
                        }
                    }
                }else{

                }
            }else{
            }
        }catch (Exception e){
            e.printStackTrace();

        }
        return idEncuesta;
    }

    public static void deleteSurvey(){
        try {
            List<Opciones> opciones = G500App.getDaoSession().getOpcionesDao().queryBuilder().list();
            if (opciones != null && opciones.size() > 0){
                G500App.getDaoSession().getOpcionesDao().deleteAll();
            }

            List<Preguntas> preguntases = G500App.getDaoSession().getPreguntasDao().queryBuilder().list();
            if (preguntases != null && preguntases.size() > 0){
                G500App.getDaoSession().getPreguntasDao().deleteAll();
            }

            List<Encuesta> encuestas = G500App.getDaoSession().getEncuestaDao().queryBuilder().list();
            if (encuestas != null && encuestas.size() > 0){
                G500App.getDaoSession().getEncuestaDao().deleteAll();
            }

        }catch (Exception e){
            e.getLocalizedMessage();
        }
    }


    public static Respuesta insertRespuestaParent(long encuesta_id,long gas_id,boolean completada, boolean enviada,String fechaFin,String fechaSync){
        Respuesta respuesta = null;
        try {
            Respuesta answerParent = getAnswerParent(encuesta_id,gas_id);
            if (answerParent == null) {
                Respuesta respuestaTemp = new Respuesta();
                respuestaTemp.setEncuesta_id(encuesta_id);
                respuestaTemp.setGas_id(gas_id);
                respuestaTemp.setEmail(email());
                respuestaTemp.setCompletada(completada);
                respuestaTemp.setEnviada(enviada);
                respuestaTemp.setName_gas(gasolineraById(gas_id).getNombre_gas());
                G500App.getDaoSession().getRespuestaDao().insertOrReplace(respuestaTemp);
                respuesta = getAnswerParent(encuesta_id,gas_id);
                return respuesta;
            }else{
                if (!answerParent.getCompletada()){
                    answerParent.setCompletada(completada);
                    answerParent.setEnviada(enviada);
                    answerParent.setFechaFin(fechaFin);
                    answerParent.setFechaSyn(fechaSync);
                    G500App.getDaoSession().getRespuestaDao().updateInTx(answerParent);
                }
                return answerParent;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static Respuesta updateRespuestaParent(long encuesta_id,long gas_id,boolean completada, boolean enviada,String fechaFin,String fechaSync){
        Respuesta respuesta = null;
        try {
            Respuesta answerParent = getAnswerParent(encuesta_id,gas_id);
            if (answerParent != null) {
                    answerParent.setCompletada(completada);
                    answerParent.setEnviada(enviada);
                    answerParent.setFechaFin(fechaFin);
                    answerParent.setFechaSyn(fechaSync);
                    G500App.getDaoSession().getRespuestaDao().updateInTx(answerParent);
                    Gasolineras gasolineras = gasolineraById(answerParent.getGas_id());
                        if (gasolineras != null) {
                            deleteRoute(gasolineras);
                        }
                return answerParent;
                }else {
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static Respuesta getAnswerParent(long encuestaId, long gasId){
        try {
            QueryBuilder qb = G500App.getDaoSession().getRespuestaDao().queryBuilder();
            List<Respuesta> answerParent = qb.where(qb.and(RespuestaDao.Properties.Encuesta_id.eq(encuestaId),RespuestaDao.Properties.Gas_id.eq(gasId),
                    RespuestaDao.Properties.Email.eq(email()))).list();
            return answerParent != null && answerParent.size() > 0 ? answerParent.get(0) : null;
        }catch (Exception e){
            e.printStackTrace();
            Log.d(Dal.class.getSimpleName(),"Error en getAnswerParent "+e.getMessage());
            return null;
        }
    }

    public static Respuesta getAnsweParentById(long id){
        try {
            List<Respuesta> respuesta = G500App.getDaoSession().getRespuestaDao().queryBuilder().
                    where(RespuestaDao.Properties.Id.eq(id)).list();
            return respuesta != null && respuesta.size() > 0 ? respuesta.get(0) : null;
        }catch (Exception e){
            e.printStackTrace();
            Log.d(Dal.class.getSimpleName(),"Error getAnswerParent: " + e.getMessage());
            return null;
        }
    }

    public static long insertRespuestaDetalle(long idParent,long preguntaId,int tipoId,int respuestaCodigo,String respuestaTexto,RespuestaDetalle respuestaDetalleT){
        try {
            if (respuestaDetalleT != null && respuestaDetalleT.getId() != null && respuestaDetalleT.getId() > 0) {
                RespuestaDetalle answerDetail = getRespuestaDetalleById(idParent,preguntaId);
                if (answerDetail == null){
                    RespuestaDetalle respuestaDetalle = new RespuestaDetalle();
                    respuestaDetalle.setId_parent(idParent);
                    respuestaDetalle.setPregunta_id(preguntaId);
                    respuestaDetalle.setTipo_id(tipoId);
                    respuestaDetalle.setRespuestacodigo(respuestaCodigo);
                    respuestaDetalle.setRespuestatexto(respuestaTexto);
                 return    G500App.getDaoSession().getRespuestaDetalleDao().insertOrReplace(respuestaDetalle);
                }else{
                    answerDetail.setRespuestacodigo(respuestaCodigo);
                    answerDetail.setRespuestatexto(respuestaTexto);
                    G500App.getDaoSession().getRespuestaDetalleDao().updateInTx(answerDetail);
                    return answerDetail.getId();
                }

            }else{
                RespuestaDetalle respuestaDetalle = new RespuestaDetalle();
                respuestaDetalle.setId_parent(idParent);
                respuestaDetalle.setPregunta_id(preguntaId);
                respuestaDetalle.setTipo_id(tipoId);
                respuestaDetalle.setRespuestacodigo(respuestaCodigo);
                respuestaDetalle.setRespuestatexto(respuestaTexto);
              return   G500App.getDaoSession().getRespuestaDetalleDao().insertOrReplace(respuestaDetalle);
            }
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public static long insertRespuestaDetalleMultiOption(boolean isChecked, long idParent,long preguntaId,int tipoId,int respuestaCodigo,RespuestaDetalle respuestaDetalleT){
        try {
            if (respuestaDetalleT != null && respuestaDetalleT.getId() != null && respuestaDetalleT.getId() > 0) {
                if (isChecked) {
                    RespuestaDetalle answerDetail = getRespuestaDetalleByIdCodigo(idParent, preguntaId, respuestaCodigo);
                    if (answerDetail == null) {
                        RespuestaDetalle respuestaDetalle = new RespuestaDetalle();
                        respuestaDetalle.setId_parent(idParent);
                        respuestaDetalle.setPregunta_id(preguntaId);
                        respuestaDetalle.setTipo_id(tipoId);
                        respuestaDetalle.setRespuestacodigo(respuestaCodigo);
                        return  G500App.getDaoSession().getRespuestaDetalleDao().insertOrReplace(respuestaDetalle);
                    } else {
                        if (!isChecked){
                            G500App.getDaoSession().getRespuestaDetalleDao().delete(answerDetail);
                            return -1;
                        }else {
                            answerDetail.setRespuestacodigo(respuestaCodigo);
                            G500App.getDaoSession().getRespuestaDetalleDao().updateInTx(answerDetail);
                            return answerDetail.getId();
                        }
                    }
                }else{
                    RespuestaDetalle answerDetail = getRespuestaDetalleByIdCodigo(idParent, preguntaId, respuestaCodigo);
                    if (answerDetail == null) {
                        G500App.getDaoSession().getRespuestaDetalleDao().delete(answerDetail);
                    }
                    return -1;
                }
            }else{
                RespuestaDetalle respuestaDetalle = new RespuestaDetalle();
                respuestaDetalle.setId_parent(idParent);
                respuestaDetalle.setPregunta_id(preguntaId);
                respuestaDetalle.setTipo_id(tipoId);
                respuestaDetalle.setRespuestacodigo(respuestaCodigo);
                return  G500App.getDaoSession().getRespuestaDetalleDao().insertOrReplace(respuestaDetalle);
            }
        }catch (Exception e){
            e.printStackTrace();
            return  -1;
        }
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

    public static RespuestaDetalle getRespuestaDetalleById(long idParent, long preguntaId){
        try{
            QueryBuilder qb = G500App.getDaoSession().getRespuestaDetalleDao().queryBuilder();
            List<RespuestaDetalle> respuestaDetalles = qb.where(qb.and(RespuestaDetalleDao.Properties.Id_parent.eq(idParent),
                    RespuestaDetalleDao.Properties.Pregunta_id.eq(preguntaId))).list();
            return respuestaDetalles != null && respuestaDetalles.size() > 0 ? respuestaDetalles.get(0) : null;
        }catch (Exception e ){
            e.printStackTrace();
            return  null;
        }
    }

    public static List<RespuestaDetalle> getRespuestaDetalleByIdParent(long idParent){
        try{
            QueryBuilder qb = G500App.getDaoSession().getRespuestaDetalleDao().queryBuilder();
            List<RespuestaDetalle> respuestaDetalles = qb.where(RespuestaDetalleDao.Properties.Id_parent.eq(idParent)).list();
            return respuestaDetalles != null && respuestaDetalles.size() > 0 ? respuestaDetalles : null;
        }catch (Exception e ){
            e.printStackTrace();
            return  null;
        }
    }


    public static RespuestaDetalle getRespuestaDetalleByIdCodigo(long idParent, long preguntaId,int idCodigo){
        try{
            QueryBuilder qb = G500App.getDaoSession().getRespuestaDetalleDao().queryBuilder();
            List<RespuestaDetalle> respuestaDetalles = qb.where(qb.and(RespuestaDetalleDao.Properties.Id_parent.eq(idParent),
                    RespuestaDetalleDao.Properties.Pregunta_id.eq(preguntaId),
                    RespuestaDetalleDao.Properties.Respuestacodigo.eq(idCodigo))).list();
            return respuestaDetalles != null && respuestaDetalles.size() > 0 ? respuestaDetalles.get(0) : null;
        }catch (Exception e ){
            e.printStackTrace();
            return  null;
        }
    }

    public static void deleteAnswer(Respuesta answerParent){
        try {
            List<RespuestaDetalle> respuestaDetalles = getRespuestaDetalleByIdParent(answerParent.getId());
            if (respuestaDetalles != null && respuestaDetalles.size() > 0){
                G500App.getDaoSession().getRespuestaDetalleDao().deleteInTx(respuestaDetalles);
                G500App.getDaoSession().getRespuestaDao().deleteInTx(answerParent);
            }
        }catch (Exception e){
            e.printStackTrace();
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

    public static String email(){
        try {
            User user = user();
            return user != null ? user.getEmail() : "";
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

    public static SurveyComplete surveyComplete(long idParent){
        try {
            SurveyComplete surveyComplete = new SurveyComplete();
            List<Encuesta> encuestas = G500App.getDaoSession().getEncuestaDao().queryBuilder().list();
            if (encuestas != null && encuestas.size() > 0){
                List<Preguntas> preguntas = G500App.getDaoSession().getPreguntasDao().queryBuilder().
                        where(PreguntasDao.Properties.Encuesta_id.eq(encuestas.get(0).getEncuesta_id())).orderAsc(PreguntasDao.Properties.Pregunta_orden).list();
                if (preguntas != null && preguntas.size() > 0){
                    for (Preguntas pregunta : preguntas){
                        List<Opciones> opciones = G500App.getDaoSession().getOpcionesDao().queryBuilder().
                                where(OpcionesDao.Properties.Pregunta_id.eq(pregunta.getPregunta_id())).orderAsc(OpcionesDao.Properties.Opcion_contenido).list();
                        if (opciones != null && opciones.size() > 0 ){
                            pregunta.setOpciones(opciones);
                        }
                        pregunta.setRespuestaDetalle(getRespuestaDetallByIdParentIdQuestion(idParent,pregunta.getPregunta_id()));
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

    public static RespuestaDetalle getRespuestaDetallByIdParentIdQuestion(long idParent, long idQuestion){
        RespuestaDetalle respuestaDetalle = null;
        try {
            QueryBuilder qb = G500App.getDaoSession().getRespuestaDetalleDao().queryBuilder();
            List<RespuestaDetalle> answer = qb.where(qb.and(RespuestaDetalleDao.Properties.Id_parent.eq(idParent),
                    RespuestaDetalleDao.Properties.Pregunta_id.eq(idQuestion))).list();
            respuestaDetalle = answer != null && answer.size() > 0 ? answer.get(0) : null;
        }catch (Exception e){
            e.printStackTrace();
        }
        return respuestaDetalle;
    }

    public static RespuestaDetalle getRespuestaDetalleByIdLong(long id){
        try {
            List<RespuestaDetalle> answer = G500App.getDaoSession().getRespuestaDetalleDao().queryBuilder().
                    where(RespuestaDetalleDao.Properties.Id.eq(id)).list();
            return answer != null && answer.size() > 0 ? answer.get(0) : null;

        }catch (Exception e){
            e.printStackTrace();
            Log.d(Dal.class.getSimpleName(),"Error getRespuestaDetalleByLong "+e.getMessage());
            return null;
        }
    }

    public static long idSurvey(){
        try {
            List<Encuesta> encuestas = G500App.getDaoSession().getEncuestaDao().queryBuilder().list();
            return encuestas != null && encuestas.size() > 0 ? encuestas.get(0).getEncuesta_id() : -1;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public static ArrayList<SurveySync> surveysSync(){
        try {
            ArrayList<SurveySync> surveySyncs = null;
            List<Respuesta> respuestas = G500App.getDaoSession().getRespuestaDao().queryBuilder().where(RespuestaDao.Properties.Completada.eq(true)).list();
            if (respuestas != null && respuestas.size() > 0 ){
                surveySyncs = new ArrayList<>();
                for (Respuesta answerParent : respuestas){
                    SurveySync surveySync = new SurveySync();
                    List<RespuestaDetalle> respuestaDetalles = getRespuestaDetalleByIdParent(answerParent.getId());
                    if (respuestaDetalles != null && respuestaDetalles.size() > 0){
                        ArrayList<AnswerSync> answers = new ArrayList<>();
                        for (RespuestaDetalle answer : respuestaDetalles){
                            AnswerSync answerSync = new AnswerSync(answer.getPregunta_id(),answer.getTipo_id(),answer.getRespuestacodigo(),answer.getRespuestatexto());
                            answers.add(answerSync);
                        }
                        surveySync.setAnswerSyncs(answers);
                        surveySync.setEmail(email());
                        surveySync.setGasolineraId(answerParent.getGas_id());
                        surveySync.setSurveyId(idSurvey());
                        surveySync.setParentId(answerParent.getId());
                        surveySync.setSync(answerParent.getEnviada());
                        Gasolineras gasolineras = gasolineraById(answerParent.getGas_id());
                        if (gasolineras != null) {
                            deleteRoute(gasolineras);
                        }
                        surveySyncs.add(surveySync);

                    }
                }
                return surveySyncs;
            }else{
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d(Dal.class.getSimpleName(),"Error surveysSync "+e.getMessage());
            return null;
        }
    }

    public static SurveySync surveysSyncById(long idRespuesta){
        try {
            Respuesta respuesta = getAnsweParentById(idRespuesta);
            if (respuesta != null ){
                    SurveySync surveySync = new SurveySync();
                    List<RespuestaDetalle> respuestaDetalles = getRespuestaDetalleByIdParent(respuesta.getId());
                    if (respuestaDetalles != null && respuestaDetalles.size() > 0){
                        ArrayList<AnswerSync> answers = new ArrayList<>();
                        for (RespuestaDetalle answer : respuestaDetalles){
                            AnswerSync answerSync = new AnswerSync(answer.getPregunta_id(),answer.getTipo_id(),answer.getRespuestacodigo(),answer.getRespuestatexto());
                            answers.add(answerSync);
                        }
                        surveySync.setAnswerSyncs(answers);
                        surveySync.setEmail(email());
                        surveySync.setGasolineraId(respuesta.getGas_id());
                        surveySync.setSurveyId(idSurvey());
                        surveySync.setParentId(respuesta.getId());
                        surveySync.setSync(respuesta.getEnviada());

                        return surveySync;
                    }else{
                        return null;
                    }
            }else{
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d(Dal.class.getSimpleName(),"Error surveysSync "+e.getMessage());
            return null;
        }
    }

    public static boolean hasPhoto(long idRespuesta){
        boolean hasPhoto = false;
        try {
            Respuesta respuesta = getAnsweParentById(idRespuesta);
            if (respuesta != null){
                List<RespuestaDetalle> respuestaDetalles = getRespuestaDetalleByIdParent(respuesta.getId());
                if (respuestaDetalles != null && respuestaDetalles.size() > 0){
                    for (RespuestaDetalle respuestaDetalle : respuestaDetalles){
                        if (respuestaDetalle.getTipo_id() == QuestionType.IMAGE){
                            return true;

                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d(Dal.class.getSimpleName(),"Error has photo "+e.getMessage());
        }
        return hasPhoto;
    }

    public static Gasolineras gasolineraById(long id){
        try {
            List<Gasolineras> gasolinera = G500App.getDaoSession().getGasolinerasDao().queryBuilder().
                    where(GasolinerasDao.Properties.Gas_id.eq(id)).list();
            return  gasolinera != null && gasolinera.size() > 0 ? gasolinera.get(0) : null;
        }catch (Exception e){
            e.printStackTrace();
            Log.d(Dal.class.getSimpleName(),"Error gasolineraById: "+e.getMessage());
            return null;
        }
    }

    public static ArrayList<String> getFilesPath(long idParent,long idQuestion){
        try {
            QueryBuilder queryBuilder = G500App.getDaoSession().getRespuestaDetalleDao().queryBuilder();
            List<RespuestaDetalle> respuestaDetalles = queryBuilder.where(queryBuilder.and(RespuestaDetalleDao.Properties.Id_parent.eq(idParent),
                    RespuestaDetalleDao.Properties.Tipo_id.eq(QuestionType.IMAGE))).list();
            if (respuestaDetalles != null && respuestaDetalles.size() > 0){
                ArrayList<String> paths = new ArrayList<>();
                for (RespuestaDetalle respuestaDetalle : respuestaDetalles){
                    paths.add(respuestaDetalle.getRespuestatexto());
                }
                return paths;
            }else{
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d(Dal.class.getSimpleName(),"Error en getFilesPath "+e.getMessage());
            return  null;
        }
    }
    //endregion

    //regionDelete
    public static void deleteRoutes(){
        try {
            List<Gasolineras> gasolinerases = G500App.getDaoSession().getGasolinerasDao().queryBuilder().
                    where(GasolinerasDao.Properties.Audio.isNull()).list();
            if (gasolinerases != null && gasolinerases.size() > 0) {
                G500App.getDaoSession().getGasolinerasDao().deleteInTx(gasolinerases);
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d("DAL","Error en deleteRoutes "+e.getMessage());

        }
    }

    public static void deleteRoute(Gasolineras gasolineras){
        try {
            if (gasolineras != null ){
                if (gasolineras.getAudio() != null && !gasolineras.getAudio().isEmpty()){
                    File file = new File(gasolineras.getAudio());
                    if (file.exists()){
                        boolean deleted = file.delete();
                        Log.d("BORRAR ARCHIVO ","Valor "+deleted);
                    }
                }
                G500App.getDaoSession().getGasolinerasDao().deleteInTx(gasolineras);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void deleteUser(){
        try {
            G500App.getDaoSession().getUserDao().deleteAll();
        }catch (Exception e){
            e.getLocalizedMessage();
        }
    }
    //endregion
}
