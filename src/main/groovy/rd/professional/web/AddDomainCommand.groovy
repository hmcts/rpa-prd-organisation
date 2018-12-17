package rd.professional.web

import grails.validation.Validateable
import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true)
class AddDomainCommand implements Validateable {

    String domain

    static constraints = {
        domain nullable: false
    }
}
