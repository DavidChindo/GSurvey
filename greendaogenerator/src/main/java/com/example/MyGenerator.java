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

    }

    private static Entity addUserEntities(final Schema schema) {
        Entity user = schema.addEntity("User");
        user.addIntProperty("email").primaryKey().notNull();
        user.addStringProperty("token");
        return user;
    }

    private static Entity addGasolinerasEntities(final Schema schema){
        Entity gasolinera = schema.addEntity("Gasolineras");
        gasolinera.addIntProperty("gas_id").primaryKey();
        gasolinera.addStringProperty("nombre_gas");
        gasolinera.addStringProperty("coordenadas");
        gasolinera.addStringProperty("direccion");
        gasolinera.addBooleanProperty("visited");
        gasolinera.addStringProperty("fecha");

        return gasolinera;
    }

    private static Entity addSurvey(final Schema schema){
        Entity survey = schema.addEntity("Encuesta");
        survey.addIntProperty("encuesta_id").primaryKey();
        survey.addStringProperty("encuesta_nombre");
        survey.addStringProperty("encuesta_desc");

        return survey;
    }

    private static Entity addQuestions(final Schema schema){
        Entity question = schema.addEntity("Preguntas");
        question.addLongProperty("pregunta_id").primaryKey().autoincrement();
        question.addIntProperty("encuesta_id");
        question.addStringProperty("pregunta_obligatoria");
        question.addStringProperty("pregunta_encabezado");
        question.addStringProperty("pregunta_tipo");
        question.addIntProperty("tipo_min");
        question.addIntProperty("tipo_max");
        question.addStringProperty("tipo_dato");

        return question;
    }

    private static Entity addOptions(final Schema schema){
        Entity option = schema.addEntity("Opciones");
        option.addLongProperty("id").primaryKey().autoincrement();
        option.addIntProperty("pregunta_id");
        option.addIntProperty("encuenta_id");
        option.addIntProperty("opcion_id");
        option.addStringProperty("opcion_contenido");

        return option;
    }

}
