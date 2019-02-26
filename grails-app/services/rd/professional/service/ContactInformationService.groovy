package rd.professional.service

import grails.gorm.transactions.Transactional
import rd.professional.domain.ContactInformation

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
}
