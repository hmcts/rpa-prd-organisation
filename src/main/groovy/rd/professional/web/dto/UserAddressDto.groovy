package rd.professional.web.dto

import rd.professional.domain.ContactInformation

class UserAddressDto {

    UserAddressDto(ContactInformation data) {
        id = data.contactId
        address = data.address
        userId = data.user.userId
    }

    UUID id
    String address
    UUID userId
}
