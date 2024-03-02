package br.com.felipemira.out;

import br.com.felipemira.domain.model.Message;
import br.com.felipemira.out.base.BaseQueue;
import br.com.felipemira.out.mappers.MessageOutMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Component
public class SQSOutputError extends BaseQueue {

    public SQSOutputError(@Value("${aws.sqs.out.queues.error.x-type}") String xType,
                          @Value("${aws.sqs.out.queues.error.url}") String queueUrl,
                          @Autowired SqsAsyncClient sqsClient,
                          @Autowired MessageOutMapper messageOutMapper){
        super(xType, queueUrl, sqsClient, messageOutMapper);
    }
}
