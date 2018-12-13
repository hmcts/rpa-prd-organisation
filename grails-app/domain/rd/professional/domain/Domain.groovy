package rd.professional.domain

import grails.converters.JSON
import grails.rest.Resource

@Resource(readOnly = false, formats = ['json'])
class Domain {

    UUID domainId = UUID.randomUUID()

    String host

    static {
        JSON.registerObjectMarshaller(Domain, {
            return [
                    domainId:it.domainId,
                    host:it.host,
            ]
        })
    }

    static belongsTo = [organisation: Organisation]

    static constraints = {
        host nullable: false
        domainId nullable: false, unique: true
    }

    static mapping = {
        domainId type: 'uuid-binary', length: 16
    }
}
