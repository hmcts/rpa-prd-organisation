package rd.professional.web

import grails.testing.web.controllers.ControllerUnitTest
import rd.professional.service.OrganisationService
import spock.lang.Shared
import spock.lang.Specification

class OrganisationControllerSpec extends Specification implements ControllerUnitTest<OrganisationController> {

    @Shared
    private cmd = new OrganisationRegistrationCommand(
            name: "ACME Inc.",
            superUser: new UserRegistrationCommand(
                firstName: "Foo",
                lastName: "Barton",
                email: "foo@bar.com"
            )
    )

    void "test exception from service causes 400 response"() {
        given: "a mocked OrganisationService that will throw an exception"
        def mockOrganisationService = Mock(OrganisationService)
        mockOrganisationService.registerOrganisation(cmd) >> { c ->
            throw new RuntimeException("Someone set us up the bomb!")
        }
        controller.organisationService = mockOrganisationService

        when: "a company sends a registration request"
        controller.save(cmd)

        then: "an error response is returned"
        response.status == 400
        response.text == "Someone set us up the bomb!"
    }

    void "test service gives 201 response"() {
        given: "a mocked OrganisationService"
        def mockOrganisationService = Stub(OrganisationService)
        controller.organisationService = mockOrganisationService

        when: "a company sends a registration request"
        controller.save(cmd)

        then: "a created response is returned"
        response.status == 201
    }
}