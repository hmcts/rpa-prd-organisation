package prd.organisation.domain


import grails.rest.Resource
import prd.organisation.web.SubclassRestfulController

@Resource(readOnly = false, formats = ['json'], superClass = SubclassRestfulController)
class Organisation {
    String organisationId = UUID.randomUUID().toString()
    String name
    URL url
    String sraId

    boolean sraRegulated
    String companyNumber

    Date lastUpdated
    Status status = Status.PENDING

    static hasMany = [
            users    : ProfessionalUser,
            accounts : PaymentAccount,
            domains  : Domain,
            addresses: Address
    ]

    static constraints = {
        name nullable: false, unique: true
        sraId nullable: true
        url nullable: true
        companyNumber nullable: true
    }
}