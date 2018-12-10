package prd.organisation.web

import prd.organisation.domain.*

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
        def orgAccounts = Organisation.withCriteria {
            'users' {
                eq('emailId', email)
            }
        }.accounts

        // TODO: What if a user belongs to multiple organisations?
        if (orgAccounts && orgAccounts.size() == 1) {
            respond orgAccounts.get(0).pbaNumber
        } else {
            response.status = 404
            render '[]'
        }
    }

}