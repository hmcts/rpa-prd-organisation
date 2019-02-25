package rd.professional.web.dto

import rd.professional.domain.ProfessionalUser
import rd.professional.domain.Status

class ProfessionalUserDto {

    ProfessionalUserDto(ProfessionalUser user) {
        id = user.userId
        emailId = user.emailId
        firstName = user.firstName
        lastName = user.lastName
        status = user.status
        organisationId = user.organisation.organisationId
        pbaAccount = user.accounts ? user.accounts[0] : null
        addresses = user.contacts.collect { new UserAddressDto(it) }
    }

    UUID id
    String emailId
    String firstName
    String lastName
    Status status
    UUID organisationId
    String pbaAccount
    List<UserAddressDto> addresses
}
