package br.com.felipemira.domain.service;

import br.com.felipemira.common.UseCase;
import br.com.felipemira.domain.model.Message;
import br.com.felipemira.exceptions.Errors;
import br.com.felipemira.ports.in.ProcessMessageUseCase;
import br.com.felipemira.ports.out.QueueData;
import br.com.felipemira.ports.out.SendMessage;

@UseCase
public class ProcessMessageService implements ProcessMessageUseCase {

    QueueData queueData;
    SendMessage sendMessage;

    public ProcessMessageService(QueueData queueData, SendMessage sendMessage) {
        this.queueData = queueData;
        this.sendMessage = sendMessage;
    }

    @Override
    public void processMessage(Message message) {
        if(message.getMessageAttributes().isEmpty()) {
            Errors.throwBusinessException(Errors.ERROR_MESSAGE_WITHOUT_ATTRIBUTES);
        }
        if(message.getText().isEmpty()) {
            Errors.throwBusinessException(Errors.ERROR_MESSAGE_WITHOUT_TEXT);
        }
        queueData.getQueueUrl(message.getQueueUrl());
        sendMessage.sendMessage(message);
    }
}
