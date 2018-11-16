package prd.organisation.web

import groovy.transform.ToString
import grails.validation.Validateable

@ToString(includeNames=true, includeFields=true)
class OrganisationRegistrationCommand implements Validateable {
    String name
    String url
    String sraId
    
    String firstName
    String lastName
    String email

    String pbaAccounts
    String domains

    String houseNoBuildingName
    String addressLine1
    String addressLine2
    String townCity
    String county
    String country
    String postcode

    static constraints = {
        sraId nullable: true
        url nullable: true
        name nullable: false

        firstName nullable: false
        lastName nullable: false
        email nullable: false

        pbaAccounts nullable: true
        domains nullable: true

        houseNoBuildingName nullable: true
        addressLine1 nullable: true
        addressLine2 nullable: true
        townCity nullable: true
        county nullable: true
        country nullable: true
        postcode nullable: true
    }
}