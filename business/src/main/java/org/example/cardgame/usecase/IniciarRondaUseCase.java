package org.example.cardgame.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import org.example.cardgame.Juego;
import org.example.cardgame.command.IniciarRondaCommand;
import org.example.cardgame.gateway.JuegoDomainEventRepository;
import org.example.cardgame.values.JuegoId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class
IniciarRondaUseCase extends UseCaseForCommand<IniciarRondaCommand> {

  private final JuegoDomainEventRepository repository;

  public IniciarRondaUseCase(JuegoDomainEventRepository repository) {
    this.repository = repository;
  }

  @Override
  public Flux<DomainEvent> apply(Mono<IniciarRondaCommand> iniciarRondaCommand) {
    return iniciarRondaCommand.flatMapMany(comando -> repository.obtenerEventosPor(
        comando.getJuegoId()).collectList().flatMapIterable(evento -> {
      var juego = Juego.from(JuegoId.of(comando.getJuegoId()), evento);
      juego.iniciarRonda();
      return juego.getUncommittedChanges();
    }));
  }
}
