package rd.professional.service

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import rd.professional.domain.ContactInformation
import rd.professional.domain.Organisation
import rd.professional.domain.ProfessionalUser
import spock.lang.Shared
import spock.lang.Specification

class ContactInformationServiceSpec extends Specification implements ServiceUnitTest<ContactInformationService>, DataTest {

    @Shared
    ContactInformation address1

    @Shared
    ContactInformation address2

    @Shared
    Organisation org

    @Shared
    ProfessionalUser user

    def setup() {
        mockDomains(ContactInformation, Organisation, ProfessionalUser)
        address1 = new ContactInformation(
                houseNoBuildingName: "1",
                postcode: "AB1 2CD"
        )
        address2 = new ContactInformation(
                houseNoBuildingName: "2",
                postcode: "AB1 2CD"
        )
        org = new Organisation(name: "QuxNet")
        user = new ProfessionalUser(firstName: "Foo", lastName: "Barton", emailId: "foo@qux.net")
        org.addToContacts(address1)
        user.addToContacts(address2)
        org.addToUsers(user)
        org.save(flush: true)
    }

    void "test get contacts for org"() {
        when:
        def contacts = service.getContactsForOrg(org.organisationId)

        then:
        contacts[0].contactId == address1.contactId
    }

    void "test get contacts for org not found"() {
        when:
        def contacts = service.getContactsForOrg(UUID.randomUUID())

        then:
        contacts.size() == 0
    }

    void "test get contacts for user"() {
        when:
        def contacts = service.getContactsForUser(user.userId)

        then:
        contacts[0].contactId == address2.contactId
    }

    void "test get contacts for user not found"() {
        when:
        def contacts = service.getContactsForUser(UUID.randomUUID())

        then:
        contacts.size() == 0
    }

    void "test get contact"() {
        when:
        def contact = service.getContact(address1.contactId)

        then:
        contact.contactId == address1.contactId
    }

    void "test get contact not found"() {
        when:
        def contact = service.getContact(UUID.randomUUID())

        then:
        contact == null
    }
}