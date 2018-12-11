package rd.professional

import geb.spock.GebSpec
import grails.plugins.rest.client.RestBuilder
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import org.junit.After
import rd.professional.domain.Organisation

import static org.springframework.http.HttpStatus.*

@Integration
@Rollback
class OrganisationControllerFunctionalSpec extends GebSpec {

    RestBuilder restBuilder() {
        new RestBuilder()
    }

    @After
    void deleteData() {
         Organisation.findAll()*.delete()
    }

    void "test exception from service causes 400 response"() {
        when: "a company sends an invalid registration request"
        def resp = restBuilder().post("${baseUrl}organisations/register", {
            accept("application/json")
            contentType("application/json")
            json {}
        })

        then: "an error response is returned"
        resp.status == BAD_REQUEST.value()
    }

    void "test service gives 201 response"() {
        when: "a company sends a registration request"
        def resp = restBuilder().post("${baseUrl}organisations/register", {
            accept("application/json")
            contentType("application/json")
            json {
                name = "ACME Inc."
                firstName = "Foo"
                lastName = "Barton"
                email = "foo@bar.com"
            }
        })

        then: "a created response is returned"
        resp.status == CREATED.value()
    }

    void "test GET organisations returns a list of all organisations"() {
        given: "two companies are added to the database"
        restBuilder().post("${baseUrl}organisations/register", {
            accept("application/json")
            contentType("application/json")
            json {
                name = "ACME Inc."
                firstName = "Foo"
                lastName = "Barton"
                email = "foo@bar.com"
            }
        })
        restBuilder().post("${baseUrl}/organisations/register", {
            accept("application/json")
            contentType("application/json")
            json {
                name = "Aperture Science"
                firstName = "Cave"
                lastName = "Johnson"
                email = "cjohnson@aperturescience.com"
            }
        })

        when: "a GET request is sent"
        def resp = restBuilder().get("${baseUrl}organisations", {
            accept("application/json")
        })

        then: "a list of organisations is returned"
        resp.status == OK.value()
        def json = resp.json
        json.size() == 2
        json[0].name == "ACME Inc."
        json[0].users.size() == 1
        json[1].name == "Aperture Science"
        json[1].users.size() == 1
        json[0].users[0].id != json[1].users[0].id
    }
}
