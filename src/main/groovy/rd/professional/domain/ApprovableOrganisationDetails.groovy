package rd.professional.domain

import groovy.transform.Canonical

@Canonical
class ApprovableOrganisationDetails {

    UUID id
    String organisationName
    URL organisationUrl
    String organisationSRAId
    Status organisationStatus
    String initialSuperuserFirstName
    String initialSuperuserLastName
    String initialSuperuserEmail
    String address
    String pbas

}
