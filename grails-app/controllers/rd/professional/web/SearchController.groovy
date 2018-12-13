package rd.professional.web

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import rd.professional.domain.ApprovableOrganisationDetails
import rd.professional.domain.Organisation
import rd.professional.domain.ProfessionalUser
import rd.professional.domain.Status

@Api(value = "organisations")
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
            String address = org.contacts ? org.contacts[0] : null
            new ApprovableOrganisationDetails(
                    org.id, org.name, org.url, org.sraId, org.status,
                    initialSuperUser.firstName, initialSuperUser.lastName, initialSuperUser.emailId,
                    address
            )
        }
    }

    @ApiOperation(
            value = "Search for payment accounts belonging to an email address",
            nickname = "pba/{email}",
            produces = "application/json",
            httpMethod = "GET"
    )
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