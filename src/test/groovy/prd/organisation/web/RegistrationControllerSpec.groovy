package prd.organisation.web

import grails.testing.web.controllers.ControllerUnitTest
import prd.organisation.service.RegistrationService
import spock.lang.Shared
import spock.lang.Specification

class RegistrationControllerSpec extends Specification implements ControllerUnitTest<RegistrationController>{

    @Shared
    private cmd = new OrganisationRegistrationCommand(
            name: "ACME Inc.",
            firstName: "Foo",
            lastName: "Barton",
            email: "foo@bar.com"
    )

    void "test exception from service causes 400 response"() {
        given: "a mocked RegistrationService that will throw an exception"
        def mockRegistrationService = Mock(RegistrationService)
        mockRegistrationService.registerOrganisation(cmd) >> { c ->
            throw new RuntimeException("Someone set us up the bomb!")
        }
        controller.registrationService = mockRegistrationService

        when: "a company sends a registration request"
        controller.register(cmd)

        then: "an error response is returned"
        response.status == 400
        response.text == "Someone set us up the bomb!"
    }

    void "test service gives 201 response"() {
        given: "a mocked RegistrationService"
        def mockRegistrationService = Stub(RegistrationService)
        controller.registrationService = mockRegistrationService

        when: "a company sends a registration request"
        controller.register(cmd)

        then: "a created response is returned"
        response.status == 201
    }
}