package org.example.cardgame.usecase;

import co.com.sofka.domain.generic.DomainEvent;

import org.example.cardgame.Juego;
import org.example.cardgame.events.RondaTerminada;
import org.example.cardgame.gateway.JuegoDomainEventRepository;
import org.example.cardgame.values.JuegoId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Objects;

public class CrearRondaUseCase extends UseCaseForEvent<RondaTerminada> {

    private final JuegoDomainEventRepository repository;

    public CrearRondaUseCase(JuegoDomainEventRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flux<DomainEvent> apply(Mono<RondaTerminada> rondaTerminada) {
        return rondaTerminada.flatMapMany((event) -> repository
                .obtenerEventosPor(event.aggregateRootId())
                .collectList()
                .flatMapIterable(events -> {
                    var juego = Juego.from(JuegoId.of(event.aggregateRootId()), events);
                    var jugadores = new HashSet<>(event.getJugadorIds());
                    var ronda = juego.ronda();
                    if(Objects.isNull(ronda)){
                        throw new IllegalArgumentException("Debe existir la primera ronda");
                    }
                    juego.crearRonda(ronda.incrementarRonda(jugadores), 60);
                    return juego.getUncommittedChanges();
                }));
    }
}
