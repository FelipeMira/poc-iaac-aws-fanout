package br.com.felipemira.domain.service;

import br.com.felipemira.common.UseCase;
import br.com.felipemira.domain.model.Message;
import br.com.felipemira.exceptions.BusinessException;
import br.com.felipemira.exceptions.Errors;
import br.com.felipemira.ports.in.ProcessMessageUseCase;
import br.com.felipemira.ports.out.SendMessage;

import java.util.List;

@UseCase
public class ProcessMessageService implements ProcessMessageUseCase {

    private final List<SendMessage> sendMessages;

    public ProcessMessageService(List<SendMessage> sendMessages) {
        this.sendMessages = sendMessages;
    }

    @Override
    public void processMessage(Message message) {
        if(message.getMessageAttributes().isEmpty()) {
            Errors.throwBusinessException(Errors.ERROR_MESSAGE_WITHOUT_ATTRIBUTES);
        }
        if(message.getText().isEmpty()) {
            Errors.throwBusinessException(Errors.ERROR_MESSAGE_WITHOUT_TEXT);
        }

        String xType = message.getMessageAttributes().get("x-type");

        if (xType == null || xType.isEmpty()) {
            Errors.throwBusinessException(Errors.ERROR_X_TIPO_NOT_FOUND);
        }

        sendMessages.stream()
                .filter(sender -> sender.isCorrectQueue(xType))
                .findFirst()
                .orElseThrow(() -> new BusinessException(Errors.ERROR_OUT_PORT_NOT_FOUND))
                .sendMessage(message);
    }
}
