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
  public Flux<DomainEvent> apply(Mono<CrearJuegoCommand> crearJuegoCommand) {
    return listaDeCartaService.obtenerCartasDeMarvel().collectList()
        .flatMapMany(cartaMaestras ->

            crearJuegoCommand.flatMapIterable(comandoCrearJuego ->
                {
                  if (comandoCrearJuego.getJugadores().size() < MIN_JUGADORES ||
                      comandoCrearJuego.getJugadores().size() > MIN_JUGADORES) {
                    throw new BusinessException(comandoCrearJuego.getJuegoId(),
                        "No se puede crear el juego por que no tiene la cantidad requerida de jugadores [Min "
                            + MIN_JUGADORES + " Max " + MAX_JUGADORES);
                  }

                  var cartasJuego = creaCartasJuego(cartaMaestras);
                  var factory = new JugadorFactory();

                  comandoCrearJuego.getJugadores()
                      .forEach((id, alias) -> {
                        var cartasMazoJugador = seleccionaCartasJugador(cartasJuego);
                        if (cartasMazoJugador.size() < CARTAS_POR_MAZO) {
                          throw new BusinessException(comandoCrearJuego.getJuegoId(), "No hay cartas suficientes");
                        }
                        cartasMazoJugador.forEach(carta ->
                            cartasJuego.removeIf(c ->
                                c.value().cartaId().equals(carta.value().cartaId())));
                        factory.agregarJugador(JugadorId.of(id), alias, new Mazo(cartasMazoJugador));
                      });
                  var juego = new Juego(JuegoId.of(comandoCrearJuego.getJuegoId()),
                      JugadorId.of(comandoCrearJuego.getJugadorPrincipalId()), factory);
                  return juego.getUncommittedChanges();
                }
            ));
  }

  private Set<Carta> seleccionaCartasJugador(List<Carta> cartasJuego) {
    Collections.shuffle(cartasJuego);
    return cartasJuego.stream()
        .limit(CARTAS_POR_MAZO)
        .collect(Collectors.toSet());
  }

  private List<Carta> creaCartasJuego(List<CartaMaestra> cartasMaestras) {
    return cartasMaestras.stream()
        .map(cartaMaestra ->
            new Carta(CartaMaestraId.of(cartaMaestra.getId())
                , cartaMaestra.getPoder(), true, true))
        .collect(Collectors.toList());
  }

}
