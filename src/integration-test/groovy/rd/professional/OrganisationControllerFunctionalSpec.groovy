package rd.professional

import geb.spock.GebSpec
import grails.gorm.transactions.Rollback
import grails.plugins.rest.client.RestBuilder
import grails.testing.mixin.integration.Integration
import org.junit.AfterClass
import rd.professional.domain.Organisation

import static org.springframework.http.HttpStatus.*

@Integration
@Rollback
class OrganisationControllerFunctionalSpec extends GebSpec {

    RestBuilder restBuilder() {
        new RestBuilder()
    }

    @AfterClass
    void deleteData() {
        Organisation.findAll()*.delete()
    }

    void "test exception from service causes 400 response"() {
        when: "a company sends an invalid registration request"
        def resp = restBuilder().post("${baseUrl}organisations", {
            accept("application/json")
            contentType("application/json")
            json {}
        })

        then: "an error response is returned"
        resp.status == BAD_REQUEST.value()
    }

    void "test service gives 201 response on successful organisation registration"() {
        when: "a company sends a registration request"
        def resp = restBuilder().post("${baseUrl}organisations", {
            accept("application/json")
            contentType("application/json")
            json {
                name = "ACME Inc."
                superUser = {
                    firstName = "Foo"
                    lastName = "Barton"
                    email = "foo@bar.com"
                }
            }
        })

        then: "a created response is returned"
        resp.status == CREATED.value()
        resp.json != null && resp.json.organisationId != null
    }

    void "test GET organisations returns a list of all organisations"() {
        given: "a second company is added to the database"
        restBuilder().post("${baseUrl}/organisations", {
            accept("application/json")
            contentType("application/json")
            json {
                name = "Aperture Science"
                superUser = {
                    firstName = "Cave"
                    lastName = "Johnson"
                    email = "cjohnson@aperturescience.com"
                }
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

    void "test GET organisations/uuid returns details of that organisation"() {
        given: "the UUID of a company"
        def orgId = Organisation.first().organisationId

        when:
        def resp = restBuilder().get("${baseUrl}organisations/$orgId", {
            accept("application/json")
        })

        then:
        resp.status == 200
        resp.json.name == Organisation.first().name
    }

    void "test update organisation"() {
        given: "the UUID of a company"
        def orgId = Organisation.first().organisationId
        def originalName = Organisation.first().name
        def newName = "Name Enterprises LLC."

        when: "a company sends an update request"
        def resp = restBuilder().put("${baseUrl}organisations/$orgId", {
            accept("application/json")
            contentType("application/json")
            json {
                name = newName
            }
        })

        then: "a success response is returned"
        resp.status == OK.value()
        resp.json != null
        resp.json.organisationId == orgId.toString()
        resp.json.name == newName
        resp.json.name != originalName
    }

    void "test delete organisation"() {
        given: "the UUID of a company"
        def orgId = Organisation.first().organisationId
        def originalOrgCount = Organisation.count()
        restBuilder().get("${baseUrl}organisations/$orgId", {
            accept("application/json")
        }).status == 200

        when: "a company sends a delete request"
        def resp = restBuilder().delete("${baseUrl}organisations/$orgId")

        then: "a success response is returned"
        resp.status == NO_CONTENT.value()
        restBuilder().get("${baseUrl}organisations/$orgId", {
            accept("application/json")
        }).status == 404
    }
}
