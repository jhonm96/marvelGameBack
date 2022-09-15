package org.example.cardgame.command;

import co.com.sofka.domain.generic.Command;

public class EliminarJuegoCommand extends Command {
    private String juegoId;
    private String jugadorid;

    public EliminarJuegoCommand(String juegoId, String jugadorid) {
        this.juegoId = juegoId;
        this.jugadorid = jugadorid;
    }

    public EliminarJuegoCommand() {
    }

    public String getJuegoId() {
        return juegoId;
    }

    public String getJugadorid() {
        return jugadorid;
    }

    public void setJuegoId(String juegoId) {
        this.juegoId = juegoId;
    }

    public void setJugadorid(String jugadorid) {
        this.jugadorid = jugadorid;
    }
}
