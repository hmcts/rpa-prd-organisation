package rd.professional.web

import groovy.util.logging.Log
import rd.professional.domain.Address
import rd.professional.domain.ApprovableOrganisationDetails
import rd.professional.domain.Organisation
import rd.professional.domain.ProfessionalUser
import rd.professional.domain.Status

class SearchController {
    static responseFormats = ['json']

    def approvedOrganisations() {
        respond approvableOrganisationsInStatus(Status.APPROVED)
    }

    def pendingOrganisations() {
        respond approvableOrganisationsInStatus(Status.PENDING)
    }

    def approvableOrganisationsInStatus(Status status) {
        /*
         * REFACTOR: Move search logic to a Service?
         */
        Organisation.where {
            status == status
        }.list().collect { Organisation org ->
            ProfessionalUser initialSuperUser = org.users[0]
            Address address = org.addresses ? org.addresses[0] : null
            new ApprovableOrganisationDetails(
                    org.id, org.name, org.url, org.sraId, org.status,
                    initialSuperUser.firstName, initialSuperUser.lastName, initialSuperUser.emailId,
                    address?.houseNoBuildingName, address?.addressLine1, address?.addressLine2, address?.townCity, address?.county, address?.country, address?.postcode
            )
        }
    }

    def accountsByEmail(String email) {
        /*
         * REFACTOR: Move search logic to a Service?
         */
        log.info("accountsByEmail: called with $email")
        def orgAccounts = Organisation.withCriteria {
            'users' {
                eq('emailId', email)
            }
        }.accounts
        log.debug("Found accounts: $orgAccounts")

        // TODO: What if a user belongs to multiple organisations?
        if (orgAccounts && orgAccounts.size() == 1) {
            def response = [payment_accounts: orgAccounts.get(0).pbaNumber]
            respond response
        } else {
            response.status = 404
            render '[]'
        }
    }

}