package br.com.felipemira.exceptions;

public class Errors {
    public static final String ERROR_MESSAGE_WITHOUT_TEXT = "Message without text";
    public static final String ERROR_MESSAGE_WITHOUT_ATTRIBUTES = "Message without attributes";
    public static final String ERROR_MESSAGE_WITHOUT_ATTRIBUTE_QUEUE_URL = "Message without attribute queueUrl";
    public static final String ERROR_X_TIPO_NOT_FOUND = "x-tipo not found";
    public static final String ERROR_OUT_PORT_NOT_FOUND = "Out port not found";

    public static void throwBusinessException(String message) {
        throw new BusinessException(message);
    }
}
