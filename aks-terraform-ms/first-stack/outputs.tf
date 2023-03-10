output "aks_subnet_id" {
  value = azurerm_subnet.aks_subnet.id
}

output "resource_group_name" {
  value = azurerm_resource_group.example.name
}

output "resource_group_location" {
  value = azurerm_resource_group.example.location
}
output "server_name" {
  value = azurerm_postgresql_server.example.name
}
output "bd_name" {
  value = azurerm_postgresql_database.example.name
}
output "dns_name" {
  value = azurerm_private_dns_zone.dnsprivatezone.name
}