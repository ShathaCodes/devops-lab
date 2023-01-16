resource "helm_release" "prometheus" {
  name       = "prometheus"
  repository = "https://prometheus-community.github.io/helm-charts"
  chart      = "kube-prometheus-stack"
  namespace  = var.namespace

  values = [
    "${file("./helm-values/kube-prometheus-stack-values.yaml")}"
  ]

}

resource "kubernetes_secret" "postgresql" {
  metadata {
    name      = "book-shop-postgres-secret"
    namespace = var.namespace
  }
  data = {
    user     = var.user
    password = var.password
    url      = var.dburl
  }
}

resource "helm_release" "postgres" {
  name       = "postgres-exporter"
  repository = "https://prometheus-community.github.io/helm-charts"
  chart      = "prometheus-postgres-exporter"
  namespace  = var.namespace

  values = [
    "${file("./helm-values/postgres-exporter-values.yaml")}"
  ]

}