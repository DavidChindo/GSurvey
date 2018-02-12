package com.hics.g500.db;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "RESPUESTA".
 */
@Entity
public class Respuesta {

    @Id(autoincrement = true)
    private Long id;
    private Long encuesta_id;
    private Long gas_id;
    private String email;
    private Boolean completada;
    private Boolean enviada;
    private String ticket;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    @Generated
    public Respuesta() {
    }

    public Respuesta(Long id) {
        this.id = id;
    }

    @Generated
    public Respuesta(Long id, Long encuesta_id, Long gas_id, String email, Boolean completada, Boolean enviada, String ticket) {
        this.id = id;
        this.encuesta_id = encuesta_id;
        this.gas_id = gas_id;
        this.email = email;
        this.completada = completada;
        this.enviada = enviada;
        this.ticket = ticket;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEncuesta_id() {
        return encuesta_id;
    }

    public void setEncuesta_id(Long encuesta_id) {
        this.encuesta_id = encuesta_id;
    }

    public Long getGas_id() {
        return gas_id;
    }

    public void setGas_id(Long gas_id) {
        this.gas_id = gas_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getCompletada() {
        return completada;
    }

    public void setCompletada(Boolean completada) {
        this.completada = completada;
    }

    public Boolean getEnviada() {
        return enviada;
    }

    public void setEnviada(Boolean enviada) {
        this.enviada = enviada;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
