package rd.professional

class UrlMappings {

    static mappings = {
        "500"(view: '/error')
        "404"(view: '/notFound')

        get "/health" (controller: 'health', action: 'index')

        // search endpoints
        get "/search/pba/$email"(controller: 'search', action: 'accountsByEmail')
        get "/search/organisation/$name"(controller: 'search', action: 'organisationIdByName')
        post "/organisations"(controller: 'organisation', action: 'save')
        post "/organisations/${organisationId}/users"(controller: 'professionalUser', action: 'save')
    }
}


