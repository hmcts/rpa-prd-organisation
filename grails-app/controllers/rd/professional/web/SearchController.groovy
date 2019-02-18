package rd.professional.web

import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import rd.professional.domain.ApprovableOrganisationDetails
import rd.professional.domain.Organisation
import rd.professional.domain.ProfessionalUser
import rd.professional.domain.Status
import rd.professional.service.AccountsService
import rd.professional.web.dto.OrganisationDto
import rd.professional.web.dto.ProfessionalUserDto

@Api(
        value = "search/",
        description = "Search related APIs"
)
class SearchController {
    static responseFormats = ['json']

    AccountsService accountsService

    @ApiOperation(
            value = "Search for approved organisations",
            nickname = "organisations/approved",
            produces = "application/json",
            httpMethod = "GET",
            response = Organisation,
            responseContainer = "Set"
    )
    @ApiResponses([
            @ApiResponse(code = 404, message = "No approved organisations found"),
            @ApiResponse(code = 405, message = "Only GET method is allowed")
    ])
    def approvedOrganisations() {
        respond approvableOrganisationsInStatus(Status.APPROVED)
    }

    @ApiOperation(
            value = "Search for pending organisations",
            nickname = "organisations/pending",
            produces = "application/json",
            httpMethod = "GET",
            response = Organisation,
            responseContainer = "Set"
    )
    @ApiResponses([
            @ApiResponse(code = 404, message = "No pending organisations found"),
            @ApiResponse(code = 405, message = "Only GET method is allowed")
    ])
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
                    org.organisationId, org.name, org.url, org.sraId, org.status,
                    initialSuperUser.firstName, initialSuperUser.lastName, initialSuperUser.emailId,
                    address, org.accounts.pbaNumber.join(", ")
            )
        }
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

        if (orgAccounts && orgAccounts.size() >= 1) {
            def response = [payment_accounts: orgAccounts.get(0).pbaNumber]
            respond response
        } else {
            response.status = 404
            render '[]'
        }
    }

    @ApiOperation(
            value = "Search for user belonging to an email address",
            nickname = "users/{email}",
            produces = "application/json",
            httpMethod = "GET",
            response = ProfessionalUserDto
    )
    @ApiResponses([
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 405, message = "Only GET method is allowed")
    ])
    @ApiImplicitParams([
            @ApiImplicitParam(name = "email",
                    paramType = "path",
                    required = true,
                    value = "Email address of the user you wish to search for",
                    dataType = "string")
    ])
    def userByEmail(String email) {
        log.info("userByEmail: called with $email")
        def user = ProfessionalUser.findByEmailId(email)
        log.debug("Found user: $user")

        if (user) {
            respond new ProfessionalUserDto(user)
        } else {
            response.status = 404
            render '[]'
        }
    }

    @ApiOperation(
            value = "Search for organisation by user's email address",
            nickname = "organisations/{email}",
            produces = "application/json",
            httpMethod = "GET",
            response = OrganisationDto
    )
    @ApiResponses([
            @ApiResponse(code = 404, message = "Organisation not found"),
            @ApiResponse(code = 405, message = "Only GET method is allowed")
    ])
    @ApiImplicitParams([
            @ApiImplicitParam(name = "email",
                    paramType = "path",
                    required = true,
                    value = "Email address of the user whose organisation you wish to search for",
                    dataType = "string")
    ])
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
}