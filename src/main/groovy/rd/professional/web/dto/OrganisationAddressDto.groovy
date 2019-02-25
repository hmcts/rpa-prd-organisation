package rd.professional.web.dto

import rd.professional.domain.ContactInformation

class OrganisationAddressDto {

    OrganisationAddressDto(ContactInformation data) {
        id = data.contactId
        houseNoBuildingName = data.houseNoBuildingName
        addressLine1 = data.addressLine1
        addressLine2 = data.addressLine2
        townCity = data.townCity
        county = data.county
        country = data.country
        postcode = data.postcode
        organisationId = data.organisation.organisationId
    }

    UUID id
    String houseNoBuildingName
    String addressLine1
    String addressLine2
    String townCity
    String county
    String country
    String postcode
    UUID organisationId
}
