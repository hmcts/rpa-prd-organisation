package rd.professional.domain

import grails.rest.Resource
import io.swagger.annotations.ApiModelProperty

@Resource(readOnly = false, formats = ['json'])
class PaymentAccount {

    @ApiModelProperty(hidden = true)
    Long id
    UUID paymentAccountId = UUID.randomUUID()

    String pbaNumber

    @ApiModelProperty(hidden = true)
    Organisation organisation
    @ApiModelProperty(hidden = true)
    ProfessionalUser user

    static belongsTo = [
            organisation: Organisation,
            user: ProfessionalUser
    ]

    static constraints = {
        pbaNumber nullable: false, unique: true
        paymentAccountId nullable: false, unique: true
        user nullable: true
        organisation nullable: true
    }

    static mapping = {
        paymentAccountId type: 'uuid-binary', length: 16
    }

}
