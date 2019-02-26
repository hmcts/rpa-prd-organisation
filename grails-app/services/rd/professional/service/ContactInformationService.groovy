package rd.professional.service

import grails.gorm.transactions.Transactional
import rd.professional.domain.ContactInformation
import rd.professional.web.command.ContactInformationCommand

@Transactional
class ContactInformationService {

    def getContactsForOrg(def orgId) {
        ContactInformation.where {
            organisation.organisationId == orgId
        }.findAll()
    }

    def getContactsForUser(def userId) {
        ContactInformation.where {
            user.userId == userId
        }.findAll()
    }

    def getContact(def uuid) {
        ContactInformation.where {
            contactId == uuid
        }.find()
    }

    boolean doesAddressExist(ContactInformationCommand cmd) {
        ContactInformation.where {
            houseNoBuildingName == cmd.houseNoBuildingName
            addressLine1 == cmd.addressLine1
            addressLine2 == cmd.addressLine2
            townCity == cmd.townCity
            county == cmd.county
            country == cmd.country
            postcode == cmd.postcode
        }.count() > 0
    }
}
