package br.com.felipemira.out.base;

import br.com.felipemira.domain.model.Message;
import br.com.felipemira.out.mappers.MessageOutMapper;
import br.com.felipemira.ports.out.SendMessage;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;


public abstract class BaseQueue implements SendMessage {

    protected final SqsAsyncClient sqsClient;
    protected String xType;

    protected String queueUrl;

    protected final MessageOutMapper messageOutMapper;

    public BaseQueue(String xType, String queueUrl, SqsAsyncClient sqsClient, MessageOutMapper messageOutMapper) {
        this.xType = xType;
        this.queueUrl = queueUrl;
        this.sqsClient = sqsClient;
        this.messageOutMapper = messageOutMapper;
    }

    @Override
    public boolean isCorrectQueue(String xType) {
        return xType.equals(this.xType);
    }

    @Override
    public void sendMessage(Message message) {
        sqsClient.sendMessage(messageOutMapper.messageToSendMessageRequest(message, this.queueUrl));
        System.out.println("Sending message: " + message.getText());
    }
}
