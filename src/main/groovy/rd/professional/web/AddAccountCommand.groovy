package rd.professional.web

import grails.validation.Validateable
import groovy.transform.ToString
import io.swagger.annotations.ApiModelProperty
import org.springframework.validation.Errors

@ToString(includeNames = true, includeFields = true)
class AddAccountCommand implements Validateable {

    String pbaNumber

    @ApiModelProperty(hidden = true)
    Errors errors

    static constraints = {
        pbaNumber nullable: false
    }
}
