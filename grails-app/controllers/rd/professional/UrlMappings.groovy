package rd.professional

class UrlMappings {

    static mappings = {
        "500"(view: '/error')
        "404"(view: '/notFound')

        // search endpoints
        "/search/pba/$email"(controller: 'search', action: 'accountsByEmail')
        '/search/organisations/approved'(controller: 'search', action: 'approvedOrganisations')
        '/search/organisations/pending'(controller: 'search', action: 'pendingOrganisations')

        // nested endpoints
        "/organisations" (resources: 'organisation', excludes:['create', 'edit', 'patch']) {
            "/users"(resources: "professionalUser", excludes: ['create', 'edit', 'patch'])
            "/addresses"(resources: "address", excludes: ['create', 'edit', 'patch'])
            "/paymentAccounts"(resources: "account", excludes: ['create', 'edit', 'patch'])
            "/domains"(resources: "domain", excludes: ['create', 'edit', 'patch'])
        }

        // Swagger UI
        "/apidoc/$action?/$id?"(controller: "apiDoc", action: "getDocuments")
    }
}
