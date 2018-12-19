package rd.professional

import geb.spock.GebSpec
import grails.gorm.transactions.Rollback
import grails.plugins.rest.client.RestBuilder
import grails.testing.mixin.integration.Integration
import rd.professional.domain.Organisation

import static org.springframework.http.HttpStatus.*

@Integration
@Rollback
class OrganisationControllerFunctionalSpec extends GebSpec {

    RestBuilder restBuilder() {
        new RestBuilder()
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
        resp.json != null && resp.json.organisationId != null
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
        def orgId2 = resp.json.organisationId

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

    void "test create organisation with more details"() {
        String requestJson = '{\n' +
                '  "name": "test",\n' +
                '  "url": "http://test.com",\n' +
                '  "superUser": {\n' +
                '    "firstName": "test",\n' +
                '    "lastName": "test",\n' +
                '    "email": "test@test.com",\n' +
                '    "pbaAccounts": [\n' +
                '      {\n' +
                '        "pbaNumber": "PBA123"\n' +
                '      }\n' +
                '    ],\n' +
                '    "address": {\n' +
                '      "address": "test"\n' +
                '    }\n' +
                '  },\n' +
                '  "pbaAccounts": [\n' +
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
                '    "address": "test"\n' +
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
}
