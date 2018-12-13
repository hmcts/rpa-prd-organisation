package rd.professional

import geb.spock.GebSpec
import grails.gorm.transactions.Rollback
import grails.plugins.rest.client.RestBuilder
import grails.testing.mixin.integration.Integration
import grails.testing.spock.OnceBefore
import org.junit.AfterClass
import rd.professional.domain.Organisation

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

@Integration
@Rollback
class SearchControllerFunctionalSpec extends GebSpec {

    @OnceBefore
    void setupOrg() {
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
    }

    @AfterClass
    void deleteData() {
        Organisation.findAll()*.delete()
    }

    void "Test search for payment accounts by email"() {
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
        when: "a search request is made using the wrong email address"
        def resp = restBuilder().get("${baseUrl}organisations/pba/baz@qux.com", {
            accept("application/json")
        })

        then: "an error is returned"
        resp.status == NOT_FOUND.value()
    }

    void "Test search for payment accounts by email with no email given"() {
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
