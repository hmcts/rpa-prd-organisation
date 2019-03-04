package rd.professional.web

import io.swagger.annotations.*
import rd.professional.domain.Organisation
import rd.professional.domain.ProfessionalUser
import rd.professional.domain.Status
import rd.professional.service.AccountsService
import rd.professional.service.OrganisationService
import rd.professional.service.UsersService
import rd.professional.web.dto.OrganisationDto
import rd.professional.web.dto.ProfessionalUserDto

@Api(
        value = "search/",
        description = "Search related APIs"
)
class SearchController {
    static responseFormats = ['json']

    AccountsService accountsService
    OrganisationService organisationService
    UsersService usersService

    def approvedOrganisations() {
        respond organisationService.approvableOrganisationsInStatus(Status.APPROVED)
    }

    def pendingOrganisations() {
        respond organisationService.approvableOrganisationsInStatus(Status.PENDING)
    }

    @ApiOperation(
            value = "Search for payment accounts belonging to an email address",
            nickname = "pba/{email}",
            produces = "application/json",
            httpMethod = "GET",
            response = String,
            responseContainer = "Set"
    )
    @ApiResponses([
            @ApiResponse(code = 404, message = "No accounts found"),
            @ApiResponse(code = 405, message = "Only GET method is allowed")
    ])
    @ApiImplicitParams([
            @ApiImplicitParam(name = "email",
                    paramType = "path",
                    required = true,
                    value = "Email address of the user whose accounts you wish to search for",
                    dataType = "string")
    ])
    def accountsByEmail(String email) {
        /*
         * REFACTOR: Move search logic to a Service?
         */
        log.info("accountsByEmail: called with $email")
        def orgAccounts = accountsService.findOrgAccountsByEmail(email)
        log.debug("Found accounts: $orgAccounts")

        if (orgAccounts) {
            def response = [payment_accounts: orgAccounts.pbaNumber]
            respond response
        } else {
            response.status = 404
            render '[]'
        }
    }

    def userByEmail(String email) {
        log.info("userByEmail: called with $email")
        def user = usersService.findUserAccountsByEmail(email)
        log.debug("Found user: $user")

        if (user) {
            respond new ProfessionalUserDto(user)
        } else {
            response.status = 404
            render '[]'
        }
    }

    def organisationByEmail(String email) {
        log.info("organisationByEmail: called with $email")
        def user = ProfessionalUser.findByEmailId(email)

        if (user) {
            log.debug("Found org: $user.organisation")
            respond new OrganisationDto(user.organisation)
        } else {
            response.status = 404
            render '[]'
        }
    }

    def organisationIdByName(String name) {
        def org = Organisation.findByName(name)
        if (org)
            org.organisationId
    }
}