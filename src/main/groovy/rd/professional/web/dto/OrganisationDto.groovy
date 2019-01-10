package rd.professional.web.dto

import rd.professional.domain.Organisation

class OrganisationDto {

    def OrganisationDto(Organisation org) {
        id = org.organisationId
        name = org.name
        url = org.url
        sraId = org.sraId
        dxAddress = org.dxAddress ? new DxAddressDto(org.dxAddress) : null
        users = org.users.collect { it.userId }
        pbaAccounts = org.accounts.collect { it.pbaNumber }
        domains = org.domains.collect { it.host }
        contactInformationIds = org.contacts.collect { it.contactId }
    }

    UUID id
    String name
    String url
    String sraId
    DxAddressDto dxAddress
    List<UUID> users
    List<String> pbaAccounts
    List<String> domains
    List<UUID> contactInformationIds
}
