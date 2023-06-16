package com.ccsw.tutorial.prestamo.model;

import java.util.Date;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.game.model.Game;
import com.fasterxml.jackson.annotation.JsonFormat;

public class PrestamoDto {

    private Long id;

    private Game game;

    private Client client;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date startdate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date enddate;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

}
