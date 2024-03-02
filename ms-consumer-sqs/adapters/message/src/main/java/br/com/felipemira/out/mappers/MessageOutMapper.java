package br.com.felipemira.out.mappers;

import br.com.felipemira.domain.model.Message;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.HashMap;
import java.util.Map;

@Component
public class MessageOutMapper {

    public SendMessageRequest messageToSendMessageRequest(Message message, String queueUrl) {
        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        for (Map.Entry<String, String> entry : message.getMessageAttributes().entrySet()) {
            messageAttributes.put(entry.getKey(), MessageAttributeValue.builder()
                    .dataType("String")
                    .stringValue(entry.getValue())
                    .build());
        }

        return SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(message.getText())
                .messageAttributes(messageAttributes)
                .build();
    }
}

