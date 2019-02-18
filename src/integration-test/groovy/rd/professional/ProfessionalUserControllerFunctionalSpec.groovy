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
class ProfessionalUserControllerFunctionalSpec extends GebSpec {

    @Shared
    def orgId
    @Shared
    def userId

    @OnceBefore
    void setupOrg() {
        def resp = restBuilder().post("${baseUrl}organisations", {
            accept("application/json")
            contentType("application/json")
            json {
                name = "Professional User Inc."
                superUser = {
                    firstName = "Foo"
                    lastName = "Barton"
                    email = "foo@barprofessionaluser.com"
                }
                pbaAccounts = [{
                    pbaNumber = "PBAUSR1247"
                }]
            }
        })
        orgId = resp.json.id
        userId = resp.json.users[0]
    }

    @AfterClass
    void deleteData() {
        Organisation.where {
            organisationId == orgId
        }.find().delete()
    }

    void "Test POST new user"() {
        when: "a request is made to add an account to a user"
        def resp = restBuilder().post("${baseUrl}organisations/$orgId/users", {
            accept("application/json")
            contentType("application/json")
            json {
                firstName = "Phil"
                lastName = "Martens"
                email = "bar@barprofessionaluser.com"
                pbaAccount = {
                    pbaNumber = "PBAUSR1247"
                }
            }
        })

        then: "the user is added"
        resp.status == CREATED.value()
    }

    void "Test GET list of users belonging to an organisation"() {
        when: "a GET request is made to the org's /users endpoint"
        def resp = restBuilder().get("${baseUrl}organisations/$orgId/users", {
            accept("application/json")
        })

        then: "an list of users is returned"
        resp.status == OK.value()
        resp.json && resp.json.size() == 2
    }

    void "Test PUT an update works"() {
        when: "a PUT request is made to the org's /users endpoint with updated details"
        def resp = restBuilder().put("${baseUrl}organisations/$orgId/users/$userId", {
            accept("application/json")
            json {
                firstName = "Jenny"
                lastName = "Taylor"
            }
        })

        then: "a success response is returned"
        resp.status == OK.value()
    }

    void "Test GET a specific user works"() {
        when: "a GET request is made to the org's /users/$userId endpoint"
        def resp = restBuilder().get("${baseUrl}organisations/$orgId/users/$userId", {
            accept("application/json")
        })

        then: "a success response is returned"
        resp.status == OK.value()
        resp.json.firstName == "Jenny"
    }

    void "Test DELETE a user works"() {
        when: "a DELETE request is made"
        def resp = restBuilder().delete("${baseUrl}organisations/$orgId/users/$userId")
        def resp2 = restBuilder().get("${baseUrl}organisations/$orgId/users/$userId", {
            accept("application/json")
        })

        then: "a success response is returned"
        resp.status == NO_CONTENT.value()

        and: "we can no longer retrieve details about that user"
        resp2.status == 404
    }

    RestBuilder restBuilder() {
        new RestBuilder()
    }
}
