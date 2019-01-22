package rd.professional.web.dto

import rd.professional.domain.ContactInformation

class OrganisationAddressDto {

    OrganisationAddressDto(ContactInformation data) {
        id = data.contactId
        address = data.address
        organisationId = data.organisation.organisationId
    }

    UUID id
    String address
    UUID organisationId
}
