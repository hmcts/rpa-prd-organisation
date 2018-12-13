package rd.professional.domain

import grails.converters.JSON
import grails.rest.Resource
import rd.professional.web.SubclassRestfulController

@Resource(readOnly = false, formats = ['json'], superClass = SubclassRestfulController)
class ProfessionalUser {

    UUID userId = UUID.randomUUID()
    String emailId
    String firstName
    String lastName
    Status status = Status.PENDING

    static {
        JSON.registerObjectMarshaller(ProfessionalUser, {
            return [
                    userId:it.userId,
                    emailId:it.emailId,
                    firstName:it.firstName,
                    lastName:it.lastName,
                    status:it.status
            ]
        })
    }

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
