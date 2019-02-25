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
class OrganisationContactInformationControllerFunctionalSpec extends GebSpec {

    @Shared
    def orgId
    @Shared
    def contactId

    @OnceBefore
    void setupOrg() {
        def resp = restBuilder().post("${baseUrl}organisations", {
            accept("application/json")
            contentType("application/json")
            json {
                name = "Org Contact Inc."
                superUser = {
                    firstName = "Foo"
                    lastName = "Barton"
                    email = "foo@barorgcontact.com"
                }
                address = {
                    houseNoBuildingName = "wibble"
                    townCity = "London"
                    postcode = "20957"
                }
            }
        })
        orgId = resp.json.id
        contactId = resp.json.addresses[0].id
    }

    @AfterClass
    void deleteData() {
        Organisation.where {
            organisationId == orgId
        }.find().delete()
    }

    void "Test POST new address"() {
        when: "a request is made to add an address to an organisation"
        def resp = restBuilder().post("${baseUrl}organisations/$orgId/contacts", {
            accept("application/json")
            contentType("application/json")
            json {
                houseNoBuildingName = "wobble"
                townCity = "London"
                postcode = "20957"
            }
        })

        then: "the address is added"
        resp.status == CREATED.value()
    }

    void "Test GET list of addresses belonging to an organisation"() {
        when: "a GET request is made to the org's /contacts endpoint"
        def resp = restBuilder().get("${baseUrl}organisations/$orgId/contacts", {
            accept("application/json")
        })

        then: "a list of addresses is returned"
        resp.status == OK.value()
        resp.json && resp.json.size() == 2
    }

    void "Test DELETE an address works"() {
        when: "a DELETE request is made"
        def resp = restBuilder().delete("${baseUrl}organisations/$orgId/contacts/$contactId")

        then: "a success response is returned"
        resp.status == NO_CONTENT.value()
    }

    RestBuilder restBuilder() {
        new RestBuilder()
    }
}
