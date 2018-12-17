package rd.professional.web

import grails.validation.Validateable
import groovy.transform.ToString
import io.swagger.annotations.ApiModelProperty
import org.springframework.validation.Errors

@ToString(includeNames = true, includeFields = true)
class ContactInformationCommand implements Validateable {

    String address

    @ApiModelProperty(hidden = true)
    Errors errors

    static constraints = {
        address nullable: false
    }
}
