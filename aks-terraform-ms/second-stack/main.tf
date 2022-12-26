data "terraform_remote_state" "aks" {
  backend = "azurerm"
  config = {
    resource_group_name  = "automation"
    storage_account_name = "gl52"
    container_name       = "infra-state"
    key                  = "first-stack.tfstate"
  }
}

resource "azurerm_kubernetes_cluster" "app" {
  name                = "app-aks"
  location            = data.terraform_remote_state.aks.outputs.resource_group_location
  resource_group_name = data.terraform_remote_state.aks.outputs.resource_group_name
  dns_prefix          = "appaks"
  sku_tier            = "Free"

  default_node_pool {
    vnet_subnet_id = data.terraform_remote_state.aks.outputs.aks_subnet_id
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


