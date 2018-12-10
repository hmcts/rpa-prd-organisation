package rd.professional.domain


import grails.rest.Resource

@Resource(readOnly = false, formats = ['json'])
class Address {

    String houseNoBuildingName
    String addressLine1
    String addressLine2
    String townCity
    String county
    String country
    String postcode

    static belongsTo = [organisation: Organisation]

    static constraints = {
        houseNoBuildingName nullable: true
        addressLine1 nullable: true
        addressLine2 nullable: true
        townCity nullable: true
        county nullable: true
        country nullable: true
        postcode nullable: true
    }

}
