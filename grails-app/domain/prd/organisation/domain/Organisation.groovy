package prd.organisation.domain

import grails.rest.*
import prd.organisation.web.SubclassRestfulController

@Resource(readOnly = false, formats = ['json'], superClass=SubclassRestfulController)
class Organisation {
    String organisationId = UUID.randomUUID().toString()    
    String orgName
    boolean sraRegulated
    String sraId
    Date lastUpdated    
    String primaryContactFirstName
    String primaryContactLastName
    String primaryContactEmail    
    Status status = Status.PENDING
    String companyNumber

//    static mapping = {        
//      id column: 'ORGANISATION_ID'
//}

    static hasMany = [ users:ProfessionalUser ]

    static constraints = {        
        orgName nullable: false, unique: true
        sraId nullable: true
        primaryContactFirstName nullable: false
        primaryContactLastName nullable: false
        primaryContactEmail nullable: false
        companyNumber nullable: true
    }
}