package rd.professional.web.command

import grails.validation.Validateable
import groovy.transform.ToString
import io.swagger.annotations.ApiModelProperty
import org.springframework.validation.Errors

@ToString(includeNames = true, includeFields = true)
class ContactInformationCommand implements Validateable {

    String houseNoBuildingName
    String addressLine1
    String addressLine2
    String townCity
    String county
    String country
    String postcode

    @ApiModelProperty(hidden = true)
    Errors errors

    static constraints = {
        address nullable: false
    }
}
