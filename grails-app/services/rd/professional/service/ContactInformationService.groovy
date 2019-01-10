package rd.professional.service

import grails.gorm.transactions.Transactional
import rd.professional.domain.ContactInformation

import javax.xml.ws.http.HTTPException

@Transactional
class ContactInformationService {

    OrganisationService organisationService
    UsersService usersService

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
        if (!uuid || uuid == "null")
            return null
        ContactInformation.where {
            contactId == uuid
        }.find()
    }

    def deleteContact(def userId) {
        def contact = ContactInformation.where {
            user.userId == userId
        }.find()
        if (contact)
            contact.delete(flush: true)
        else
            throw new HTTPException(404)
    }


}
