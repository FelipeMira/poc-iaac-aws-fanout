package br.com.felipemira.in.mappers;

import br.com.felipemira.domain.model.Message;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MessageMapper {

    public Message awsToDomain(org.springframework.messaging.Message message, Map<String, Object> headers) {
        return Message.builder()
                .text((String) message.getPayload())
                .messageAttributes(mapToStringMap(headers)).build();
    }

    public Map<String, String> mapToStringMap(Map<String, Object> map) {
        Map<String, String> stringMap = new HashMap<>();

        map.forEach((key, value) -> {
            stringMap.put(key, value != null ? value.toString() : null);
        });

        return stringMap;
    }
}
