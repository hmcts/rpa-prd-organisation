package rd.professional.web.command

import grails.validation.Validateable
import groovy.transform.ToString
import io.swagger.annotations.ApiModelProperty
import org.springframework.validation.Errors

@ToString(includeNames = true, includeFields = true)
class OrganisationRegistrationCommand implements Validateable {
    String name
    String url
    String sraId
    DxAddressCommand dxAddress

    UserRegistrationCommand superUser

    List<AddAccountCommand> pbaAccounts
    List<AddDomainCommand> domains

    String address

    @ApiModelProperty(hidden = true)
    Errors errors

    static constraints = {
        sraId nullable: true
        url nullable: true
        name nullable: false
        dxAddress nullable: true

        superUser nullable: false

        pbaAccounts nullable: true
        domains nullable: true

        address nullable: true
    }
}