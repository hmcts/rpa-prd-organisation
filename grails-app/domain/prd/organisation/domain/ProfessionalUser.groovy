package prd.organisation.domain

import grails.rest.*
import prd.organisation.web.SubclassRestfulController

@Resource(readOnly = false, formats = ['json'], superClass=SubclassRestfulController)
class ProfessionalUser {

    String emailId
    String firstName
    String lastName
    //Status status = Status.APPROVED

    static belongsTo = [organisation: Organisation]

    static constraints = {
        emailId nullable: false, unique: true
        firstName nullable: false
        lastName nullable: false
    }
}
