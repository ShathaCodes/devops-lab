data "terraform_remote_state" "aks" {
  backend = "azurerm"
  config = {
    resource_group_name  = "automation"
    storage_account_name = "gl52"
    container_name       = "infra-state"
    key                  = "second-stack.tfstate"
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

resource "kubernetes_secret" "grafana" {
  metadata {
    name      = "grafana-admin-credentials"
    namespace = kubernetes_namespace.example.id
  }
  data = {
    admin-user     = "c2hhdGhh"
    admin-password = "bWRw"
  }

}

resource "kubernetes_secret" "postgresql" {
  metadata {
    name      = "book-shop-postgres-secret"
    namespace = kubernetes_namespace.example.id
  }
  data = {
    user     = var.login
    password = var.password
    url      = "jdbc:postgresql://the-book-boutique-server.privatelink.postgres.database.azure.com:5432/bookshop?sslmode=require"
  }
}


resource "helm_release" "argocd" {
  name       = "argocd"
  repository = "https://argoproj.github.io/argo-helm"
  chart      = "argo-cd"
  namespace  = kubernetes_namespace.example.id

  values = [
    "${file("./helm-values/argocd-values.yaml")}"
  ]

}

#resource "helm_release" "ingress" {
#  name       = "ingress-nginx"
#  repository = "https://kubernetes.github.io/ingress-nginx"
#  chart      = "ingress-nginx"
#  namespace  = "default"
#
#  values = [
#    "${file("./helm-values/ingress-values.yaml")}"
#  ]
#
#}

resource "helm_release" "prometheus" {
  name       = "prometheus"
  repository = "https://prometheus-community.github.io/helm-charts"
  chart      = "kube-prometheus-stack"
  namespace  = kubernetes_namespace.example.id

  values = [
    "${file("./helm-values/kube-prometheus-stack-values.yaml")}"
  ]

}


resource "helm_release" "loki" {
  name       = "loki"
  repository = "https://grafana.github.io/helm-charts"
  chart      = "loki-stack"
  namespace  = kubernetes_namespace.example.id

  values = [
    "${file("./helm-values/loki-stack-values.yaml")}"
  ]
}

resource "helm_release" "tempo" {
  name       = "tempo"
  repository = "https://grafana.github.io/helm-charts"
  chart      = "tempo"
  namespace  = kubernetes_namespace.example.id

  values = [
    "${file("./helm-values/tempo-values.yaml")}"
  ]

}

resource "helm_release" "postgres" {
  name       = "postgres-exporter"
  repository = "https://prometheus-community.github.io/helm-charts"
  chart      = "prometheus-postgres-exporter"
  namespace  = kubernetes_namespace.example.id

  values = [
    "${file("./helm-values/postgres-exporter-values.yaml")}"
  ]

}
