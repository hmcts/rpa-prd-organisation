package rd.professional.domain

import groovy.transform.Canonical

@Canonical
class ApprovableOrganisationDetails {

    Long organisationId
    String organisationName
    URL organisationUrl
    String organisationSRAId
    Status organisationStatus
    String initialSuperuserFirstName
    String initialSuperuserLastName
    String initialSuperuserEmail
    String houseNoBuildingName
    String addressLine1
    String addressLine2
    String townCity
    String county
    String country
    String postcode

}
