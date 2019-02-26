package rd.professional.service

import grails.config.Config
import grails.testing.services.ServiceUnitTest
import spock.lang.Shared
import spock.lang.Specification

class ConfigurationServiceSpec extends Specification implements ServiceUnitTest<ConfigurationService> {

    @Shared
    Config config

    def setup() {
        config = Mock(Config)
        config.getProperty('prd_users_endpointurl', String) >> {
            "http://qux.net"
        }
    }

    def cleanup() {
    }

    void "test set and get config"() {
        when:
        service.setConfiguration(config)

        then:
        service.getPrdUsersEndpointUrl() == "http://qux.net"
    }
}