package rd.professional.web

import grails.validation.Validateable
import groovy.transform.ToString
import rd.professional.domain.Status

@ToString(includeNames = true, includeFields = true)
class OrganisationUpdateCommand implements Validateable {
    String name
    String url
    String sraId

    String firstName
    String lastName
    String email

    String pbaAccounts
    String domains

    String address
    Status status

    static constraints = {
        sraId nullable: true
        url nullable: true
        name nullable: false

        firstName nullable: false
        lastName nullable: false
        email nullable: false

        pbaAccounts nullable: true
        domains nullable: true

        address nullable: true
        status nullable: true
    }
}