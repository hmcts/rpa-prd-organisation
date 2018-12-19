package rd.professional

import geb.spock.GebSpec
import grails.gorm.transactions.Rollback
import grails.plugins.rest.client.RestBuilder
import grails.testing.mixin.integration.Integration
import grails.testing.spock.OnceBefore
import org.junit.AfterClass
import rd.professional.domain.Organisation
import spock.lang.Shared

import static org.springframework.http.HttpStatus.*

@Integration
@Rollback
class DomainControllerFunctionalSpec extends GebSpec {

    @Shared
    def orgId

    @OnceBefore
    void setupOrg() {
        def resp = restBuilder().post("${baseUrl}organisations", {
            accept("application/json")
            contentType("application/json")
            json {
                name = "Domain Inc."
                superUser = {
                    firstName = "Foo"
                    lastName = "Barton"
                    email = "foo@bardomain.com"
                }
                domains = [{
                               domain = "www.foo.com"
                           }]
            }
        })
        orgId = resp.json.organisationId
    }

    @AfterClass
    void deleteData() {
        Organisation.where {
            organisationId == orgId
        }.find().delete()
    }

    void "Test POST new domain"() {
        when: "a request is made to add a domain to an organisation"
        def resp = restBuilder().post("${baseUrl}organisations/$orgId/domains", {
            accept("application/json")
            contentType("application/json")
            json {
                domain = "www.bar.com"
            }
        })

        then: "the domain is added"
        resp.status == CREATED.value()
    }

    void "Test GET list of domains belonging to an organisation"() {
        when: "a GET request is made to the org's /domains endpoint"
        def resp = restBuilder().get("${baseUrl}organisations/$orgId/domains", {
            accept("application/json")
        })

        then: "an list of domains is returned"
        resp.status == OK.value()
        resp.json && resp.json.size() == 2
    }

    void "Test DELETE a domain works"() {
        when: "a DELETE request is made"
        def resp = restBuilder().delete("${baseUrl}organisations/$orgId/domains/www%2Ebar%2Ecom")

        then: "a success response is returned"
        resp.status == NO_CONTENT.value()
    }

    RestBuilder restBuilder() {
        new RestBuilder()
    }
}
