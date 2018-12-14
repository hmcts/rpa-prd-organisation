package rd.professional.web

import rd.professional.domain.Organisation
import rd.professional.service.OrganisationService

class OrganisationController extends SubclassRestfulController<Organisation> {
    static responseFormats = ['json', 'xml']

    OrganisationController() {
        super(Organisation)
    }

    OrganisationService organisationService

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
