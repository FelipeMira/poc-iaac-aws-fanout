package br.com.felipemira.ports.out;

import br.com.felipemira.domain.model.Message;

public interface SendMessage {

    void sendMessage(Message message);
}
