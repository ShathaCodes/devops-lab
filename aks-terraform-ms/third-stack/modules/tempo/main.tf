resource "helm_release" "tempo" {
  name       = "tempo"
  repository = "https://grafana.github.io/helm-charts"
  chart      = "tempo"
  namespace  = var.namespace

  values = [
    "${file("./helm-values/tempo-values.yaml")}"
  ]
}