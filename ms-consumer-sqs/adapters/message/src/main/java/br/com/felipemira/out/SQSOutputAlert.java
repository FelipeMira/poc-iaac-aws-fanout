package br.com.felipemira.out;

import br.com.felipemira.out.base.BaseQueue;
import br.com.felipemira.out.mappers.MessageOutMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Component
public class SQSOutputAlert extends BaseQueue {

    public SQSOutputAlert(@Value("${aws.sqs.out.queues.alert.x-type}") String xType,
                          @Value("${aws.sqs.out.queues.alert.url}") String queueUrl,
                          @Autowired SqsAsyncClient sqsClient,
                          @Autowired MessageOutMapper messageOutMapper){
        super(xType, queueUrl, sqsClient, messageOutMapper);
    }
}
