package rd.professional

import geb.spock.GebSpec
import grails.plugins.rest.client.RestBuilder
import grails.testing.mixin.integration.Integration
import grails.transaction.Rollback

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

@Integration
@Rollback
class SearchControllerSpec extends GebSpec {

    void "Test search for payment accounts by email"() {
        given: "a company has been registered with two payment accounts"
        restBuilder().post("${baseUrl}organisations/register", {
            accept("application/json")
            contentType("application/json")
            json {
                name = "ACME Inc."
                firstName = "Foo"
                lastName = "Barton"
                email = "foo@bar.com"
                pbaAccounts = "123456,654321"
            }
        })

        when: "a search request is made using the email address"
        def resp = restBuilder().get("${baseUrl}organisations/pba/foo@bar.com", {
            accept("application/json")
        })

        then: "a list of the relevant accounts is returned"
        resp.status == OK.value()
        def pbas = resp.json.payment_accounts
        pbas && pbas.size() == 2
        pbas.contains "123456"
        pbas.contains "654321"
    }

    void "Test search for payment accounts by email that's not in the database"() {
        given: "a company has been registered with two payment accounts"
        restBuilder().post("${baseUrl}organisations/register", {
            accept("application/json")
            contentType("application/json")
            json {
                name = "ACME Inc."
                firstName = "Foo"
                lastName = "Barton"
                email = "foo@bar.com"
                pbaAccounts = "123456,654321"
            }
        })

        when: "a search request is made using the wrong email address"
        def resp = restBuilder().get("${baseUrl}organisations/pba/baz@qux.com", {
            accept("application/json")
        })

        then: "an error is returned"
        resp.status == NOT_FOUND.value()
    }

    void "Test search for payment accounts by email with no email given"() {
        given: "a company has been registered with two payment accounts"
        restBuilder().post("${baseUrl}organisations/register", {
            accept("application/json")
            contentType("application/json")
            json {
                name = "ACME Inc."
                firstName = "Foo"
                lastName = "Barton"
                email = "foo@bar.com"
                pbaAccounts = "123456,654321"
            }
        })

        when: "a search request is made using no email address"
        def resp = restBuilder().get("${baseUrl}organisations/pba", {
            accept("application/json")
        })

        then: "an error is returned"
        resp.status == NOT_FOUND.value()
    }

    RestBuilder restBuilder() {
        new RestBuilder()
    }
}
