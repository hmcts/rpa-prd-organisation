package rd.professional.domain

import grails.rest.Resource
import io.swagger.annotations.ApiModelProperty
import rd.professional.web.SubclassRestfulController

@Resource(readOnly = false, formats = ['json'], superClass = SubclassRestfulController)
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
        userId type: 'uuid-binary', length: 16
    }
}
