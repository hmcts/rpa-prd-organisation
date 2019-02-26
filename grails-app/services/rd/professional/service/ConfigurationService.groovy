package rd.professional.service

import grails.config.Config
import grails.core.support.GrailsConfigurationAware

class ConfigurationService implements GrailsConfigurationAware {

    String prdUsersEndpointUrl

    @Override
    void setConfiguration(Config co) {
        prdUsersEndpointUrl = co.getProperty('prd_users_endpointurl', String)
        log.info "Endpoint for PRD Users microservice: " + prdUsersEndpointUrl
    }

    String getPrdUsersRestEndpointURL() {
        prdUsersEndpointUrl
    }

}
