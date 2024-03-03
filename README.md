# POC infra as code - Arquitetura Fanout

## Descrição

POC simples de uma arquitetura fanout, utilizando:
- Terraform
- policy 
- kms
- sns
- sqs
- localstack

---

## Pré Requisitos

- Ter o docker instalado e configurado
- Ter terraform instalado

---


## Como foi construído?

Utilizando a arquitetura fanout:

![Arquitetura](images/fanout.png "Arquitetura")

---

## Como executar?

#### Preparando o ambiente

- Inicie seu docker:
```shell
"C:\PROGRA~1\Docker\Docker\Docker Desktop.exe"
``` 

#### Localstack padrão:
- Execute o comando:
```shell
docker-compose up
``` 
- Se tudo ocorreu bem seu ambiente está configurado.

#### Localstack PRO:

- O localstack permite que utilizemos a versão PRO com a licença `Hobby Subscription`, destinada a uso não comercial, 
sem a necessidade de informação do cartão de crédito.
- Com a licença e o token em mãos, podemos configurar o localstack pro.
- Devemos criar um arquivo .env em `~/../Public/dev/Docker/localstack/.env` com o seguinte conteúdo:

```text
LOCALSTACK_AUTH_TOKEN=<Seu token para o localstack PRO>
```

- Execute o comando:
```shell
cd localstack-pro && docker-compose up
``` 
- Se tudo ocorreu bem seu ambiente está configurado.

#### Executando criação da infraestrutura

- Devemos ir até a pasta de infraestrutura e executar o comando:
```shell
terraform -chdir=./infra init && terraform -chdir=./infra plan && terraform -chdir=./infra apply -auto-approve
```

---

## Como enviar mensagens ao tópico sns?

#### Configurar o arquivo de credenciais da aws

- Dentro da pasta `~/.aws/` crie um arquivo chamado `credentials` e adicione o seguinte conteúdo:
- Substitua os valores de `aws_access_key_id` e `aws_secret_access_key` pelos valores que estão no arquivo `docker-compose.yml`:

```text
[localstack]
aws_access_key_id=teste
aws_secret=teste
```

#### Configurar o arquivo de config da aws

- Dentro da pasta `~/.aws/` crie um arquivo chamado `config` e adicione o seguinte conteúdo:
- Substitua o valor de `region` pelo valor que está no arquivo `docker-compose.yml`:

```text
[profile localstack]
region=sa-east-1
output=json
endpoint_url=http://localhost:4566
[default]
region = sa-east-1
output = json
```

#### Mensagem para fila de alerta

```shell
aws sns publish --topic-arn "arn:aws:sns:sa-east-1:000000000000:sns-receive-env-dev" \
--message '{"default": "Mensagem de teste"}' \
--message-structure json \
--message-attributes '{"x-type":{"DataType":"String","StringValue":"alert"}, "x-queueUrl":{"DataType":"String","StringValue":"alert"}}' \
--endpoint-url=http://localhost:4566 \
--profile=localstack
```

#### Mensagem para fila de erro

```shell
aws sns publish \
 --topic-arn "arn:aws:sns:sa-east-1:000000000000:sns-receive-env-dev" \
 --message="Mensagem de teste" \
 --message-attributes '{"x-type":{"DataType":"String","StringValue":"error"}, "x-queueUrl":{"DataType":"String","StringValue":"error"}}' \
 --endpoint-url=http://localhost:4566 \
 --profile=localstack
```

## Projeto consumidor da fila SQS

- Foi criado um projeto que consome da fila sqs e envia a mensagem para outra fila.
- Vamos enviar uma mensagem na fila `sqs-receive-env-dev` recepcionar no fluxo de entrada e enviar para a fila `sqs-alerts-env-dev`.
- Após o primeiro teste vamos enviar uma mensagem na fila `sqs-receive-env-dev` recepcionar no fluxo de entrada e enviar para a fila `sqs-error-env-dev`.
- Para direcionar para a fila correta utilizamos o padrão Strategy. No padrão Strategy, um comportamento ou algoritmo 
é encapsulado em uma classe, e a classe cliente pode escolher o algoritmo apropriado em tempo de execução. 
No nosso caso, a interface SendMessage define um comportamento comum (enviar uma mensagem), e as classes SQSOutputAlert 
e SQSOutputError implementam esse comportamento de maneiras diferentes. A classe ProcessMessageService pode então 
escolher qual estratégia usar com base no tipo de mensagem.

#### Executando o projeto

- Devemos executar o comando: 
```shell
cd ms-consumer-sqs && ./gradlew :configuration:bootRun --args='--spring.profiles.active=local'
```

#### Mensagem para fila de recebimento e enviando para a fila de alerta

```shell
aws sqs send-message --queue-url "http://localhost:4566/000000000000/sqs-receive-env-dev" \
--message-body '{"default": "Mensagem de teste"}' \
--message-attributes '{"x-type":{"DataType":"String","StringValue":"alert"}, "x-queueUrl":{"DataType":"String","StringValue":"alert"}}' \
--endpoint-url=http://localhost:4566 \
--profile=localstack
```

#### Mensagem para fila de recebimento e enviando para a fila de erros

```shell
aws sqs send-message --queue-url "http://localhost:4566/000000000000/sqs-receive-env-dev" \
--message-body '{"default": "Mensagem de teste"}' \
--message-attributes '{"x-type":{"DataType":"String","StringValue":"error"}, "x-queueUrl":{"DataType":"String","StringValue":"error"}}' \
--endpoint-url=http://localhost:4566 \
--profile=localstack
```