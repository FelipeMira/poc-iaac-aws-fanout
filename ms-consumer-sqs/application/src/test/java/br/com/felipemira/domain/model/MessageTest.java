package br.com.felipemira.domain.model;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageTest {
    @Test
    public void messageAttributesAreSetCorrectly() {
        Map<String, String> attributes = Collections.singletonMap("x-type", "alert");
        Message message = new Message(attributes, "text");

        assertEquals(attributes, message.getMessageAttributes());
    }

    @Test
    public void messageTextIsSetCorrectly() {
        String text = "This is a test message";
        Message message = new Message(Collections.emptyMap(), text);

        assertEquals(text, message.getText());
    }
}
