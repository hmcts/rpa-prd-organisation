package rd.professional.domain

import io.swagger.annotations.ApiModelProperty

import javax.persistence.Id

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
            user        : ProfessionalUser
    ]

    static constraints = {
        pbaNumber nullable: false, unique: true
        paymentAccountId nullable: false, unique: true
        user nullable: true
        organisation nullable: true
    }

    static mapping = {
        id generator: 'identity'
        paymentAccountId type: 'uuid-binary', length: 16
    }

}
