package br.com.felipemira.ports.out;

import br.com.felipemira.domain.model.Message;

import java.util.Map;

public interface SendMessage {

    boolean isCorrectQueue(String queueName);
    void sendMessage(Message message);

}
