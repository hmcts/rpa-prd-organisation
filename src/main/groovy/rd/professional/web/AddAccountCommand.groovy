package rd.professional.web

import grails.validation.Validateable
import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true)
class AddAccountCommand implements Validateable {

    String pbaNumber

    static constraints = {
        pbaNumber nullable: false
    }
}
