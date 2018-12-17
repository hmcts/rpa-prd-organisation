package rd.professional.web

import grails.validation.Validateable
import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true)
class ContactInformationCommand implements Validateable {

    String address

    static constraints = {
        address nullable: false
    }
}
