package rd.professional

class UrlMappings {

    static mappings = {
        "500"(view: '/error')
        "404"(view: '/notFound')

        // search endpoints
        get "/search/pba/$email"(controller: 'search', action: 'accountsByEmail')
        get '/search/organisations/approved'(controller: 'search', action: 'approvedOrganisations')
        get '/search/organisations/pending'(controller: 'search', action: 'pendingOrganisations')

        // nested endpoints
        "/organisations" (resources: 'organisation', excludes:['create', 'edit', 'patch']) {
            "/users"(resources: "professionalUser", excludes: ['create', 'edit', 'patch'])
            "/addresses"(resources: "address", excludes: ['create', 'edit', 'patch'])
            "/paymentAccounts"(resources: "account", excludes: ['create', 'edit', 'patch'])
            "/domains"(resources: "domain", excludes: ['create', 'edit', 'patch', 'show', 'update'])
        }

        // Swagger UI
        get "/apidoc/$action?/$id?"(controller: "apiDoc", action: "getDocuments")
    }
}
