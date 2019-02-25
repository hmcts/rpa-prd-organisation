package rd.professional.web.dto

import rd.professional.domain.ContactInformation

class UserAddressDto {

    UserAddressDto(ContactInformation data) {
        id = data.contactId
        houseNoBuildingName = data.houseNoBuildingName
        addressLine1 = data.addressLine1
        addressLine2 = data.addressLine2
        townCity = data.townCity
        county = data.county
        country = data.country
        postcode = data.postcode
        userId = data.user.userId
    }

    UUID id
    String houseNoBuildingName
    String addressLine1
    String addressLine2
    String townCity
    String county
    String country
    String postcode
    UUID userId
}
