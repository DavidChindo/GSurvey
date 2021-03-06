package com.hics.g500.db;

import org.greenrobot.greendao.annotation.*;

import java.util.List;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "PREGUNTAS".
 */
@Entity
public class Preguntas {

    @Id
    private Long pregunta_id;
    private Long encuesta_id;
    private Integer pregunta_obligatoria;
    private String pregunta_encabezado;
    private String pregunta_tipo;
    private Integer tipo_min;
    private Integer tipo_max;
    private String tipo_dato;
    private Integer num_opciones;
    private Integer pregunta_orden;

    // KEEP FIELDS - put your custom fields here
    private List<Opciones> opciones;
    private RespuestaDetalle respuestaDetalle;
    // KEEP FIELDS END

    @Generated
    public Preguntas() {
    }

    public Preguntas(Long pregunta_id) {
        this.pregunta_id = pregunta_id;
    }

    @Generated
    public Preguntas(Long pregunta_id, Long encuesta_id, Integer pregunta_obligatoria, String pregunta_encabezado, String pregunta_tipo, Integer tipo_min, Integer tipo_max, String tipo_dato, Integer num_opciones, Integer pregunta_orden) {
        this.pregunta_id = pregunta_id;
        this.encuesta_id = encuesta_id;
        this.pregunta_obligatoria = pregunta_obligatoria;
        this.pregunta_encabezado = pregunta_encabezado;
        this.pregunta_tipo = pregunta_tipo;
        this.tipo_min = tipo_min;
        this.tipo_max = tipo_max;
        this.tipo_dato = tipo_dato;
        this.num_opciones = num_opciones;
        this.pregunta_orden = pregunta_orden;
    }

    public Long getPregunta_id() {
        return pregunta_id;
    }

    public void setPregunta_id(Long pregunta_id) {
        this.pregunta_id = pregunta_id;
    }

    public Long getEncuesta_id() {
        return encuesta_id;
    }

    public void setEncuesta_id(Long encuesta_id) {
        this.encuesta_id = encuesta_id;
    }

    public Integer getPregunta_obligatoria() {
        return pregunta_obligatoria;
    }

    public void setPregunta_obligatoria(Integer pregunta_obligatoria) {
        this.pregunta_obligatoria = pregunta_obligatoria;
    }

    public String getPregunta_encabezado() {
        return pregunta_encabezado;
    }

    public void setPregunta_encabezado(String pregunta_encabezado) {
        this.pregunta_encabezado = pregunta_encabezado;
    }

    public String getPregunta_tipo() {
        return pregunta_tipo;
    }

    public void setPregunta_tipo(String pregunta_tipo) {
        this.pregunta_tipo = pregunta_tipo;
    }

    public Integer getTipo_min() {
        return tipo_min;
    }

    public void setTipo_min(Integer tipo_min) {
        this.tipo_min = tipo_min;
    }

    public Integer getTipo_max() {
        return tipo_max;
    }

    public void setTipo_max(Integer tipo_max) {
        this.tipo_max = tipo_max;
    }

    public String getTipo_dato() {
        return tipo_dato;
    }

    public void setTipo_dato(String tipo_dato) {
        this.tipo_dato = tipo_dato;
    }

    public Integer getNum_opciones() {
        return num_opciones;
    }

    public void setNum_opciones(Integer num_opciones) {
        this.num_opciones = num_opciones;
    }

    public Integer getPregunta_orden() {
        return pregunta_orden;
    }

    public void setPregunta_orden(Integer pregunta_orden) {
        this.pregunta_orden = pregunta_orden;
    }

    // KEEP METHODS - put your custom methods here

    public List<Opciones> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<Opciones> opciones) {
        this.opciones = opciones;
    }

    public RespuestaDetalle getRespuestaDetalle() {
        return respuestaDetalle;
    }

    public void setRespuestaDetalle(RespuestaDetalle respuestaDetalle) {
        this.respuestaDetalle = respuestaDetalle;
    }
    // KEEP METHODS END

}
