package rd.professional.service

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import rd.professional.domain.*
import rd.professional.web.command.AddAccountCommand
import rd.professional.web.command.AddDomainCommand
import rd.professional.web.command.ContactInformationCommand
import rd.professional.web.command.DxAddressCommand
import rd.professional.web.command.OrganisationRegistrationCommand
import rd.professional.web.command.UserRegistrationCommand
import spock.lang.Specification

class OrganisationServiceSpec extends Specification implements ServiceUnitTest<OrganisationService>, DataTest {

    def setup() {
        mockDomains ContactInformation, Organisation, PaymentAccount, ProfessionalUser, Domain
    }

    void "test null registration command throws"() {
        when: "a call to registerOrganisation with a null argument is made"
        service.registerOrganisation null

        then: "a NPE is thrown"
        thrown NullPointerException

        and: "no data is persisted"
        Organisation.count == 0
        ProfessionalUser.count == 0
        PaymentAccount.count == 0
        Domain.count == 0
        ContactInformation.count == 0
    }

    void "test empty registration command throws"() {
        when: "a call to registerOrganisation with an empty OrganisationRegistrationCommand is made"
        service.registerOrganisation new OrganisationRegistrationCommand()

        then: "an exception is thrown"
        thrown RuntimeException

        and: "no data is persisted"
        Organisation.count == 0
        ProfessionalUser.count == 0
        PaymentAccount.count == 0
        Domain.count == 0
        ContactInformation.count == 0
    }

    void "test reg command with missing name throws"() {
        when:
        service.registerOrganisation(new OrganisationRegistrationCommand(
                firstName: "Foo",
                lastName: "Barton",
                email: "foo@bar.com"
        ))

        then: "an exception is thrown"
        thrown RuntimeException

        and: "no data is persisted"
        Organisation.count == 0
        ProfessionalUser.count == 0
        PaymentAccount.count == 0
        Domain.count == 0
        ContactInformation.count == 0
    }

    void "test reg command with missing first name throws"() {
        when:
        service.registerOrganisation(new OrganisationRegistrationCommand(
                name: "ACME Inc.",
                superUser: new UserRegistrationCommand(
                    lastName: "Barton",
                    email: "foo@bar.com"
                )
        ))

        then: "an exception is thrown"
        thrown RuntimeException

        and: "no data is persisted"
        Organisation.count == 0
        ProfessionalUser.count == 0
        PaymentAccount.count == 0
        Domain.count == 0
        ContactInformation.count == 0
    }

    void "test reg command with missing last name throws"() {
        when:
        service.registerOrganisation(new OrganisationRegistrationCommand(
                name: "ACME Inc.",
                superUser: new UserRegistrationCommand(
                        firstName: "Foo",
                        email: "foo@bar.com"
                )
        ))

        then: "an exception is thrown"
        thrown RuntimeException

        and: "no data is persisted"
        Organisation.count == 0
        ProfessionalUser.count == 0
        PaymentAccount.count == 0
        Domain.count == 0
        ContactInformation.count == 0
    }

    void "test reg command with missing email throws"() {
        when:
        service.registerOrganisation(new OrganisationRegistrationCommand(
                name: "ACME Inc.",
                superUser: new UserRegistrationCommand(
                        firstName: "Foo",
                        lastName: "Barton"
                )
        ))

        then: "an exception is thrown"
        thrown RuntimeException

        and: "no data is persisted"
        Organisation.count == 0
        ProfessionalUser.count == 0
        PaymentAccount.count == 0
        Domain.count == 0
        ContactInformation.count == 0
    }

    void "test reg command with only mandatory fields set stores org, a domain and a user"() {
        given:
        def name = "ACME Inc."
        def firstName = "Foo"
        def lastName = "Barton"
        def email = "foo@bar.com"

        when:
        service.registerOrganisation(new OrganisationRegistrationCommand(
                name: name,
                superUser: new UserRegistrationCommand(
                        firstName: firstName,
                        lastName: lastName,
                        email: email
                )
        ))

        then: "an exception is not thrown"
        notThrown Exception

        and: "only the organisation and a single user are persisted"
        Organisation.count == 1
        Organisation organisation = Organisation.findAll()[0]
        organisation.name == name
        organisation.users.size() == 1
        ProfessionalUser.count == 1
        ProfessionalUser user = ProfessionalUser.findAll()[0]
        verifyAll {
            user.firstName == firstName
            user.lastName == lastName
            user.emailId == email
        }
        PaymentAccount.count == 0
        Domain.count == 1
        ContactInformation.count == 0
    }

    void "test reg command with mandatory and accounts fields set stores org, domain, accounts and a user"() {
        given:
        def name = "ACME Inc."
        def firstName = "Foo"
        def lastName = "Barton"
        def email = "foo@bar.com"
        def pbaAccount1 = "1234"
        def pbaAccount2 = "4321"

        when:
        service.registerOrganisation(new OrganisationRegistrationCommand(
                name: name,
                superUser: new UserRegistrationCommand(
                        firstName: firstName,
                        lastName: lastName,
                        email: email
                ),
                pbaAccounts: Arrays.asList(new AddAccountCommand(pbaNumber: pbaAccount1), new AddAccountCommand(pbaNumber: pbaAccount2)),
        ))

        then: "an exception is not thrown"
        notThrown Exception

        and: "the organisation, two accounts and a single user are persisted"
        Organisation.count == 1
        Organisation org = Organisation.findAll()[0]
        org.name == name
        org.accounts.size() == 2
        ProfessionalUser.count == 1
        ProfessionalUser user = ProfessionalUser.findAll()[0]
        verifyAll {
            user.firstName == firstName
            user.lastName == lastName
            user.emailId == email
        }
        PaymentAccount.count == 2
        PaymentAccount.findAll().pbaNumber == ["1234", "4321"]
        Domain.count == 1
        ContactInformation.count == 0
    }

    void "test reg command with mandatory and domains fields set stores org, domains and a user"() {
        given:
        def name = "ACME Inc."
        def firstName = "Foo"
        def lastName = "Barton"
        def email = "foo@bar.com"
        def domain1 = "www.foo.com"
        def domain2 = "www.bar.com"

        when:
        service.registerOrganisation(new OrganisationRegistrationCommand(
                name: name,
                superUser: new UserRegistrationCommand(
                        firstName: firstName,
                        lastName: lastName,
                        email: email
                ),
                domains: Arrays.asList(new AddDomainCommand(domain: domain1), new AddDomainCommand(domain: domain2)),
        ))

        then: "an exception is not thrown"
        notThrown Exception

        and: "the organisation, two domains and a single user are persisted"
        Organisation.count == 1
        Organisation org = Organisation.findAll()[0]
        org.name == name
        org.domains.size() == 2
        ProfessionalUser.count == 1
        ProfessionalUser user = ProfessionalUser.findAll()[0]
        verifyAll {
            user.firstName == firstName
            user.lastName == lastName
            user.emailId == email
        }
        Domain.count == 2
        Domain.findAll().host == ["www.foo.com", "www.bar.com"]
        PaymentAccount.count == 0
        ContactInformation.count == 0
    }

    void "test reg command with mandatory and address fields set stores org, domain, address and a user"() {
        given:
        def name = "ACME Inc."
        def firstName = "Foo"
        def lastName = "Barton"
        def email = "foo@bar.com"
        def address = new ContactInformationCommand(
                houseNoBuildingName: "Flat 7",
                addressLine1: "Baz Towers",
                addressLine2: "Foo Street",
                townCity: "Bar upon Thames",
                county: "Surrey",
                country: "UK",
                postcode: "F00 BAR"
        )

        when:
        service.registerOrganisation(new OrganisationRegistrationCommand(
                name: name,
                superUser: new UserRegistrationCommand(
                        firstName: firstName,
                        lastName: lastName,
                        email: email
                ),
                address: address
        ))

        then: "an exception is not thrown"
        notThrown Exception

        and: "the organisation, a domain, an address and a single user are persisted"
        Organisation.count == 1
        Organisation org = Organisation.findAll()[0]
        org.name == name
        org.contacts.size() == 1
        ProfessionalUser.count == 1
        ProfessionalUser user = ProfessionalUser.findAll()[0]
        verifyAll {
            user.firstName == firstName
            user.lastName == lastName
            user.emailId == email
        }
        ContactInformation.count == 1
        ContactInformation contacts = ContactInformation.findAll()[0]
        contacts.houseNoBuildingName == "Flat 7"
        PaymentAccount.count == 0
        Domain.count == 1
    }

    void "test reg command with all fields set stores org, address, accounts, domains and a user"() {
        given:
        def name = "ACME Inc."
        def firstName = "Foo"
        def lastName = "Barton"
        def email = "foo@bar.com"
        def address = new ContactInformationCommand(
                houseNoBuildingName: "Flat 7",
                addressLine1: "Baz Towers",
                addressLine2: "Foo Street",
                townCity: "Bar upon Thames",
                county: "Surrey",
                country: "UK",
                postcode: "F00 BAR"
        )
        def domain1 = "www.foo.com"
        def domain2 = "www.bar.com"
        def pbaAccount1 = "1234"
        def pbaAccount2 = "4321"
        def dxNum = "dxnumber"
        def dxExch = "dxexchange"

        when:
        service.registerOrganisation(new OrganisationRegistrationCommand(
                name: name,
                superUser: new UserRegistrationCommand(
                        firstName: firstName,
                        lastName: lastName,
                        email: email
                ),
                dxAddress: new DxAddressCommand(dxNumber: dxNum, dxExchange: dxExch),
                domains: Arrays.asList(new AddDomainCommand(domain: domain1), new AddDomainCommand(domain: domain2)),
                pbaAccounts: Arrays.asList(new AddAccountCommand(pbaNumber: pbaAccount1), new AddAccountCommand(pbaNumber: pbaAccount2)),
                address: address
        ))

        then: "an exception is not thrown"
        notThrown Exception

        and: "the organisation, two domains and a single user are persisted"
        Organisation.count == 1
        Organisation org = Organisation.findAll()[0]
        org.name == name
        org.contacts.size() == 1
        org.accounts.size() == 2
        org.domains.size() == 2
        ProfessionalUser.count == 1
        ProfessionalUser user = ProfessionalUser.findAll()[0]
        verifyAll {
            user.firstName == firstName
            user.lastName == lastName
            user.emailId == email
        }
        ContactInformation.count == 1
        ContactInformation contacts = ContactInformation.findAll()[0]
        contacts.houseNoBuildingName == "Flat 7"
        PaymentAccount.count == 2
        PaymentAccount.findAll().pbaNumber == ["1234", "4321"]
        Domain.count == 2
        Domain.findAll().host == ["www.foo.com", "www.bar.com"]
        DxAddress.count == 1
        DxAddress dxAddress = DxAddress.findAll()[0]
        dxAddress.dxExchange == dxExch
        dxAddress.dxNumber == dxNum
    }
}