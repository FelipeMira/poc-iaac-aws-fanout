package br.com.felipemira.in;

import br.com.felipemira.in.mappers.MessageInMapper;
import br.com.felipemira.ports.in.ProcessMessageUseCase;

import io.awspring.cloud.sqs.annotation.SqsListener;
import br.com.felipemira.domain.model.Message;
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Map;

// Adapter de entrada
@Service
public class SQSInputAdapter {
    private final ProcessMessageUseCase processMessage;
    private final MessageInMapper messageInMapper;

    public SQSInputAdapter(ProcessMessageUseCase processMessage, MessageInMapper messageInMapper) {
        this.processMessage = processMessage;
        this.messageInMapper = messageInMapper;
    }

    @SqsListener(value = "${aws.sqs.in.queues.receive.name}")
    public void listenToSqsQueue(@Payload org.springframework.messaging.Message<?> message, @Headers Map<String, Object> headers, Acknowledgement acknowledgement) {
        System.out.println("Mensagem recebida: " + message.getPayload());

        Message domainMessage = messageInMapper.awsToDomain(message, headers);

        try {
            processMessage.processMessage(domainMessage);
            acknowledgement.acknowledge();
        } catch (Exception e) {
            System.out.println("Erro ao processar a mensagem: " + e.getMessage());
        }
    }
}

