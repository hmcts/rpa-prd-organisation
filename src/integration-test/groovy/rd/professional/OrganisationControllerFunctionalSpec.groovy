package rd.professional

import geb.spock.GebSpec
import grails.converters.JSON
import grails.gorm.transactions.Rollback
import grails.plugins.rest.client.RestBuilder
import grails.testing.mixin.integration.Integration
import rd.professional.domain.Organisation
import spock.lang.Shared

import static org.springframework.http.HttpStatus.*

@Integration
@Rollback
class OrganisationControllerFunctionalSpec extends GebSpec {

    @Shared
    def organisation

    RestBuilder restBuilder() {
        new RestBuilder()
    }

    Organisation getOrg() {
        if (!organisation) {
            organisation = Organisation.where {
                name == "Organisation Inc."
            }.find()
        }
        return organisation
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
                name = "Organisation Inc."
                superUser = {
                    firstName = "Foo"
                    lastName = "Barton"
                    email = "foo@barorganisation.com"
                }
            }
        })

        then: "a created response is returned"
        resp.status == CREATED.value()
        resp.json != null && resp.json.id != null
    }

    void "test DX number and exchange can be set and retrieved"() {
        given: "a company sends a registration request with DX number and exchange set"
        def requestJson = [
                name     : "DX Details Ltd.",
                dxAddress: [
                        dxNumber  : "0123456789abc",
                        dxExchange: "0123456789abcdef0123"
                ],
                superUser: [
                        firstName: "Foo",
                        lastName : "Barton",
                        email    : "foo@bardxdetails.com"
                ]
        ] as JSON
        def resp = restBuilder().post("${baseUrl}organisations", {
            accept("application/json")
            contentType("application/json")
            json(requestJson)
        })

        expect: "the DX number and exchange are returned"
        resp.status == CREATED.value()
        def orgId = resp.json.id
        def dxAddress = resp.json.dxAddress
        dxAddress.dxNumber == "0123456789abc"
        dxAddress.dxExchange == "0123456789abcdef0123"

        and: "a GET request for the organisation also returns the DX details"
        def resp2 = restBuilder().get("${baseUrl}organisations/$orgId", { accept("application/json") })
        def dxAddress2 = resp2.json.dxAddress
        dxAddress2.dxNumber == "0123456789abc"
        dxAddress2.dxExchange == "0123456789abcdef0123"

        cleanup:
        restBuilder().delete("${baseUrl}organisations/$orgId")
    }

    void "test GET organisations returns a list of all organisations"() {
        given: "a second company is added to the database"
        def resp = restBuilder().post("${baseUrl}/organisations", {
            accept("application/json")
            contentType("application/json")
            json {
                name = "Aperture Science Organisation"
                superUser = {
                    firstName = "Cave"
                    lastName = "Johnson"
                    email = "cjohnson@aperturescienceorganisation.com"
                }
            }
        })
        def orgId2 = resp.json.id

        when: "a GET request is sent"
        resp = restBuilder().get("${baseUrl}organisations", {
            accept("application/json")
        })

        then: "a list of organisations is returned"
        resp.status == OK.value()
        def json = resp.json
        json.size() >= 2
        json.name.contains("Organisation Inc.")
        json.name.contains("Aperture Science Organisation")

        and: "Remove the no longer needed org"
        Organisation.where {
            organisationId == orgId2
        }.find().delete()
    }

    void "test GET organisations/uuid returns details of that organisation"() {
        given: "the UUID of a company"
        def organisation = getOrg()
        def orgId = organisation.organisationId
        def name = organisation.name

        when:
        def resp = restBuilder().get("${baseUrl}organisations/$orgId", {
            accept("application/json")
        })

        then:
        resp.status == 200
        resp.json.name == name
    }

    void "test update organisation"() {
        given: "the UUID of a company"
        def org = getOrg()
        def orgId = org.organisationId
        def originalName = org.name
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
        resp.json.id == orgId.toString()
        resp.json.name == newName
        resp.json.name != originalName
    }

    void "test delete organisation"() {
        given: "the UUID of a company"
        def org = getOrg()
        def orgId = org.organisationId
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

    void "test create organisation with more details"() {
        String requestJson = '{\n' +
                '  "name": "test",\n' +
                '  "url": "http://test.com",\n' +
                '  "superUser": {\n' +
                '    "firstName": "test",\n' +
                '    "lastName": "test",\n' +
                '    "email": "test@test.com",\n' +
                '    "pbaAccount": [\n' +
                '      {\n' +
                '        "pbaNumber": "PBA123"\n' +
                '      }\n' +
                '    ],\n' +
                '    "address": {\n' +
                '      "houseNoBuildingName": "test",\n' +
                '      "townCity": "London",\n' +
                '      "postcode": "SW1 123"\n' +
                '    }\n' +
                '  },\n' +
                '  "pbaAccounts": [\n' +
                '    {\n' +
                '      "pbaNumber": "PBA123"\n' +
                '    },\n' +
                '    {\n' +
                '      "pbaNumber": "PBA456"\n' +
                '    }\n' +
                '  ],\n' +
                '  "domains": [\n' +
                '    {\n' +
                '      "domain": "test.com"\n' +
                '    }\n' +
                '  ],\n' +
                '  "address": {\n' +
                '    "houseNoBuildingName": "test2",\n' +
                '    "townCity": "London",\n' +
                '    "postcode": "SW1 123"\n' +
                '  }\n' +
                '}'
        when: "a company sends a registration request"
        def resp = restBuilder().post("${baseUrl}organisations", {
            accept("application/json")
            contentType("application/json")
            body(requestJson)
        })

        then:
        println(resp.body)
        resp.status == 201

        and:
        restBuilder().delete("${baseUrl}organisations/$resp.json.organisationId")
    }

    void "test create organisation with test data"() {
        String requestJson = '{\n' +
                '  "name": "SolicitorOrg2",\n' +
                '  "url": "www.bbc.co.uk",\n' +
                '  "sraId": "A0002",\n' +
                '  "superUser": {\n' +
                '    "firstName": "Nasim",\n' +
                '    "lastName": "Lnasim",\n' +
                '    "email": "nasim_fr_sol@mailinator.com"\n' +
                '  },\n' +
                '  "pbaAccounts": [\n' +
                '    {\n' +
                '      "pbaNumber": "PBA0000011"\n' +
                '    },\n' +
                '    {\n' +
                '      "pbaNumber": "PBA0000012"\n' +
                '    },\n' +
                '    {\n' +
                '      "pbaNumber": "PBA0000014"\n' +
                '    }\n' +
                '  ],\n' +
                '  "domains": [\n' +
                '    {\n' +
                '      "domain": ""\n' +
                '    }\n' +
                '  ],\n' +
                '  "address": [\n' +
                '    {\n' +
                '      "houseNoBuildingName": "66",\n' +
                '      "addressLine1": "The Strand",\n' +
                '      "addressLine2": "asd",\n' +
                '      "townCity": "Westminister",\n' +
                '      "postcode": "W5T4BQ",\n' +
                '      "county": "London",\n' +
                '      "country": "uk"\n' +
                '    }\n' +
                '  ]\n' +
                '}'
        when: "a company sends a registration request"
        def resp = restBuilder().post("${baseUrl}organisations", {
            accept("application/json")
            contentType("application/json")
            body(requestJson)
        })

        then: "the organisation is created"
        println(resp.body)
        resp.status == 201

        and: "the domain is taken from the superUser's email"
        resp.json.domains.contains "mailinator.com"

        and:
        restBuilder().delete("${baseUrl}organisations/$resp.json.organisationId")
    }

}
