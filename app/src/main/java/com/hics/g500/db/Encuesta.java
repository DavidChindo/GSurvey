package com.hics.g500.db;

import org.greenrobot.greendao.annotation.*;

import java.util.List;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "ENCUESTA".
 */
@Entity
public class Encuesta {

    @Id
    private Long encuesta_id;
    private String encuesta_nombre;
    private String encuesta_desc;

    // KEEP FIELDS - put your custom fields here
    private List<Preguntas> preguntas;
    // KEEP FIELDS END

    @Generated
    public Encuesta() {
    }

    public Encuesta(Long encuesta_id) {
        this.encuesta_id = encuesta_id;
    }

    @Generated
    public Encuesta(Long encuesta_id, String encuesta_nombre, String encuesta_desc) {
        this.encuesta_id = encuesta_id;
        this.encuesta_nombre = encuesta_nombre;
        this.encuesta_desc = encuesta_desc;
    }

    public Long getEncuesta_id() {
        return encuesta_id;
    }

    public void setEncuesta_id(Long encuesta_id) {
        this.encuesta_id = encuesta_id;
    }

    public String getEncuesta_nombre() {
        return encuesta_nombre;
    }

    public void setEncuesta_nombre(String encuesta_nombre) {
        this.encuesta_nombre = encuesta_nombre;
    }

    public String getEncuesta_desc() {
        return encuesta_desc;
    }

    public void setEncuesta_desc(String encuesta_desc) {
        this.encuesta_desc = encuesta_desc;
    }

    // KEEP METHODS - put your custom methods here

    public List<Preguntas> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<Preguntas> preguntas) {
        this.preguntas = preguntas;
    }
    // KEEP METHODS END

}
