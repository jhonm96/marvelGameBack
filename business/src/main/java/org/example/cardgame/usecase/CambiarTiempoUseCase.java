package org.example.cardgame.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import org.example.cardgame.Juego;
import org.example.cardgame.command.CambiarTiempoCommand;
import org.example.cardgame.gateway.JuegoDomainEventRepository;
import org.example.cardgame.values.JuegoId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CambiarTiempoUseCase extends UseCaseForCommand<CambiarTiempoCommand> {

  private final JuegoDomainEventRepository repository;

  public CambiarTiempoUseCase(JuegoDomainEventRepository repository) {
    this.repository = repository;
  }

  @Override
  public Flux<DomainEvent> apply(Mono<CambiarTiempoCommand> cambiarTiempoCommandMono) {
    return cambiarTiempoCommandMono.flatMapMany(comando ->
        repository.obtenerEventosPor(comando.getJuegoId().value())
            .collectList()
            .flatMapIterable(evento -> {
              var juego = Juego
                  .from(JuegoId.of(comando.getJuegoId().value()), evento);

              juego.cambiarTiempoDelTablero(comando.getTableroId(), comando.getTiempo());
              return juego.getUncommittedChanges();
            }));
  }
}
