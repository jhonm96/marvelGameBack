package org.example.cardgame.usecase;

import co.com.sofka.business.generic.BusinessException;
import co.com.sofka.domain.generic.DomainEvent;
import org.example.cardgame.Juego;
import org.example.cardgame.JugadorFactory;
import org.example.cardgame.command.CrearJuegoCommand;
import org.example.cardgame.gateway.ListaDeCartaService;
import org.example.cardgame.gateway.model.CartaMaestra;
import org.example.cardgame.values.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CrearJuegoUseCase extends UseCaseForCommand<CrearJuegoCommand> {

  private final ListaDeCartaService listaDeCartaService;
  private final Integer CARTAS_POR_MAZO = 5;

  private final Integer MIN_JUGADORES = 2;

  private final Integer MAX_JUGADORES = 6;

  public CrearJuegoUseCase(ListaDeCartaService listaDeCartaService) {
    this.listaDeCartaService = listaDeCartaService;
  }

    @Override
    public Flux<DomainEvent> apply(Mono<CrearJuegoCommand> input) {
        return listaDeCartaService.obtenerCartasDeMarvel().collectList()
                .flatMapMany(cartas -> input.flatMapIterable(command -> {
                    var factory = new JugadorFactory();
                    command.getJugadores()
                            .forEach((id, alias) ->
                                    factory.agregarJugador(JugadorId.of(id), alias, generarMazo(cartas))
                            );
                    var juego = new Juego(JuegoId.of(command.getJuegoId()), JugadorId.of(command.getJugadorPrincipalId()), factory);
                    return juego.getUncommittedChanges();
                }));

    }

    private Mazo generarMazo(List<CartaMaestra> cartas) {
        Collections.shuffle(cartas);
        var lista = cartas.stream().limit(2)
                .map(carta -> new Carta(CartaMaestraId.of(carta.getId()), carta.getPoder(), false, true))
                .collect(Collectors.toList());
        cartas.removeIf(cartaMaestra -> lista.stream().anyMatch(carta -> {
            var id = carta.value().cartaId().value();
            return cartaMaestra.getId().equals(id);
        }));
        return new Mazo(new HashSet<>(lista));
    }


}