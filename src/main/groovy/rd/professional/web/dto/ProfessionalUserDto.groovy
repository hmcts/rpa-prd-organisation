package rd.professional.web.dto

import rd.professional.domain.ProfessionalUser
import rd.professional.domain.Status

class ProfessionalUserDto {

    def ProfessionalUserDto(ProfessionalUser user) {
        id = user.userId
        emailId = user.emailId
        firstName = user.firstName
        lastName = user.lastName
        status = user.status
        organisationId = user.organisation.organisationId
        pbaAccounts = user.accounts.collect { it.pbaNumber }
        contactInformationIds = user.contacts.collect { it.contactId }
    }

    UUID id
    String emailId
    String firstName
    String lastName
    Status status
    UUID organisationId
    List<String> pbaAccounts
    List<UUID> contactInformationIds
}
