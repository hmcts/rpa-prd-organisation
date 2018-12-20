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
class OrganisationAccountControllerFunctionalSpec extends GebSpec {

    @Shared
    def orgId

    @OnceBefore
    void setupOrg() {
        def resp = restBuilder().post("${baseUrl}organisations", {
            accept("application/json")
            contentType("application/json")
            json {
                name = "Org Account Inc."
                superUser = {
                    firstName = "Foo"
                    lastName = "Barton"
                    email = "foo@barorgaccount.com"
                }
                pbaAccounts = [{
                               pbaNumber = "123456ORG"
                           }]
            }
        })
        orgId = resp.json.organisationId
    }

    @AfterClass
    void deleteData() {
        def organisation = Organisation.where {
            organisationId == orgId
        }.find()
        if (organisation)
            organisation.delete()
    }

    void "Test POST new account"() {
        when: "a request is made to add an account to an organisation"
        def resp = restBuilder().post("${baseUrl}organisations/$orgId/pbas", {
            accept("application/json")
            contentType("application/json")
            json {
                pbaNumber = "234567ORG"
            }
        })

        then: "the account is added"
        resp.status == CREATED.value()
    }

    void "Test GET list of accounts belonging to an organisation"() {
        when: "a GET request is made to the org's /pbas endpoint"
        def resp = restBuilder().get("${baseUrl}organisations/$orgId/pbas", {
            accept("application/json")
        })

        then: "a list of accounts is returned"
        resp.status == OK.value()
        resp.json && resp.json.size() == 2
        resp.json.pbaNumber.contains "123456ORG"
        resp.json.pbaNumber.contains "234567ORG"
    }

    void "Test DELETE an account works"() {
        when: "a DELETE request is made"
        def resp = restBuilder().delete("${baseUrl}organisations/$orgId/pbas/234567ORG")

        then: "a success response is returned"
        resp.status == NO_CONTENT.value()
    }

    RestBuilder restBuilder() {
        new RestBuilder()
    }
}
