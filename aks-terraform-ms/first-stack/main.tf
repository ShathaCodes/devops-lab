resource "azurerm_resource_group" "app" {
  name     = var.resource_group_name
  location = "West Europe"
}

resource "azurerm_kubernetes_cluster" "app" {
  name                = "app-aks"
  location            = azurerm_resource_group.app.location
  resource_group_name = azurerm_resource_group.app.name
  dns_prefix          = "appaks"
  sku_tier            = "Free"

  default_node_pool {
    name       = "default"
    node_count = 1
    vm_size    = "Standard_D2_v2"
  }

  identity {
    type = "SystemAssigned"
  }

  tags = {
    Environment = "Development"
  }
}


