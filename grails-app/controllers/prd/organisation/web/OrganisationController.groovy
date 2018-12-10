package prd.organisation.web


import prd.organisation.service.OrganisationService

class OrganisationController {
    static responseFormats = ['json', 'xml']

    OrganisationService organisationService

    def save(OrganisationRegistrationCommand cmd) {
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
