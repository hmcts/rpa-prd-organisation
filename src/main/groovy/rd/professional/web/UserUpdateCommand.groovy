package rd.professional.web

import grails.validation.Validateable
import groovy.transform.ToString
import io.swagger.annotations.ApiModelProperty
import org.springframework.validation.Errors
import rd.professional.domain.Status

@ToString(includeNames = true, includeFields = true)
class UserUpdateCommand implements Validateable {
    String firstName
    String lastName
    String email
    Status status

    @ApiModelProperty(hidden = true)
    Errors errors

    static constraints = {
        firstName nullable: false
        lastName nullable: false
        email nullable: false
        status nullable: true
    }
}