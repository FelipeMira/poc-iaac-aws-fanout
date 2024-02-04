package br.com.felipemira.in.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.listener.acknowledgement.handler.AcknowledgementMode;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class SqsConfig {

    @Bean
    @Primary
    @Profile("local")
    public SqsAsyncClient amazonSQSLocal() throws URISyntaxException {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create("teste", "teste");
        return SqsAsyncClient.builder()
                .endpointOverride(new URI("http://localhost:4566"))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .region(Region.of("sa-east-1"))
                .build();
    }

    @Bean
    @Primary
    @Profile("dev")
    public SqsAsyncClient amazonSQSDev() {
        return SqsAsyncClient.builder().region(Region.of("sa-east-1")).build();
    }

    @Bean
    SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory(SqsAsyncClient sqsAsyncClient) {

        return SqsMessageListenerContainerFactory.builder()
                .configure(options -> options.acknowledgementMode(AcknowledgementMode.MANUAL))
                .acknowledgementResultCallback(new MessageResultCallback())
                .sqsAsyncClient(sqsAsyncClient)
                .build();
    }

    @Bean
    public SqsTemplate sqsTemplate(SqsAsyncClient sqsAsyncClient){
        return SqsTemplate.builder().sqsAsyncClient(sqsAsyncClient).build();
    }

    private MessageConverter myConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        return converter;
    }
}
