# Define o provedor da AWS e a região
provider "aws" {
  region = var.region
  access_key                 = "teste"
  secret_key                 = "teste"
  skip_credentials_validation = true
  skip_metadata_api_check    = true


  endpoints {
    kms       = "http://localhost:4566"
    sns       = "http://localhost:4566"
    sqs       = "http://localhost:4566"
    sts       = "http://localhost:4566"
  }
}

# Cria uma chave kms para criptografar as mensagens
resource "aws_kms_key" "kms_key" {
  description             = "Chave KMS para criptografar as mensagens do SNS e do SQS"
  deletion_window_in_days = 10
}

# Cria um tópico SNS
resource "aws_sns_topic" "sns_topic" {
  name              = local.sns-name
  policy            = data.aws_iam_policy_document.sns-topic-policy.json
  kms_master_key_id = aws_kms_key.kms_key.arn
}

# Cria um tópico SQS
resource "aws_sqs_queue" "sqs_queue" {
  name                              = local.sqs-name
  kms_master_key_id                 = aws_kms_key.kms_key.arn
  policy                            = data.aws_iam_policy_document.sqs-queue-policy.json
  kms_data_key_reuse_period_seconds = 300
}

# Cria um tópico SQS para Erro
resource "aws_sqs_queue" "sqs_queue_error" {
  name                              = "${local.sqs-name}-error"
  kms_master_key_id                 = aws_kms_key.kms_key.arn
  policy                            = data.aws_iam_policy_document.sqs-queue-policy.json
  kms_data_key_reuse_period_seconds = 300
}


# Inscreve o tópico SQS no tópico SNS com um filtro
resource "aws_sns_topic_subscription" "sqs_sub" {
  topic_arn = aws_sns_topic.sns_topic.arn
  protocol  = "sqs"
  endpoint  = aws_sqs_queue.sqs_queue.arn

  filter_policy = <<EOF
{
  "${var.filter_name}": ["${var.filter_value}"]
}
EOF
}

# Inscreve o tópico SQS no tópico SNS com um filtro de erro
resource "aws_sns_topic_subscription" "sqs_sub_error" {
  topic_arn = aws_sns_topic.sns_topic.arn
  protocol  = "sqs"
  endpoint  = aws_sqs_queue.sqs_queue_error.arn

  filter_policy = <<EOF
{
  "${var.filter_name}": ["erro"]
}
EOF
}