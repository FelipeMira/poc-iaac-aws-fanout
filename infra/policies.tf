# Criar uma política para o tópico SNS. Isso permitirá que a fila SQS possa se inscrever no tópico
data "aws_iam_policy_document" "sns-topic-policy" {
  statement {
    actions = [
      "SNS:Subscribe",
      "SNS:Receive",
    ]

    condition {
      test     = "StringLike"
      variable = "SNS:Endpoint"

      # Para evitar dependências circulares, devemos criar o ARN nós mesmos
      values = [
        "arn:aws:sqs:${var.region}:${data.aws_caller_identity.current.account_id}:${local.sqs-name}",
      ]
    }

    effect = "Allow"

    principals {
      type        = "AWS"
      identifiers = ["*"]
    }

    resources = [
      "arn:aws:sns:${var.region}:${data.aws_caller_identity.current.account_id}:${local.sns-name}"
    ]

    sid = "sid-101"
  }
}

# Criar uma política para a fila SQS. Isso permitirá que o tópico SNS possa publicar mensagens na fila SQS
data "aws_iam_policy_document" "sqs-queue-policy" {
  policy_id = "arn:aws:sqs:${var.region}:${data.aws_caller_identity.current.account_id}:${local.sqs-name}/SQSDefaultPolicy"

  statement {
    sid = "example-sns-topic"

    effect = "Allow"

    principals {
      type        = "AWS"
      identifiers = ["*"]
    }

    actions = [
      "SQS:SendMessage",
    ]

    resources = [
      "arn:aws:sqs:${var.region}:${data.aws_caller_identity.current.account_id}:${local.sqs-name}"
    ]

    condition {
      test     = "ArnEquals"
      variable = "aws:SourceArn"

      values = [
        "arn:aws:sns:${var.region}:${data.aws_caller_identity.current.account_id}:${local.sns-name}"
      ]
    }
  }
}