package rd.professional

class UrlMappings {

    static mappings = {
        delete "/$controller/$id(.$format)?"(action: "delete")
        get "/$controller(.$format)?"(action: "index")
        get "/$controller/$id(.$format)?"(action: "show")
        post "/$controller(.$format)?"(action: "save")
        put "/$controller/$id(.$format)?"(action: "update")
        patch "/$controller/$id(.$format)?"(action: "patch")
        "/"(controller: 'application', action: 'index')
        "500"(view: '/error')
        "404"(view: '/notFound')

        // nested endpoints
        "/organisations" (resources: "organisation") {
            "/users"(resources: "professionalUser")
            "/addresses"(resources: "address")
            "/paymentAccounts"(resources: "account")
            "/domains"(resources: "domain")
        }

        '/organisations/register'(controller: 'organisation', action: 'register')
        // search endpoints
        '/search/organisations/approved'(controller: 'search', action: 'approvedOrganisations')
        '/search/organisations/pending'(controller: 'search', action: 'pendingOrganisations')
        "/organisations/pba/$email"(controller: 'search', action: 'accountsByEmail')
        "/search/users"(resources: "professionalUser")

    }
}
