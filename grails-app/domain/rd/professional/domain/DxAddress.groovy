package rd.professional.domain

import grails.validation.Validateable
import io.swagger.annotations.ApiModelProperty
import org.springframework.validation.Errors
import rd.professional.web.command.DxAddressCommand

class DxAddress implements Validateable {

    DxAddress(DxAddressCommand cmd) {
        this(
                dxExchange: cmd.dxExchange,
                dxNumber: cmd.dxNumber
        )
    }

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
