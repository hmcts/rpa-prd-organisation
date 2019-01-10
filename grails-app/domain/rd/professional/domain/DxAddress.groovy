package rd.professional.domain

import grails.validation.Validateable
import io.swagger.annotations.ApiModelProperty
import org.springframework.validation.Errors

class DxAddress implements Validateable {

    @ApiModelProperty(hidden = true)
    Long id
    UUID dxAddressId = UUID.randomUUID()
    String dxNumber
    String dxExchange

    @ApiModelProperty(hidden = true)
    Errors errors

    static constraints = {
        dxNumber nullable: false, unique: 'dxExchange'
        dxExchange nullable: false, unique: 'dxNumber'
        dxAddressId nullable: false, unique: true
    }
}
