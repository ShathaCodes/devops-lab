terraform {
    required_version = "~>1.1.7"
    required_providers {
        azurerm = {
            source = "hashicorp/azurerm"
            version = "~>3.31.0"
        }
        kubernetes = {
            source = "hashicorp/kubernetes"
            version = "~>2.16.0"
        }
    }
    backend "azurerm" {
    resource_group_name  = "automation"
    storage_account_name = "gl52"
    container_name       = "infra-state"
    key                  = "first-stack.tfstate"
  }
}
provider "azurerm" {
  features {}
}