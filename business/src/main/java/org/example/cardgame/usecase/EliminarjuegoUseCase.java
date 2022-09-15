package org.example.cardgame.usecase;

import co.com.sofka.business.support.RequestCommand;
import co.com.sofka.business.support.ResponseEvents;
import co.com.sofka.domain.generic.DomainEvent;
import org.example.cardgame.command.CrearJuegoCommand;
import org.example.cardgame.command.EliminarJuegoCommand;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class EliminarjuegoUseCase extends UseCaseForCommand<EliminarJuegoCommand> {


    @Override
    public Flux<DomainEvent> apply(Mono<EliminarJuegoCommand> eliminarJuegoCommandMono) {
        return null;
    }
}
   /* @Override
    public void executeUseCase(RequestCommand<EliminarJuegoCommand> eliminarMaquinaRequestCommand) {
        var command = eliminarMaquinaRequestCommand.getCommand();

        var sede = Sede.from(command.getSedeId(), repository().getEventsBy(command.getSedeId().value()));

        sede.eliminarMaquina(command.getMaquinaId());

        emit().onResponse(new ResponseEvents(sede.getUncommittedChanges()));
    }
}*/