package rd.professional.domain

import grails.converters.JSON
import grails.rest.Resource
import rd.professional.web.SubclassRestfulController

import java.time.LocalDateTime

@Resource(readOnly = false, formats = ['json'], superClass = SubclassRestfulController)
class Organisation {

    String name
    URL url
    String sraId
    UUID organisationId = UUID.randomUUID()

    boolean sraRegulated
    String companyNumber

    LocalDateTime lastUpdated
    Status status = Status.PENDING

    static {
        JSON.registerObjectMarshaller(Organisation, {
            return [
                    name:it.name,
                    url:it.url,
                    sraId:it.sraId,
                    organisationId:it.organisationId,
                    lastUpdated:it.lastUpdated,
                    companyNumber:it.companyNumber,
                    status:status,
                    contacts:it.contacts,
                    accounts:it.accounts,
                    domains:it.domains,
                    contacts:it.contacts
            ]
        })
    }

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