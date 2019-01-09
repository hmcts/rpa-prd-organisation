package rd.professional.web.command

import grails.validation.Validateable
import groovy.transform.ToString
import io.swagger.annotations.ApiModelProperty
import org.springframework.validation.Errors

@ToString(includeNames = true, includeFields = true)
class AddDomainCommand implements Validateable {

    String domain

    @ApiModelProperty(hidden = true)
    Errors errors

    static constraints = {
        domain nullable: false
    }
}
