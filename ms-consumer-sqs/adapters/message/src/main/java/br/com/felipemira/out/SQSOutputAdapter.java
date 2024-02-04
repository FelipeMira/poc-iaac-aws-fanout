package br.com.felipemira.out;

import br.com.felipemira.domain.model.Message;
import br.com.felipemira.ports.out.SendMessage;
import org.springframework.stereotype.Component;


@Component
public class SQSOutputAdapter implements SendMessage {
    @Override
    public void sendMessage(Message message) {
        System.out.println("Sending message: " + message.getText());
    }
}
