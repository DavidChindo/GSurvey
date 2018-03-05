package com.example;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

import sun.security.krb5.SCDynamicStoreConfig;

public class MyGenerator {

    public static void main(String[] args) {
        Schema schema = new Schema(1, "com.hics.g500.db"); // Your app package name and the (.db) is the folder where the DAO files will be generated into.
        schema.enableKeepSectionsByDefault();

        addTables(schema);

        try {
            new DaoGenerator().generateAll(schema,"./app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTables(final Schema schema) {
        addUserEntities(schema);
        addGasolinerasEntities(schema);
        addSurvey(schema);
        addQuestions(schema);
        addOptions(schema);
        addAnswers(schema);
        addAnswersDetail(schema);

    }

    private static Entity addUserEntities(final Schema schema) {
        Entity user = schema.addEntity("User");
        user.addStringProperty("email").primaryKey().notNull();
        user.addStringProperty("token");
        return user;
    }

    private static Entity addGasolinerasEntities(final Schema schema){
        Entity gasolinera = schema.addEntity("Gasolineras");
        gasolinera.addLongProperty("gas_id").primaryKey();
        gasolinera.addStringProperty("nombre_gas");
        gasolinera.addStringProperty("coordenadas");
        gasolinera.addStringProperty("direccion");
        gasolinera.addBooleanProperty("visited");
        gasolinera.addStringProperty("fecha");
        gasolinera.addStringProperty("audio");

        return gasolinera;
    }

    private static Entity addSurvey(final Schema schema){
        Entity survey = schema.addEntity("Encuesta");
        survey.addLongProperty("encuesta_id").primaryKey();
        survey.addStringProperty("encuesta_nombre");
        survey.addStringProperty("encuesta_desc");

        return survey;
    }

    private static Entity addQuestions(final Schema schema){
        Entity question = schema.addEntity("Preguntas");
        question.addLongProperty("pregunta_id").primaryKey();
        question.addLongProperty("encuesta_id");
        question.addIntProperty("pregunta_obligatoria");
        question.addStringProperty("pregunta_encabezado");
        question.addStringProperty("pregunta_tipo");
        question.addIntProperty("tipo_min");
        question.addIntProperty("tipo_max");
        question.addStringProperty("tipo_dato");
        question.addIntProperty("num_opciones");

        return question;
    }

    private static Entity addOptions(final Schema schema){
        Entity option = schema.addEntity("Opciones");
        option.addLongProperty("pregunta_id");
        option.addLongProperty("encuenta_id");
        option.addLongProperty("opcion_id").primaryKey();
        option.addStringProperty("opcion_contenido");
        option.addStringProperty("opcion_url");

        return option;
    }

    private static Entity addAnswers(final Schema schema){
        Entity answer = schema.addEntity("Respuesta");
        answer.addLongProperty("id").primaryKey().autoincrement();
        answer.addLongProperty("encuesta_id");
        answer.addLongProperty("gas_id");
        answer.addStringProperty("name_gas");
        answer.addStringProperty("email");
        answer.addBooleanProperty("completada");
        answer.addBooleanProperty("enviada");
        answer.addStringProperty("ticket");
        answer.addStringProperty("fechaFin");
        answer.addStringProperty("fechaSyn");

        return answer;

    }

    private static Entity addAnswersDetail(final Schema schema){
        Entity answerDetail = schema.addEntity("RespuestaDetalle");
        answerDetail.addLongProperty("id").primaryKey().autoincrement();
        answerDetail.addLongProperty("pregunta_id");
        answerDetail.addIntProperty("tipo_id");
        answerDetail.addIntProperty("respuestacodigo");
        answerDetail.addStringProperty("respuestatexto");
        answerDetail.addLongProperty("id_parent");

        return answerDetail;
    }

}
