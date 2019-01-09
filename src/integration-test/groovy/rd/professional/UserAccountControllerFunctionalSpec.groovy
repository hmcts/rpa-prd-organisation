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
class UserAccountControllerFunctionalSpec extends GebSpec {

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
                name = "User Account Inc."
                superUser = {
                    firstName = "Foo"
                    lastName = "Barton"
                    email = "foo@baruseraccount.com"
                    pbaAccounts = [{
                                       pbaNumber = "123456USR"
                                   }]
                }
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

    void "Test POST new account"() {
        when: "a request is made to add an account to a user"
        def resp = restBuilder().post("${baseUrl}organisations/$orgId/users/$userId/pbas", {
            accept("application/json")
            contentType("application/json")
            json {
                pbaNumber = "234567USR"
            }
        })

        then: "the account is added"
        resp.status == CREATED.value()
    }

    void "Test GET list of accounts belonging to a user"() {
        when: "a GET request is made to the org's /pbas endpoint"
        def resp = restBuilder().get("${baseUrl}organisations/$orgId/users/$userId/pbas", {
            accept("application/json")
        })

        then: "a list of accounts is returned"
        resp.status == OK.value()
        resp.json && resp.json.size() == 2
        resp.json.pbaNumber.contains "123456USR"
        resp.json.pbaNumber.contains "234567USR"
    }

    void "Test DELETE an account works"() {
        when: "a DELETE request is made"
        def resp = restBuilder().delete("${baseUrl}organisations/$orgId/users/$userId/pbas/234567USR")

        then: "a success response is returned"
        resp.status == NO_CONTENT.value()
    }

    RestBuilder restBuilder() {
        new RestBuilder()
    }
}
