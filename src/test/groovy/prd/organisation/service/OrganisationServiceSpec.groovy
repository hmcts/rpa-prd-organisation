package prd.organisation.service

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import prd.organisation.domain.*
import prd.organisation.web.OrganisationRegistrationCommand
import spock.lang.Specification

class OrganisationServiceSpec extends Specification implements ServiceUnitTest<OrganisationService>, DataTest {

    def setup() {
        mockDomains Address, Organisation, PaymentAccount, ProfessionalUser, Domain
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
        Address.count == 0
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
        Address.count == 0
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
        Address.count == 0
    }

    void "test reg command with missing first name throws"() {
        when:
        service.registerOrganisation(new OrganisationRegistrationCommand(
                name: "ACME Inc.",
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
        Address.count == 0
    }

    void "test reg command with missing last name throws"() {
        when:
        service.registerOrganisation(new OrganisationRegistrationCommand(
                name: "ACME Inc.",
                firstName: "Foo",
                email: "foo@bar.com"
        ))

        then: "an exception is thrown"
        thrown RuntimeException

        and: "no data is persisted"
        Organisation.count == 0
        ProfessionalUser.count == 0
        PaymentAccount.count == 0
        Domain.count == 0
        Address.count == 0
    }

    void "test reg command with missing email throws"() {
        when:
        service.registerOrganisation(new OrganisationRegistrationCommand(
                name: "ACME Inc.",
                firstName: "Foo",
                lastName: "Barton"
        ))

        then: "an exception is thrown"
        thrown RuntimeException

        and: "no data is persisted"
        Organisation.count == 0
        ProfessionalUser.count == 0
        PaymentAccount.count == 0
        Domain.count == 0
        Address.count == 0
    }

    void "test reg command with only mandatory fields set stores org and a user"() {
        given:
        def name = "ACME Inc."
        def firstName = "Foo"
        def lastName = "Barton"
        def email = "foo@bar.com"

        when:
        service.registerOrganisation(new OrganisationRegistrationCommand(
                name: name,
                firstName: firstName,
                lastName: lastName,
                email: email
        ))

        then: "an exception is not thrown"
        notThrown Exception

        and: "only the organisation and a single user are persisted"
        Organisation.count == 1
        Organisation.findAll()[0].name == name
        ProfessionalUser.count == 1
        ProfessionalUser user = ProfessionalUser.findAll()[0]
        verifyAll {
            user.firstName == firstName
            user.lastName == lastName
            user.emailId == email
        }
        PaymentAccount.count == 0
        Domain.count == 0
        Address.count == 0
    }

    void "test reg command with mandatory and accounts fields set stores org, accounts and a user"() {
        given:
        def name = "ACME Inc."
        def firstName = "Foo"
        def lastName = "Barton"
        def email = "foo@bar.com"
        def pbaAccounts = "123,321"

        when:
        service.registerOrganisation(new OrganisationRegistrationCommand(
                name: name,
                firstName: firstName,
                lastName: lastName,
                email: email,
                pbaAccounts: pbaAccounts
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
        PaymentAccount.findAll().pbaNumber == ["123", "321"]
        Domain.count == 0
        Address.count == 0
    }

    void "test reg command with mandatory and domains fields set stores org, domains and a user"() {
        given:
        def name = "ACME Inc."
        def firstName = "Foo"
        def lastName = "Barton"
        def email = "foo@bar.com"
        def domains = "www.foo.com,www.bar.com"

        when:
        service.registerOrganisation(new OrganisationRegistrationCommand(
                name: name,
                firstName: firstName,
                lastName: lastName,
                email: email,
                domains: domains
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
        Address.count == 0
    }

    void "test reg command with mandatory and address fields set stores org, address and a user"() {
        given:
        def name = "ACME Inc."
        def firstName = "Foo"
        def lastName = "Barton"
        def email = "foo@bar.com"
        def houseNoBuildingName = "Flat 7"
        def addressLine1 = "Baz Towers"
        def addressLine2 = "Foo Street"
        def townCity = "Bar upon Thames"
        def county = "Surrey"
        def postcode = "F00 BAR"

        when:
        service.registerOrganisation(new OrganisationRegistrationCommand(
                name: name,
                firstName: firstName,
                lastName: lastName,
                email: email,
                houseNoBuildingName: houseNoBuildingName,
                addressLine1: addressLine1,
                addressLine2: addressLine2,
                townCity: townCity,
                county: county,
                postcode: postcode
        ))

        then: "an exception is not thrown"
        notThrown Exception

        and: "the organisation, two domains and a single user are persisted"
        Organisation.count == 1
        Organisation org = Organisation.findAll()[0]
        org.name == name
        org.addresses.size() == 1
        ProfessionalUser.count == 1
        ProfessionalUser user = ProfessionalUser.findAll()[0]
        verifyAll {
            user.firstName == firstName
            user.lastName == lastName
            user.emailId == email
        }
        Address.count == 1
        Address address = Address.findAll()[0]
        verifyAll {
            address.houseNoBuildingName == houseNoBuildingName
            address.addressLine1 == addressLine1
            address.addressLine2 == addressLine2
            address.townCity == townCity
            address.county == county
            address.postcode == postcode
        }
        PaymentAccount.count == 0
        Domain.count == 0
    }

    void "test reg command with all fields set stores org, address, accounts, domains and a user"() {
        given:
        def name = "ACME Inc."
        def firstName = "Foo"
        def lastName = "Barton"
        def email = "foo@bar.com"
        def houseNoBuildingName = "Flat 7"
        def addressLine1 = "Baz Towers"
        def addressLine2 = "Foo Street"
        def townCity = "Bar upon Thames"
        def county = "Surrey"
        def postcode = "F00 BAR"
        def domains = "www.foo.com,www.bar.com"
        def pbaAccounts = "123,321"

        when:
        service.registerOrganisation(new OrganisationRegistrationCommand(
                name: name,
                firstName: firstName,
                lastName: lastName,
                email: email,
                domains: domains,
                pbaAccounts: pbaAccounts,
                houseNoBuildingName: houseNoBuildingName,
                addressLine1: addressLine1,
                addressLine2: addressLine2,
                townCity: townCity,
                county: county,
                postcode: postcode
        ))

        then: "an exception is not thrown"
        notThrown Exception

        and: "the organisation, two domains and a single user are persisted"
        Organisation.count == 1
        Organisation org = Organisation.findAll()[0]
        org.name == name
        org.addresses.size() == 1
        org.accounts.size() == 2
        org.domains.size() == 2
        ProfessionalUser.count == 1
        ProfessionalUser user = ProfessionalUser.findAll()[0]
        verifyAll {
            user.firstName == firstName
            user.lastName == lastName
            user.emailId == email
        }
        Address.count == 1
        Address address = Address.findAll()[0]
        verifyAll {
            address.houseNoBuildingName == houseNoBuildingName
            address.addressLine1 == addressLine1
            address.addressLine2 == addressLine2
            address.townCity == townCity
            address.county == county
            address.postcode == postcode
        }
        PaymentAccount.count == 2
        PaymentAccount.findAll().pbaNumber == ["123", "321"]
        Domain.count == 2
        Domain.findAll().host == ["www.foo.com", "www.bar.com"]
    }
}