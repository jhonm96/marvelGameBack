package org.example.cardgame.application.handle.model;

import java.util.Objects;
import java.util.Set;

public class MazoViewModel {
    private Integer cantidad;
    private Set<Carta> cartas;

    public void setCartas(Set<Carta> cartas) {
        this.cartas = cartas;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Set<Carta> getCartas() {
        return cartas;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public static class Carta {
        private  String cartaId;
        private  String jugadorId;
        private  Boolean estaOculta;
        private  Boolean estaHabilitada;
        private  Integer poder;
        private  String nombre;
        private  String uri;

        public String getCartaId() {
            return cartaId;
        }

        public String getNombre() {
            return nombre;
        }

        public String getUri() {
            return uri;
        }

        public void setCartaId(String cartaId) {
            this.cartaId = cartaId;
        }

        public Boolean getEstaOculta() {
            return estaOculta;
        }

        public void setEstaOculta(Boolean estaOculta) {
            this.estaOculta = estaOculta;
        }

        public Boolean getEstaHabilitada() {
            return estaHabilitada;
        }

        public void setPoder(Integer poder) {
            this.poder = poder;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public Integer getPoder() {
            return poder;
        }

        public void setEstaHabilitada(Boolean estaHabilitada) {
            this.estaHabilitada = estaHabilitada;
        }

        public String getJugadorId() {
            return jugadorId;
        }

        public void setJugadorId(String jugadorId) {
            this.jugadorId = jugadorId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Carta carta = (Carta) o;
            return Objects.equals(cartaId, carta.cartaId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(cartaId);
        }
    }
}