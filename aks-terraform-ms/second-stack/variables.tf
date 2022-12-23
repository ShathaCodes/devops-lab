variable "environment" {
  description = "Environment name"
  type        = string

  validation {
    condition     = contains(["prod", "dev"], var.environment)
    error_message = "Valid values are 'dev' and 'prod'."
  }
}
variable "namespace" {
  type        = string
  description = "Namespace to deploy to"
}
