package rd.professional.web

import grails.converters.JSON
import rd.professional.domain.Organisation
import rd.professional.service.OrganisationService

class OrganisationController extends SubclassRestfulController<Organisation> {
    static responseFormats = ['json', 'xml']
    OrganisationController() {
        super(Organisation)
    }

    OrganisationService organisationService

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def orgs = listAllResources(params)
        // Needed so that the custom marshalling is used, else we get an infinite loop
        JSON.use('deep') {
            render orgs as JSON
        }
    }

    def register(OrganisationRegistrationCommand cmd) {
        log.info "Creating organisation"

        try {
            organisationService.registerOrganisation(cmd)
            response.status = 201
        } catch (Exception e) {
            response.status = 400
            response.outputStream << e.getMessage().bytes
        }
    }

}
