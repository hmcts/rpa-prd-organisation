package prd.organisation.web

import grails.validation.ValidationException
import prd.organisation.service.RegistrationService

class RegistrationController {
	static responseFormats = ['json', 'xml']

    RegistrationService registrationService

    def register(OrganisationRegistrationCommand cmd) { 
        log.info "Creating organisation"

        try {
            registrationService.registerOrganisation(cmd)
            response.status = 201
        } catch (Exception e) {
            response.status = 400
            response.outputStream << e.getMessage().bytes
        }
    }

}
