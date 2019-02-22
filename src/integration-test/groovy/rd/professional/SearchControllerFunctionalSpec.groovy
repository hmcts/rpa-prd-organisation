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
class SearchControllerFunctionalSpec extends GebSpec {

    @Shared
    def orgId

    @OnceBefore
    void setupOrg() {
        def resp = restBuilder().post("${baseUrl}organisations", {
            accept("application/json")
            contentType("application/json")
            json {
                name = "Search Inc."
                superUser = {
                    firstName = "Foo"
                    lastName = "Barton"
                    email = "foo@barsearch.com"
                }
                pbaAccounts = [{
                                   pbaNumber = "123456Search"
                               },
                               {
                                   pbaNumber = "654321Search"
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

    void "Test search for payment accounts by email"() {
        when: "a search request is made using the email address"
        def resp = restBuilder().get("${baseUrl}search/pba/foo@barsearch.com", {
            accept("application/json")
        })

        then: "a list of the relevant accounts is returned"
        resp.status == OK.value()
        def pbas = resp.json.payment_accounts
        pbas && pbas.size() == 2
        pbas.contains "123456Search"
        pbas.contains "654321Search"
    }

    void "Test search for payment accounts by email that's not in the database"() {
        when: "a search request is made using the wrong email address"
        def resp = restBuilder().get("${baseUrl}search/pba/baz@qux.com", {
            accept("application/json")
        })

        then: "an error is returned"
        resp.status == NOT_FOUND.value()
    }

    void "Test search for payment accounts by email with no email given"() {
        when: "a search request is made using no email address"
        def resp = restBuilder().get("${baseUrl}search/pba", {
            accept("application/json")
        })

        then: "an error is returned"
        resp.status == NOT_FOUND.value()
    }

    void "Test search for user by email"() {
        when: "a search request is made using the email address"
        def resp = restBuilder().get("${baseUrl}search/users/foo@barsearch.com", {
            accept("application/json")
        })

        then: "the relevant user is returned"
        resp.status == OK.value()
        def user = resp.json
        user && user.firstName == "Foo"
    }

    void "Test search for user by email that's not in the database"() {
        when: "a search request is made using the wrong email address"
        def resp = restBuilder().get("${baseUrl}search/users/baz@qux.com", {
            accept("application/json")
        })

        then: "an error is returned"
        resp.status == NOT_FOUND.value()
    }

    void "Test search for user by email with no email given"() {
        when: "a search request is made using no email address"
        def resp = restBuilder().get("${baseUrl}search/users", {
            accept("application/json")
        })

        then: "an error is returned"
        resp.status == NOT_FOUND.value()
    }

    void "Test search for organisations by email"() {
        when: "a search request is made using the email address"
        def resp = restBuilder().get("${baseUrl}search/organisations/foo@barsearch.com", {
            accept("application/json")
        })

        then: "the relevant organisation is returned"
        resp.status == OK.value()
        def org = resp.json
        org && org.name == "Search Inc."
    }

    void "Test search for organisations by email that's not in the database"() {
        when: "a search request is made using the wrong email address"
        def resp = restBuilder().get("${baseUrl}search/organisations/baz@qux.com", {
            accept("application/json")
        })

        then: "an error is returned"
        resp.status == NOT_FOUND.value()
    }

    void "Test search for organisations by email with no email given"() {
        when: "a search request is made using no email address"
        def resp = restBuilder().get("${baseUrl}search/organisations", {
            accept("application/json")
        })

        then: "an error is returned"
        resp.status == NOT_FOUND.value()
    }

    RestBuilder restBuilder() {
        new RestBuilder()
    }
}
