package rd.professional.web

import grails.converters.JSON
import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import org.springframework.http.HttpStatus
import rd.professional.domain.ContactInformation
import rd.professional.domain.Organisation
import rd.professional.domain.PaymentAccount
import rd.professional.domain.ProfessionalUser
import rd.professional.service.OrganisationService
import rd.professional.service.UsersService
import spock.lang.Shared
import spock.lang.Specification

class ProfessionalUserControllerSpec extends Specification implements ControllerUnitTest<ProfessionalUserController>, DataTest {

    @Shared
    UUID orgId

    void setup() {
        mockDomains(ProfessionalUser, PaymentAccount, Organisation, ContactInformation)
        Organisation org = new Organisation()
        org.setName("Foo inc")
        def pba = new PaymentAccount(pbaNumber: "PBA123456")
        org.addToAccounts(pba)
        org.save()
        orgId = org.organisationId
        def orgService = Mock(OrganisationService)
        orgService.getForUuid(_) >> org
        controller.organisationService = orgService
        controller.usersService = new UsersService()
    }

    void "Test save with minimal data"() {
        when:
        request.json = [
                firstName: "Foo",
                lastName : "Barton",
                email    : "foobarton@baz.com"
        ] as JSON
        controller.save()

        then:
        ProfessionalUser.findByEmailId("foobarton@baz.com") != null
    }

    void "Test save with PBA account"() {
        when:
        request.json = [
                firstName : "Bar",
                lastName  : "Barton",
                email     : "barbarton@baz.com",
                pbaAccount: [
                        pbaNumber: "PBA123456"
                ]
        ] as JSON
        controller.save()

        then:
        ProfessionalUser.findByEmailId("barbarton@baz.com") != null
    }

    void "test save with PBA account not in org"() {
        when:
        request.json = [
                firstName: "Bar",
                lastName: "Barton",
                email: "barbarton@baz.com",
                pbaAccount: [
                        pbaNumber: "PBA3297593"
                ]
        ] as JSON
        controller.save()

        then:
        response.status == HttpStatus.NOT_FOUND.value()
    }
}