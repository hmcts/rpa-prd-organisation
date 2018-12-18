package rd.professional

import geb.spock.GebSpec
import grails.gorm.transactions.Rollback
import grails.plugins.rest.client.RestBuilder
import grails.testing.mixin.integration.Integration
import grails.testing.spock.OnceBefore
import org.junit.AfterClass
import rd.professional.domain.Organisation
import spock.lang.Shared

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

@Integration
@Rollback
class HealthControllerFunctionalSpec extends GebSpec {

    void "Test health controller works"() {
        when:
        def resp = restBuilder().get("${baseUrl}health", {
            accept("application/json")
        })

        then:
        resp.status == OK.value()
    }

    RestBuilder restBuilder() {
        new RestBuilder()
    }
}
