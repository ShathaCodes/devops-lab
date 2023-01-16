data "terraform_remote_state" "aks" {
  backend = "azurerm"
  config = {
    resource_group_name  = "automation"
    storage_account_name = "gl52"
    container_name       = "infra-state"
    key                  = "second-stack.tfstate"
  }
}
data "terraform_remote_state" "bd" {
  backend = "azurerm"
  config = {
    resource_group_name  = "automation"
    storage_account_name = "gl52"
    container_name       = "infra-state"
    key                  = "first-stack.tfstate"
  }
}


resource "kubernetes_namespace" "example" {
  metadata {
    annotations = {
      name = "example-annotation"
    }
    name = "monitoring"
  }
}

module "argo_cd" {
  source = "./modules/argocd"
  providers = {
    helm = helm
  }
  namespace = kubernetes_namespace.example.id
}
module "loki" {
  source = "./modules/loki"
  providers = {
    helm = helm
  }
  namespace = kubernetes_namespace.example.id
}
module "prometheus" {
  source = "./modules/prometheus"
  providers = {
    helm       = helm
    kubernetes = kubernetes
  }
  namespace = kubernetes_namespace.example.id
  user      = var.login
  password  = var.password
  dburl       = "jdbc:postgresql://${data.terraform_remote_state.bd.outputs.server_name}.${data.terraform_remote_state.bd.outputs.dns_name}:5432/${data.terraform_remote_state.bd.outputs.bd_name}?sslmode=require"
}
module "tempo" {
  source = "./modules/tempo"
  providers = {
    helm = helm
  }
  namespace = kubernetes_namespace.example.id
}
