package rd.professional.web

import grails.validation.Validateable
import groovy.transform.ToString
import rd.professional.domain.Status

@ToString(includeNames = true, includeFields = true)
class UserUpdateCommand implements Validateable {
    String firstName
    String lastName
    String email

    String pbaAccounts

    String address
    Status status

    static constraints = {
        firstName nullable: false
        lastName nullable: false
        email nullable: false

        pbaAccounts nullable: true

        address nullable: true
        status nullable: true
    }
}