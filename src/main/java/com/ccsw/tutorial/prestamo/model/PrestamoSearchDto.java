package com.ccsw.tutorial.prestamo.model;

import java.util.Date;

import com.ccsw.tutorial.common.pagination.PageableRequest;

public class PrestamoSearchDto {

    private Long idClient;
    private Long idGame;
    private Date fecha;
    private PageableRequest pageable;

    public Long getIdClient() {
        return idClient;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    public Long getIdGame() {
        return idGame;
    }

    public void setIdGame(Long idGame) {
        this.idGame = idGame;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public PageableRequest getPageable() {
        return pageable;
    }

    public void setPageable(PageableRequest pageable) {
        this.pageable = pageable;
    }

    @Override
    public String toString() {
        return "PrestamoSearchDto [idClient=" + idClient + ", idGame=" + idGame + ", fecha=" + fecha + ", pageable="
                + pageable + "]";
    }

}
