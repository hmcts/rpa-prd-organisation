package rd.professional.web

import grails.validation.Validateable
import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true)
class UserRegistrationCommand implements Validateable {
    String firstName
    String lastName
    String email

    String pbaAccounts

    String address

    static constraints = {
        firstName nullable: false
        lastName nullable: false
        email nullable: false

        pbaAccounts nullable: true

        address nullable: true
    }
}