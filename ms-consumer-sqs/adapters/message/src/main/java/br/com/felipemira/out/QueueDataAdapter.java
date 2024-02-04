package br.com.felipemira.out;

import br.com.felipemira.exceptions.Errors;
import br.com.felipemira.ports.out.QueueData;
import org.springframework.stereotype.Component;

@Component
public class QueueDataAdapter implements QueueData {

    @Override
    public String getQueueUrl(String attributeUrl) {
        switch (attributeUrl) {
            case "alerts":
                return "http://localhost:4566/000000000000/sqs-alerts-env-dev";
            case "errors":
                return "http://localhost:4566/000000000000/sqs-errors-env-dev";
            default:
                Errors.throwBusinessException(Errors.ERROR_MESSAGE_WITHOUT_ATTRIBUTE_QUEUE_URL);
        }
        return null;
    }
}
