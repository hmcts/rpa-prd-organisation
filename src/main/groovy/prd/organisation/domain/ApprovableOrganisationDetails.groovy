package prd.organisation.domain


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

    ApprovableOrganisationDetails(Long organisationId, String organisationName, URL organisationUrl, String organisationSRAId, Status organisationStatus, String initialSuperuserFirstName, String initialSuperuserLastName, String initialSuperuserEmail, String houseNoBuildingName, String addressLine1, String addressLine2, String townCity, String county, String country, String postcode) {
        this.organisationId = organisationId
        this.organisationName = organisationName
        this.organisationUrl = organisationUrl
        this.organisationSRAId = organisationSRAId
        this.organisationStatus = organisationStatus
        this.initialSuperuserFirstName = initialSuperuserFirstName
        this.initialSuperuserLastName = initialSuperuserLastName
        this.initialSuperuserEmail = initialSuperuserEmail
        this.houseNoBuildingName = houseNoBuildingName
        this.addressLine1 = addressLine1
        this.addressLine2 = addressLine2
        this.townCity = townCity
        this.county = county
        this.country = country
        this.postcode = postcode
    }

}
