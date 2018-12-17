package rd.professional.domain

import grails.rest.Resource
import io.swagger.annotations.ApiModelProperty
import rd.professional.web.SubclassRestfulController

import java.time.LocalDateTime

@Resource(readOnly = false, formats = ['json'], superClass = SubclassRestfulController)
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
            users    : ProfessionalUser,
            accounts : PaymentAccount,
            domains  : Domain,
            contacts : ContactInformation
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
        organisationId type: 'uuid-binary', length: 16
    }
}