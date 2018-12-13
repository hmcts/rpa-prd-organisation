package rd.professional.domain

import grails.rest.Resource

import java.time.LocalDateTime

@Resource(readOnly = false, formats = ['json'])
class ContactInformation {

    String address // TODO: Change to a native JSON column once GORM 7.0 is released
    LocalDateTime dateCreated
    LocalDateTime lastUpdated
    UUID contactId = UUID.randomUUID()

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
