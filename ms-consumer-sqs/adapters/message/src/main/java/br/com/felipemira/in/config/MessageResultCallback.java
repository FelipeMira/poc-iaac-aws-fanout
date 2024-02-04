package br.com.felipemira.in.config;

import io.awspring.cloud.sqs.listener.acknowledgement.AcknowledgementResultCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;

import java.time.OffsetDateTime;
import java.util.Collection;

@Slf4j
class MessageResultCallback implements AcknowledgementResultCallback<Object> {

    @Override
    public void onSuccess(Collection<Message<Object>> messages) {
        log.info("Ack with success at {}", OffsetDateTime.now());
    }

    @Override
    public void onFailure(Collection<Message<Object>> messages, Throwable t) {
        log.error("Error while acknowledging messages", t);
    }
}
