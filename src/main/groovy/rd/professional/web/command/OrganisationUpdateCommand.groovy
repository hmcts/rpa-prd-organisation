package rd.professional.web.command

import grails.validation.Validateable
import groovy.transform.ToString
import io.swagger.annotations.ApiModelProperty
import org.springframework.validation.Errors
import rd.professional.domain.Status

@ToString(includeNames = true, includeFields = true)
class OrganisationUpdateCommand implements Validateable {
    String name
    String url
    String sraId

    @ApiModelProperty(hidden = true)
    Errors errors

    static constraints = {
        sraId nullable: true
        url nullable: true
        name nullable: true
    }
}