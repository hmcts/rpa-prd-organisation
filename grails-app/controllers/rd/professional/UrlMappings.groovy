package rd.professional

class UrlMappings {

    static mappings = {
        "500"(view: '/error')
        "404"(view: '/notFound')

        get "/health" (controller: 'health', action: 'index')

        // search endpoints
        get "/search/pba/$email"(controller: 'search', action: 'accountsByEmail')
        get '/search/organisations/approved'(controller: 'search', action: 'approvedOrganisations')
        get '/search/organisations/pending'(controller: 'search', action: 'pendingOrganisations')

        // nested endpoints
        "/organisations" (resources: 'organisation', excludes: ['create', 'edit', 'patch']) {
            "/users"(resources: "professionalUser", excludes: ['create', 'edit', 'patch']) {
                "/contacts"(resources: "userContactInformation", excludes: ['create', 'edit', 'patch'])
                "/pbas"(resources: "userPaymentAccount", excludes: ['create', 'edit', 'patch', 'show', 'update'])
            }
            "/contacts"(resources: "organisationContactInformation", excludes: ['create', 'edit', 'patch'])
            "/pbas"(resources: "organisationPaymentAccount", excludes: ['create', 'edit', 'patch', 'show', 'update'])
            "/domains"(resources: "domain", excludes: ['create', 'edit', 'patch', 'show', 'update'])
        }

        // Swagger UI
        get "/apidoc/$action?/$id?"(controller: "apiDoc", action: "getDocuments")
    }
}


