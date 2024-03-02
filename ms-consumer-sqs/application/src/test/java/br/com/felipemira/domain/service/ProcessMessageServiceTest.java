package br.com.felipemira.domain.service;

import br.com.felipemira.domain.model.Message;
import br.com.felipemira.exceptions.BusinessException;
import br.com.felipemira.exceptions.Errors;
import br.com.felipemira.ports.out.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProcessMessageServiceTest {

    @Mock
    private SendMessage sendMessage;

    private List<SendMessage> sendMessageList;

    @Autowired
    private ProcessMessageService processMessageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        sendMessageList = Collections.singletonList(sendMessage);
        processMessageService = new ProcessMessageService(sendMessageList);
    }

    @Test
    public void processMessageWithAllAttributesSendsMessage() {
        Message message = new Message();
        message.setMessageAttributes(Collections.singletonMap("x-type", "alert"));
        message.setText("text");

        when(sendMessage.isCorrectQueue("alert")).thenReturn(true);

        doNothing().when(sendMessage).sendMessage(any(Message.class));

        processMessageService.processMessage(message);

        verify(sendMessage).sendMessage(message);
    }

    @Test
    public void processMessageWithoutAttributesThrowsException() {
        Message message = new Message();
        message.setMessageAttributes(Collections.emptyMap());
        message.setText("text");

        assertThrows(BusinessException.class, () -> processMessageService.processMessage(message), Errors.ERROR_MESSAGE_WITHOUT_ATTRIBUTES);
    }

    @Test
    public void processMessageWithoutTextThrowsException() {
        Message message = new Message();
        message.setMessageAttributes(Collections.singletonMap("x-type", "alert"));
        message.setText("");

        assertThrows(BusinessException.class, () -> processMessageService.processMessage(message), Errors.ERROR_MESSAGE_WITHOUT_TEXT);
    }

    @Test
    public void processMessageWithoutXTypeThrowsException() {
        Message message = new Message();
        message.setMessageAttributes(Collections.singletonMap("key", "value"));
        message.setText("text");

        assertThrows(BusinessException.class, () -> processMessageService.processMessage(message), Errors.ERROR_X_TIPO_NOT_FOUND);
    }
}