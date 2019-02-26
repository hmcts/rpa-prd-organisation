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
        respond organisationService.approvableOrganisationsInStatus(Status.APPROVED)
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
        def user = usersService.findUserAccountsByEmail(email)
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