package rd.professional.service

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import rd.professional.domain.ContactInformation
import rd.professional.domain.Domain
import rd.professional.domain.Organisation
import rd.professional.domain.PaymentAccount
import rd.professional.domain.ProfessionalUser
import spock.lang.Specification

class AccountsServiceSpec extends Specification implements ServiceUnitTest<AccountsService>, DataTest {

    def setup() {
        mockDomains ContactInformation, Organisation, PaymentAccount, ProfessionalUser, Domain
    }

    def cleanup() {

    }

    void "should find Organisation Accounts by email"() {

        given: "There's an an account with an e-mail within an organisation in the DB"

        String email = "test@email.com"
        URL url = new URL("https://somewhere.com")

        Organisation organisation = new Organisation(name: "test-org", url: url, sraId: "something")
        ProfessionalUser user = new ProfessionalUser(emailId: email, firstName: "xxx", lastName: "yyy")
        PaymentAccount expectedAcc = new PaymentAccount(pbaNumber: "123")
        assert expectedAcc.pbaNumber == "123"
        organisation.addToAccounts(expectedAcc)

        organisation.addToUsers(user)
        organisation.save()

        when: "The findOrgAccountsByEmail method is called"

        def accounts = service.findOrgAccountsByEmail(email)

        then: "The expected account record is returned"

        List<PaymentAccount> returnedAcc = accounts as List<PaymentAccount>
        returnedAcc.size() == 1

        //TODO - how come this is returned as an array??
        returnedAcc.get(0).pbaNumber.get(0) == expectedAcc.pbaNumber
    }

    void "should find no org accounts records if given e-mail is null"() {
        given: "There's an an account with an e-mail which is null within an organisation in the DB"

        URL url = new URL("https://somewhere.com")

        Organisation organisation = new Organisation(name: "test-org", url: url, sraId: "something")
        ProfessionalUser user = new ProfessionalUser(emailId: "test@email.com", firstName: "xxx", lastName: "yyy")
        PaymentAccount expectedAcc = new PaymentAccount(pbaNumber: "123")
        organisation.addToAccounts(expectedAcc)

        organisation.addToUsers(user)
        organisation.save()

        when: "The findOrgAccountsByEmail method is called"

        def accounts = service.findOrgAccountsByEmail(null)

        then: "The expected account record is returned"

        List<PaymentAccount> returnedAcc = accounts as List<PaymentAccount>
        returnedAcc.size() == 0

    }

    void "should find no org account records if given e-mail is not in DB"() {

        given: "There's an an account with an e-mail within an organisation in the DB"

        String email = "test@email.com"
        URL url = new URL("https://somewhere.com")

        Organisation organisation = new Organisation(name: "test-org", url: url, sraId: "something")
        ProfessionalUser user = new ProfessionalUser(emailId: email, firstName: "xxx", lastName: "yyy")
        PaymentAccount expectedAcc = new PaymentAccount(pbaNumber: "123")
        assert expectedAcc.pbaNumber == "123"
        organisation.addToAccounts(expectedAcc)

        organisation.addToUsers(user)
        organisation.save()

        when: "The findOrgAccountsByEmail method is called"

        def accounts = service.findOrgAccountsByEmail("another@email.com")

        then: "The expected account record is returned"

        List<PaymentAccount> returnedAcc = accounts as List<PaymentAccount>
        returnedAcc.size() == 0

    }

    void "should find User Accounts by email"() {

        given: "There's an an account with an e-mail within an organisation in the DB"

        String email = "test@email.com"
        URL url = new URL("https://somewhere.com")

        Organisation organisation = new Organisation(name: "test-org", url: url, sraId: "something")
        ProfessionalUser user = new ProfessionalUser(emailId: email, firstName: "xxx", lastName: "yyy")
        PaymentAccount paymentAccount = new PaymentAccount(pbaNumber: "123")
        Domain domain = new Domain(host: "test-host")

        organisation.addToDomains(domain)
        organisation.addToAccounts(paymentAccount)
        organisation.addToUsers(user)

        organisation.save()

        when: "The findUserAccountsByEmail method is called"

        def accounts = service.findUserAccountsByEmail(email)

        then: "The expected account record is returned"

        List<PaymentAccount> returnedAcc = accounts as List<PaymentAccount>
        returnedAcc.size() == 1

        //TODO - how come this is returned as an array and not a string??
        returnedAcc.get(0).pbaNumber.get(0) == paymentAccount.pbaNumber

    }

    void "should find no user account records if given e-mail is null"() {
        given: "There's an an account with an e-mail which is null within an organisation in the DB"

        URL url = new URL("https://somewhere.com")

        Organisation organisation = new Organisation(name: "test-org", url: url, sraId: "something")
        ProfessionalUser user = new ProfessionalUser(emailId: "test@email.com", firstName: "xxx", lastName: "yyy")
        PaymentAccount expectedAcc = new PaymentAccount(pbaNumber: "123")
        organisation.addToAccounts(expectedAcc)

        organisation.addToUsers(user)
        organisation.save()

        when: "The findOrgAccountsByEmail method is called"

        def accounts = service.findUserAccountsByEmail(null)

        then: "The expected account record is returned"

        List<PaymentAccount> returnedAcc = accounts as List<PaymentAccount>
        returnedAcc.size() == 0

    }

    void "should find no user account records if given e-mail is not in DB"() {

        given: "There's an an account with an e-mail within an organisation in the DB"

        String email = "test@email.com"
        URL url = new URL("https://somewhere.com")

        Organisation organisation = new Organisation(name: "test-org", url: url, sraId: "something")
        ProfessionalUser user = new ProfessionalUser(emailId: email, firstName: "xxx", lastName: "yyy")
        PaymentAccount expectedAcc = new PaymentAccount(pbaNumber: "123")
        assert expectedAcc.pbaNumber == "123"
        organisation.addToAccounts(expectedAcc)

        organisation.addToUsers(user)
        organisation.save()

        when: "The findOrgAccountsByEmail method is called"

        def accounts = service.findUserAccountsByEmail("another@email.com")

        then: "The expected account record is returned"

        List<PaymentAccount> returnedAcc = accounts as List<PaymentAccount>
        returnedAcc.size() == 0

    }
}