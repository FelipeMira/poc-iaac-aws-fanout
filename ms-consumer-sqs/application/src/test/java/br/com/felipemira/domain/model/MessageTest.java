package br.com.felipemira.domain.model;

import br.com.felipemira.exceptions.BusinessException;
import br.com.felipemira.exceptions.Errors;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MessageTest {

    @Test
    public void getQueueUrlWithValidAttributesReturnsQueueUrl() {
        Message message = new Message();
        message.setMessageAttributes(Collections.singletonMap("queueUrl", "http://localhost:4566/000000000000/test-queue"));
        message.setText("text");

        String queueUrl = message.getQueueUrl();

        assertEquals("http://localhost:4566/000000000000/test-queue", queueUrl);
    }

    @Test
    public void getQueueUrlWithoutQueueUrlAttributeThrowsBusinessException() {
        Message message = new Message();
        message.setMessageAttributes(Collections.singletonMap("key", "value"));
        message.setText("text");

        Exception exception = assertThrows(BusinessException.class, message::getQueueUrl);

        assertEquals(Errors.ERROR_MESSAGE_WITHOUT_ATTRIBUTE_QUEUE_URL, exception.getMessage());

    }

    @Test
    public void getQueueUrlWithNullAttributesThrowsBusinessException() {
        Message message = new Message();
        message.setMessageAttributes(null);
        message.setText("text");

        Exception exception = assertThrows(BusinessException.class, message::getQueueUrl);

        assertEquals(Errors.ERROR_MESSAGE_WITHOUT_ATTRIBUTE_QUEUE_URL, exception.getMessage());
    }
}
