// used for db migrations
output "microserviceName" {
  value = "${local.app_full_name}"
}

// used for db migrations
output "vaultName" {
  value = "${module.local_key_vault.key_vault_name}"
}

// used for grabing shared secrects (shown in the jenkins file)
//output "vaultUri" {
//  value = "${data.azurerm_key_vault.shared_key_vault.vault_uri}"
//}

//output "idam_api_url" {
//  value = "http://${var.idam_api_url}-${local.local_env}.service.core-compute-${local.local_env}.internal"
//}

//output "s2s_url" {
//  value = "http://${var.s2s_url}-${local.local_env}.service.core-compute-${local.local_env}.internal"
//}

//output "test_s2s_token" {
//  value = "${data.azurerm_key_vault_secret.source_test_s2s_secret.value}"
//}
