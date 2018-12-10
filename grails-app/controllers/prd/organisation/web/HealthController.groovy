package prd.organisation.web

class HealthController {
    static responseFormats = ['json']

    def index() {
        render '{"status": "UP"}'
    }
}
