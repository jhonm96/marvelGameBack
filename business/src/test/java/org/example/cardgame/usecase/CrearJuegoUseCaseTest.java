package org.example.cardgame.usecase;

import org.example.cardgame.command.CrearJuegoCommand;
import org.example.cardgame.events.JuegoCreado;
import org.example.cardgame.events.JugadorAgregado;
import org.example.cardgame.gateway.ListaDeCartaService;
import org.example.cardgame.gateway.model.CartaMaestra;
import org.example.cardgame.values.JuegoId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CrearJuegoUseCaseTest {

    @Mock
    private ListaDeCartaService service;

    @InjectMocks
    private CrearJuegoUseCase useCase;

    @Test
    void crearJuegoHappyPass(){
        //Arange
        var juegoId = JuegoId.of("JuegoId-001");
        var jugadores = new HashMap<String,String>();
        jugadores.put("jugador-001","Prueba #1");
        jugadores.put("jugador-002","Prueba #2");
        var comando = new CrearJuegoCommand(juegoId.value(),jugadores,"jugador-001");

        when(service.obtenerCartasDeMarvel()).thenReturn(cartasJuego());

        StepVerifier.create(useCase.apply(Mono.just(comando)))
                .expectNextMatches(eventoDominio->{
                    var evento = (JuegoCreado) eventoDominio;
                    return "JuegoId-001".equals(evento.aggregateRootId())
                            && "jugador-001".equals(evento.getJugadorPrincipal().value());
                })
                .expectNextMatches(eventoDomio->{
                    var evento = (JugadorAgregado) eventoDomio;
                    return "jugador-001".equals(evento.getJugadorId().value())
                            && "Prueba #1".equals(evento.getAlias());
                })
                .expectNextMatches(eventoDomio->{
                    var evento = (JugadorAgregado) eventoDomio;
                    return "jugador-002".equals(evento.getJugadorId().value())
                            && "Prueba #2".equals(evento.getAlias());
                })
                .expectComplete()
                .verify();

    }

    private Flux<CartaMaestra> cartasJuego() {

        return Flux.just(
                new CartaMaestra("carta-001","prueba #1"),
                new CartaMaestra("carta-002","prueba #2"),
                new CartaMaestra("carta-003","prueba #3"),
                new CartaMaestra("carta-004","prueba #5"),
                new CartaMaestra("carta-006","prueba #6"),
                new CartaMaestra("carta-007","prueba #7"),
                new CartaMaestra("carta-008","prueba #8"),
                new CartaMaestra("carta-009","prueba #9"),
                new CartaMaestra("carta-010","prueba #10")

        );
    }

}