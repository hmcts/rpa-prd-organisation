package rd.professional

class UrlMappings {

    static mappings = {
        "500"(view: '/error')
        "404"(view: '/notFound')

        get "/health" (controller: 'health', action: 'index')

        // search endpoints
        get "/search/pba/$email"(controller: 'search', action: 'accountsByEmail')
    }
}


