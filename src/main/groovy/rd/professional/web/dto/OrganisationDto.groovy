package rd.professional.web.dto

import rd.professional.domain.Organisation

class OrganisationDto {

    def OrganisationDto(Organisation org) {
        id = org.organisationId
        name = org.name
        url = org.url
        sraId = org.sraId
        users = org.users.collect { it.userId }
        pbaAccounts = org.accounts.collect { it.pbaNumber }
        domains = org.domains.collect { it.host }
        addresses = org.contacts.collect { it.userId }
    }

    UUID id
    String name
    String url
    String sraId
    List<UUID> users
    List<String> pbaAccounts
    List<String> domains
    List<UUID> addresses
}
