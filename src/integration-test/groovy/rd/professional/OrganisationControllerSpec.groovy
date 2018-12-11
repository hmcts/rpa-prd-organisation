package rd.professional

import geb.spock.GebSpec
import grails.plugins.rest.client.RestBuilder
import grails.testing.mixin.integration.Integration
import grails.transaction.Rollback

import static org.springframework.http.HttpStatus.*

@Integration
@Rollback
class OrganisationControllerSpec extends GebSpec {

    static uuidPattern = "[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}"

    RestBuilder restBuilder() {
        new RestBuilder()
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
        given: "a second company is added to the database"
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
        json[0].users[0].id == 1
        json[0].organisationId && json[0].organisationId ==~ uuidPattern
        json[1].name == "Aperture Science"
        json[1].users.size() == 1
        json[1].users[0].id == 2
        json[1].organisationId && json[1].organisationId ==~ uuidPattern
        json[0].organisationId != json[1].organisationId
    }
}
