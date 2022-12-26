variable "name_prefix" {
  default     = "the-book-boutique"
  description = "Prefix of the resource name."
}
variable "resource_group_name" {
  type        = string
  description = "Resource Group Name"
  default     = "devops"
}
variable "login" {
  type    = string
  default = "---------"
}

variable "password" {
  type    = string
  default = "---------"
}
variable "database" {
  type    = string
  default = "--------"
}
