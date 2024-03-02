package br.com.felipemira.ports.in;

import br.com.felipemira.domain.model.Message;

import java.util.Map;

public interface ProcessMessageUseCase {
        void processMessage(Message message);
}
