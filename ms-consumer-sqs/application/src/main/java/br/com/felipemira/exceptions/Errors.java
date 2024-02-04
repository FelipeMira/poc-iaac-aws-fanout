package br.com.felipemira.exceptions;

public class Errors {
    public static final String ERROR_MESSAGE_WITHOUT_TEXT = "Message without text";
    public static final String ERROR_MESSAGE_WITHOUT_ATTRIBUTES = "Message without attributes";
    public static final String ERROR_MESSAGE_WITHOUT_ATTRIBUTE_QUEUE_URL = "Message without attribute queueUrl";

    public static void throwBusinessException(String message) {
        throw new BusinessException(message);
    }
}
