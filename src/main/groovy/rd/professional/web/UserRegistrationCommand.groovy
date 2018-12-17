package rd.professional.web

import grails.validation.Validateable
import groovy.transform.ToString
import io.swagger.annotations.ApiModelProperty
import org.springframework.validation.Errors

@ToString(includeNames = true, includeFields = true)
class UserRegistrationCommand implements Validateable {
    String firstName
    String lastName
    String email

    String pbaAccounts

    ContactInformationCommand address

    @ApiModelProperty(hidden = true)
    Errors errors

    static constraints = {
        firstName nullable: false
        lastName nullable: false
        email nullable: false

        pbaAccounts nullable: true

        address nullable: true
    }
}