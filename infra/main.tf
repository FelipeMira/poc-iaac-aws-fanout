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

# Cria um tópico SNS
resource "aws_sns_topic" "sns_topic" {
  name              = local.sns-name
  policy            = data.aws_iam_policy_document.sns-topic-policy.json
  kms_master_key_id = aws_kms_key.kms_key.arn
}

# Cria um tópico SQS
resource "aws_sqs_queue" "sqs_queue" {
  name                              = local.sqs-alerts-name
  kms_master_key_id                 = aws_kms_key.kms_key.arn
  policy                            = data.aws_iam_policy_document.sqs-alerts-policy.json
  kms_data_key_reuse_period_seconds = 300
}

# Cria um tópico SQS para Erro
resource "aws_sqs_queue" "sqs_queue_error" {
  name                              = local.sqs-errors-name
  kms_master_key_id                 = aws_kms_key.kms_key.arn
  policy                            = data.aws_iam_policy_document.sqs-errors-policy.json
  kms_data_key_reuse_period_seconds = 300
}


# Inscreve o tópico SQS no tópico SNS com um filtro
resource "aws_sns_topic_subscription" "sqs_sub" {
  topic_arn = aws_sns_topic.sns_topic.arn
  protocol  = "sqs"
  endpoint  = aws_sqs_queue.sqs_queue.arn
  raw_message_delivery = true

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
  raw_message_delivery = true

  filter_policy = <<EOF
{
  "${var.filter_name}": ["erro"]
}
EOF
}