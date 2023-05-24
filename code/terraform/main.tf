terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "3.52.0"
    }
  }
}


provider "azurerm" {
  features {}
 }


locals {
  azure_common_tags = {
    env = "development"
    project = "labs-apim"
    source = "terraform"
  }
}


module "network" {
  source        = "./network"
  location      = "northeurope"
  base_name     = "labs-apim"
  common_tags   = local.azure_common_tags
  my_ip         = "set-your-ip"
}

# module "infrastructure" {
#   source                = "./infrastructure"
#   location              = "northeurope"
#   dns_name              = "antoniocaccamo-labs.it"
#   resource-group-name   = module.network.resource-group-name-out
#   common_tags           = local.azure_common_tags
#   subnet-appgw-wl-id    = module.network.subnet-appgw-wl-id-out
#   subnet-apim-wl-id     = module.network.subnet-apim-wl-id-out
#   subnet-jumphost-id    = module.network.subnet-jumphost-id-out
#   ssk-key-pub-location  = file("~/.ssh/azure-vm.pub")
# }
