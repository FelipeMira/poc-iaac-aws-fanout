package br.com.felipemira.domain.model;

import br.com.felipemira.exceptions.Errors;
import lombok.*;

import java.util.Map;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {

    // Define um atributo para armazenar os atributos da mensagem do sqs
    private Map<String, String> messageAttributes;

    // Define um atributo para armazenar o texto da mensagem do sqs
    private String text;

    public String getQueueUrl(){
        if(this.getMessageAttributes() != null && this.getMessageAttributes().containsKey("queueUrl")){
            return this.getMessageAttributes().get("queueUrl");
        }else{
            Errors.throwBusinessException(Errors.ERROR_MESSAGE_WITHOUT_ATTRIBUTE_QUEUE_URL);
        }
        return this.getMessageAttributes().get("queueUrl");
    }
}
