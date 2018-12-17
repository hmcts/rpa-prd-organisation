package rd.professional.web

import grails.validation.Validateable
import groovy.transform.ToString
import io.swagger.annotations.ApiModelProperty
import org.springframework.validation.Errors

@ToString(includeNames = true, includeFields = true)
class OrganisationRegistrationCommand implements Validateable {
    String name
    String url
    String sraId

    UserRegistrationCommand superUser

    String pbaAccounts
    String domains

    ContactInformationCommand address

    @ApiModelProperty(hidden = true)
    Errors errors

    static constraints = {
        sraId nullable: true
        url nullable: true
        name nullable: false

        superUser nullable: false

        pbaAccounts nullable: true
        domains nullable: true

        address nullable: true
    }
}