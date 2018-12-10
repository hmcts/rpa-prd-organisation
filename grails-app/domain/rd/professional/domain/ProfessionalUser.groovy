package rd.professional.domain


import grails.rest.Resource
import rd.professional.web.SubclassRestfulController

@Resource(readOnly = false, formats = ['json'], superClass = SubclassRestfulController)
class ProfessionalUser {

    String emailId
    String firstName
    String lastName
    Status status = Status.PENDING

    static belongsTo = [organisation: Organisation]

    static constraints = {
        emailId nullable: false, unique: true
        firstName nullable: false
        lastName nullable: false
    }
}
