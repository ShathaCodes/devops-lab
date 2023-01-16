variable "name_prefix" {
  default     = "the-book-shop"
  description = "Prefix of the resource name."
}
variable "resource_group_name" {
  type        = string
  description = "Resource Group Name"
  default     = "devops"
}
variable "login" {
  type    = string
}

variable "password" {
  type    = string
}
variable "database" {
  type    = string
}
