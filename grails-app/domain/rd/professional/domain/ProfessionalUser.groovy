package rd.professional.domain

import io.swagger.annotations.ApiModelProperty

class ProfessionalUser {

    @ApiModelProperty(hidden = true)
    Long id
    UUID userId = UUID.randomUUID()
    String emailId
    String firstName
    String lastName
    Status status = Status.PENDING

    @ApiModelProperty(hidden = true)
    Organisation organisation

    static belongsTo = [organisation: Organisation]

    static hasMany = [
            contacts: ContactInformation,
            accounts: PaymentAccount
    ]

    static constraints = {
        emailId nullable: false, unique: true
        firstName nullable: false
        lastName nullable: false
        status nullable: false
        userId nullable: false, unique: true
    }

    static mapping = {
        id generator: 'identity'
        userId type: 'uuid-binary', length: 16
    }
}
