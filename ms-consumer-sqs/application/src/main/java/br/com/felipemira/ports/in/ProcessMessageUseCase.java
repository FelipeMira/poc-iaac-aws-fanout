package br.com.felipemira.ports.in;

import br.com.felipemira.domain.model.Message;

public interface ProcessMessageUseCase {
        void processMessage(Message message);
}
