package rd.professional.web

import grails.testing.gorm.DataTest
import grails.testing.spock.OnceBefore
import grails.testing.web.controllers.ControllerUnitTest
import org.junit.Before
import rd.professional.domain.ContactInformation
import rd.professional.domain.Organisation
import rd.professional.domain.PaymentAccount
import rd.professional.domain.ProfessionalUser
import rd.professional.domain.Status
import rd.professional.service.AccountsService
import spock.lang.Shared
import spock.lang.Specification

class SearchControllerSpec extends Specification implements ControllerUnitTest<SearchController>, DataTest {

    @Shared
    private user
    @Shared
    private address
    @Shared
    private account

    private Organisation addOrganisation() {
        def org = new Organisation(
                name: "ACME Inc.",
                url: "http://bar.com",
                sraId: "sra",
                sraRegulated: true,
                companyNumber: "12345",
        )
        org.addToContacts(address)
        org.addToUsers(user)
        org.addToAccounts(account)
        if (!org.save(flush: true)) {
                org.errors.allErrors.each {
                    println it
                }
        }
        return org
    }

    private Organisation addOrganisation2() {
        def org = new Organisation(
                name: "Aperture Science",
                url: "http://baz.com",
                sraId: "srb",
                sraRegulated: true,
                companyNumber: "12346",
        )
        org.addToContacts(new ContactInformation(
                address: "{}"
        ))
        org.addToUsers(new ProfessionalUser(
                emailId: "foo@baz.com",
                firstName: "Cave",
                lastName: "Johnson",
                status: Status.APPROVED
        ))
        org.addToAccounts(new PaymentAccount(pbaNumber: "54322"))
        org.save(flush: true)
    }

    void setupSpec() {
        mockDomains Organisation, ProfessionalUser, ContactInformation, PaymentAccount

        user = new ProfessionalUser(
                emailId: "foo@bar.com",
                firstName: "Foo",
                lastName: "Barton",
                status: Status.APPROVED
        )
        address = new ContactInformation(
                address: "{}"
        )
        account = new PaymentAccount(pbaNumber: "54321")
    }

    @Before
    void injectService() {
        controller.accountsService = new AccountsService()
    }

    void "test get approved when none exist"() {
        when:
        controller.approvedOrganisations()

        then:
        response.status == 200
        response.json == []
    }

    void "test get pending when none exist"() {
        when:
        controller.pendingOrganisations()

        then:
        response.status == 200
        response.json == []
    }

    void "test get approved"() {
        given:
        def org = addOrganisation()
        org.setStatus(Status.APPROVED)
        org.save()

        when:
        controller.approvedOrganisations()

        then:
        response.status == 200
        response.json.organisationName == [org.name]
    }

    void "test get pending"() {
        given:
        def org = addOrganisation()

        when:
        controller.pendingOrganisations()

        then:
        response.status == 200
        response.json.organisationName == [org.name]
    }

    void "test get approved multiple"() {
        given:
        def org = addOrganisation()
        org.setStatus(Status.APPROVED)
        org.save()
        def org2 = addOrganisation2()
        org2.setStatus(Status.APPROVED)
        org2.save()

        when:
        controller.approvedOrganisations()

        then:
        response.status == 200
        response.json.organisationName == [org.name, org2.name]
    }

    void "test get pending multiple"() {
        given:
        def org = addOrganisation()
        def org2 = addOrganisation2()

        when:
        controller.pendingOrganisations()

        then:
        response.status == 200
        response.json.organisationName == [org.name, org2.name]
    }

    void "test get accounts by email not found"() {
        when:
        controller.accountsByEmail("unknown@wrong.com")

        then:
        response.status == 404
    }

    void "test get accounts by email"() {
        given:
        addOrganisation()

        when:
        controller.accountsByEmail(user.emailId)

        then:
        response.status == 200
        response.json.payment_accounts == [account.pbaNumber]
    }

    void "test get accounts by email multiple"() {
        given:
        def org = addOrganisation()
        def account2 = new PaymentAccount(pbaNumber: "32764")
        org.addToAccounts(account2)

        when:
        controller.accountsByEmail(user.emailId)

        then:
        response.status == 200
        response.json.payment_accounts == [account.pbaNumber, account2.pbaNumber]
    }
}
