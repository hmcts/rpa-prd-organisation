package rd.professional.service

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import rd.professional.domain.Organisation
import rd.professional.domain.PaymentAccount
import rd.professional.domain.ProfessionalUser
import spock.lang.Shared
import spock.lang.Specification

class AccountsServiceSpec extends Specification implements ServiceUnitTest<AccountsService>, DataTest {

    @Shared
    Organisation org

    def setup() {
        mockDomains(Organisation, PaymentAccount, ProfessionalUser)

        PaymentAccount account = new PaymentAccount(pbaNumber: "123456")
        PaymentAccount account2 = new PaymentAccount(pbaNumber: "654321")
        org = new Organisation(name: "QuxNet")
        ProfessionalUser user = new ProfessionalUser(firstName: "Foo", lastName: "Barton", emailId: "foo@qux.net")
        org.addToAccounts(account)
        org.addToAccounts(account2)
        org.addToUsers(user)
        user.addToAccounts(account)
        org.save(flush: true)
    }

    def cleanup() {
    }

    void "test find organisation accounts by email"() {
        when:
        def accounts = service.findOrgAccountsByEmail("foo@qux.net")

        then:
        accounts[0].pbaNumber == "123456"
        accounts[1].pbaNumber == "654321"
    }

    void "test find organisation accounts by email, user has no accounts"() {
        given:
        ProfessionalUser user2 = new ProfessionalUser(firstName: "Bar", lastName: "Fooster", emailId: "bar@qux.net")
        org.addToUsers(user2)
        org.save(flush: true)

        when:
        def accounts = service.findOrgAccountsByEmail("bar@qux.net")

        then:
        accounts[0].pbaNumber == "123456"
        accounts[1].pbaNumber == "654321"
    }

    void "test find organisation accounts by email, not found"() {
        when:
        def accounts = service.findOrgAccountsByEmail("baz@qux.net")

        then:
        accounts == null
    }
}
