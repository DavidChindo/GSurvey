package com.example;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

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

    }

    // This is use to describe the colums of your table
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

}
