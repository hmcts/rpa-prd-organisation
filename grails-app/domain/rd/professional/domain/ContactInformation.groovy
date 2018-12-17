package rd.professional.domain

import grails.rest.Resource
import io.swagger.annotations.ApiModelProperty

import java.time.LocalDateTime

@Resource(readOnly = false, formats = ['json'])
class ContactInformation {

    @ApiModelProperty(hidden = true)
    Long id
    String address // TODO: Change to a native JSON column once GORM 7.0 is released
    LocalDateTime dateCreated
    LocalDateTime lastUpdated
    UUID contactId = UUID.randomUUID()

    @ApiModelProperty(hidden = true)
    Organisation organisation
    @ApiModelProperty(hidden = true)
    ProfessionalUser user

    static belongsTo = [
            organisation: Organisation,
            user        : ProfessionalUser
    ]
    static constraints = {
        address nullable: false
        contactId nullable: false, unique: true
        organisation nullable: true
        user nullable: true
    }

    static mapping = {
        contactId type: 'uuid-binary', length: 16
    }
}
