package rd.professional.domain

import io.swagger.annotations.ApiModelProperty

import java.time.LocalDateTime

class Organisation {

    @ApiModelProperty(hidden = true)
    Long id
    String name
    URL url
    String sraId
    UUID organisationId = UUID.randomUUID()

    boolean sraRegulated
    String companyNumber

    LocalDateTime lastUpdated
    Status status = Status.PENDING

    static hasMany = [
            users   : ProfessionalUser,
            accounts: PaymentAccount,
            domains : Domain,
            contacts: ContactInformation
    ]

    static constraints = {
        name nullable: false, unique: true
        sraId nullable: true
        url nullable: true
        companyNumber nullable: true
        organisationId nullable: false, unique: true
        status nullable: false
    }

    static mapping = {
        id generator: 'identity'
        organisationId type: 'uuid-binary', length: 16
    }
}