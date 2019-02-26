package rd.professional.web.command

import groovy.transform.ToString
import rd.professional.domain.Status

@ToString(includeNames = true, includeFields = true)
class OrganisationUpdateCommand {
    String name
    String url
    String sraId
    DxAddressCommand dxAddress

    List<AddAccountCommand> pbaAccounts
    List<AddDomainCommand> domains

    ContactInformationCommand address
    Status status
}