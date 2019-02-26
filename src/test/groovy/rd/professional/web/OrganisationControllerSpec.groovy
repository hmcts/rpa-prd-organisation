package rd.professional.web


import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import org.springframework.http.HttpStatus
import rd.professional.domain.*
import rd.professional.service.ContactInformationService
import rd.professional.service.OrganisationService
import rd.professional.web.command.*
import spock.lang.Shared
import spock.lang.Specification

class OrganisationControllerSpec extends Specification implements ControllerUnitTest<OrganisationController>, DataTest {

    @Shared
    Organisation organisation

    @Shared
    ProfessionalUser user

    @Shared
    PaymentAccount account

    @Shared
    ContactInformation contactInformation

    @Shared
    Domain domain

    @Shared
    DxAddress dxAddress

    def setup() {
        mockDomains(Organisation, ProfessionalUser, PaymentAccount, ContactInformation, Domain, DxAddress)
        domain = new Domain(host: "qux.net")
        user = new ProfessionalUser(firstName: "Foo", lastName: "Barton", emailId: "foo@qux.net")
        account = new PaymentAccount(pbaNumber: "123456")
        contactInformation = new ContactInformation(houseNoBuildingName: "12", postcode: "AB12 3CD")
        dxAddress = new DxAddress(dxExchange: "2134", dxNumber: "32536")
        organisation = new Organisation(name: "QuxNet", url: "https://qux.net", sraId: "sraid", dxAddress: dxAddress)
        organisation.addToUsers(user)
        organisation.addToContacts(contactInformation)
        organisation.addToAccounts(account)
        organisation.addToDomains(domain)
        organisation.save(flush: true)

        controller.contactInformationService = new ContactInformationService()
        controller.organisationService = new OrganisationService()
    }

    def cleanup() {
    }

    void "test show"() {
        when:
        params['id'] = organisation.organisationId.toString()
        controller.show()

        then:
        response.status == HttpStatus.OK.value()
        response.json.id == organisation.organisationId.toString()
    }

    void "test show not found"() {
        when:
        params['id'] = UUID.randomUUID().toString()
        controller.show()

        then:
        response.status == HttpStatus.NOT_FOUND.value()
    }

    void "test show no id"() {
        when:
        controller.show()

        then:
        response.status == HttpStatus.NOT_FOUND.value()
    }

    void "test index"() {
        when:
        controller.index()

        then:
        response.status == HttpStatus.OK.value()
        response.json[0].id == organisation.organisationId.toString()
    }

    void "test delete"() {
        given:
        Organisation org2 = new Organisation(name: "RivalOrg")
        org2.save(flush: true)

        when:
        params['id'] = org2.organisationId.toString()
        controller.delete()

        then:
        response.status == HttpStatus.NO_CONTENT.value()
        Organisation.get(org2.organisationId) == null
    }

    void "test delete not found"() {
        when:
        params['id'] = UUID.randomUUID().toString()
        controller.delete()

        then:
        response.status == HttpStatus.NOT_FOUND.value()
    }

    void "test delete no id"() {
        when:
        controller.delete()

        then:
        response.status == HttpStatus.NOT_FOUND.value()
    }

    void "test update no body"() {
        when:
        params['id'] = organisation.organisationId.toString()
        controller.update()

        then:
        response.status == HttpStatus.OK.value()
    }

    void "test update no id"() {
        when:
        controller.update()

        then:
        response.status == HttpStatus.NOT_FOUND.value()
    }

    void "test update all fields set, no change"() {
        given:
        def orgUpdate = new OrganisationUpdateCommand(
                name: organisation.name,
                url: organisation.url,
                sraId: organisation.sraId,
                dxAddress: new DxAddressCommand(
                        dxNumber: dxAddress.dxNumber,
                        dxExchange: dxAddress.dxExchange
                ),
                pbaAccounts: Arrays.asList(new AddAccountCommand(pbaNumber: account.pbaNumber)),
                domains: Arrays.asList(new AddDomainCommand(domain: domain.host)),
                address: new ContactInformationCommand(
                        houseNoBuildingName: contactInformation.houseNoBuildingName,
                        postcode: contactInformation.postcode
                )
        )

        when:
        params['id'] = organisation.organisationId.toString()
        controller.update(orgUpdate)

        then:
        response.status == 200
        ContactInformation.count() == 1
        ProfessionalUser.count() == 1
        DxAddress.count() == 1
        PaymentAccount.count() == 1
        Domain.count() == 1
    }
}