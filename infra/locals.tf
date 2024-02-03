# Criar algumas variáveis locais para os nomes do SQS e do SNS
locals {
  sqs-name = "app-sqs-env-${var.env-name}"
  sns-name = "app-sns-env-${var.env-name}"
}
