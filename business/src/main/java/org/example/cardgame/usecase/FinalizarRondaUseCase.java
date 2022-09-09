package org.example.cardgame.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import org.example.cardgame.Juego;
import org.example.cardgame.command.FinalizarRondaCommand;
import org.example.cardgame.gateway.JuegoDomainEventRepository;
import org.example.cardgame.values.JuegoId;
import org.example.cardgame.values.JugadorId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FinalizarRondaUseCase extends UseCaseForCommand<FinalizarRondaCommand> {
private final JuegoDomainEventRepository repository;

  public FinalizarRondaUseCase(JuegoDomainEventRepository repository) {
    this.repository = repository;
  }

  @Override
  public Flux<DomainEvent> apply(Mono<FinalizarRondaCommand> finalizarRondaCommandMono) {
    return finalizarRondaCommandMono.flatMapMany(comando->
        repository.obtenerEventosPor(comando.getJuegoId())
            .collectList()
            .flatMapIterable(event->{
              var juego = Juego.from(JuegoId.of(comando.getJuegoId()),event);

              return juego.getUncommittedChanges();
            })

        );
  }
}
