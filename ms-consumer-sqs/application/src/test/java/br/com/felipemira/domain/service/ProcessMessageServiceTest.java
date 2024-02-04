package br.com.felipemira.domain.service;

import br.com.felipemira.domain.model.Message;
import br.com.felipemira.exceptions.BusinessException;
import br.com.felipemira.exceptions.Errors;
import br.com.felipemira.ports.out.QueueData;
import br.com.felipemira.ports.out.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

public class ProcessMessageServiceTest {

    @Mock
    private QueueData queueData;
    @Mock
    private SendMessage sendMessage;

    private ProcessMessageService processMessageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        processMessageService = new ProcessMessageService(queueData, sendMessage);
    }


    @Test
    public void processMessageWithValidMessageCallsQueueDataAndSendMessage() {
        Message message = new Message();
        message.setMessageAttributes(Collections.singletonMap("queueUrl", "http://localhost:4566/000000000000/test-queue"));
        message.setText("text");

        processMessageService.processMessage(message);

        verify(queueData).getQueueUrl(message.getQueueUrl());
        verify(sendMessage).sendMessage(message);
    }

    @Test
    public void processMessageWithValidMessageCallsSendMessage() {
        Message message = new Message();
        message.setMessageAttributes(Collections.singletonMap("key", "value"));
        message.setText("text");

        Exception exception = assertThrows(BusinessException.class, () -> processMessageService.processMessage(message));

        assertEquals(Errors.ERROR_MESSAGE_WITHOUT_ATTRIBUTE_QUEUE_URL, exception.getMessage());
    }

    @Test
    public void processMessageWithEmptyAttributesThrowsBusinessException() {
        Message message = new Message();
        message.setMessageAttributes(Collections.emptyMap());
        message.setText("text");

        Exception exception = assertThrows(BusinessException.class, () -> processMessageService.processMessage(message));

        assertEquals(Errors.ERROR_MESSAGE_WITHOUT_ATTRIBUTES, exception.getMessage());
    }

    @Test
    public void processMessageWithEmptyTextThrowsBusinessException() {
        Message message = new Message();
        message.setMessageAttributes(Collections.singletonMap("queueUrl", "http://localhost:4566/000000000000/test-queue"));
        message.setText("");

        Exception exception = assertThrows(BusinessException.class, () -> processMessageService.processMessage(message));

        assertEquals(Errors.ERROR_MESSAGE_WITHOUT_TEXT, exception.getMessage());
    }
}