package rd.professional.web.command

import grails.validation.Validateable
import groovy.transform.ToString
import io.swagger.annotations.ApiModelProperty
import org.springframework.validation.Errors

@ToString(includeNames = true, includeFields = true)
class DxAddressCommand implements Validateable {

    String dxNumber
    String dxExchange

    @ApiModelProperty(hidden = true)
    Errors errors
    static constraints = {
        dxExchange nullable: false
        dxNumber nullable: false
    }
}
