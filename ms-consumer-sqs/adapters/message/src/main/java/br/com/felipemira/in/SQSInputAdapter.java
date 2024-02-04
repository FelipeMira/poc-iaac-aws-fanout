package br.com.felipemira.in;

import br.com.felipemira.in.mappers.MessageMapper;
import br.com.felipemira.ports.in.ProcessMessageUseCase;

import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement;
import br.com.felipemira.domain.model.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Map;

// Adapter de entrada
@Service
public class SQSInputAdapter {
    private final ProcessMessageUseCase processMessage;
    private final MessageMapper messageMapper;

    public SQSInputAdapter(ProcessMessageUseCase processMessage, MessageMapper messageMapper) {
        this.processMessage = processMessage;
        this.messageMapper = messageMapper;
    }

    @SqsListener(value = "sqs-alerts-env-dev")
    public void listenToSqsQueue(@Payload org.springframework.messaging.Message<?> message, @Headers Map<String, Object> headers) {
        System.out.println("Mensagem recebida: " + message.getPayload());

        Message domainMessage = messageMapper.awsToDomain(message, headers);

        processMessage.processMessage(domainMessage);

        Acknowledgement.acknowledge(message);
    }
}

