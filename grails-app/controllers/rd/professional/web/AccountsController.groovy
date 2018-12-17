package rd.professional.web

import grails.rest.RestfulController
import io.swagger.annotations.*
import rd.professional.domain.PaymentAccount
import rd.professional.domain.ProfessionalUser

@Api(
        value = "organisations/",
        description = "Payment Account APIs"
)
class AccountsController extends RestfulController<PaymentAccount> {
    static responseFormats = ['json']

    AccountsController() {
        super(PaymentAccount)
    }
    //PaymentAccountService accountService

    @ApiOperation(
            value="List all accounts belonging to an organisation",
            nickname = "/{orgId}/pbas",
            produces = "application/json",
            httpMethod = "GET",
            response = String,
            responseContainer = "Set"
    )
    @ApiResponses([
            @ApiResponse(code = 404, message = "No accounts found"),
            @ApiResponse(code = 405, message = "Method not allowed")
    ])
    @ApiImplicitParams([
            @ApiImplicitParam(
                    name = "orgId",
                    paramType = "path",
                    required = true,
                    value = "Organisation ID",
                    dataType = "string"
            )
    ])
    def listOrgAccounts(Integer max) {
        // TODO
    }

    @ApiOperation(
            value = "Delete an account belonging to an organisation",
            nickname = "/{orgId}/pbas/{pbaNumber}",
            httpMethod = 'DELETE'
    )
    @ApiResponses([
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 405, message = "Method not allowed")
    ])
    @ApiImplicitParams([
            @ApiImplicitParam(
                    name = "orgId",
                    paramType = "path",
                    required = true,
                    value = "Organisation ID",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = "pbaNumber",
                    paramType = "path",
                    required = true,
                    value = "Account number",
                    dataType = "string"
            )
    ])
    def deleteOrgAccount() {
        // TODO
    }

    @ApiOperation(
            value = "Add a new account to an organisation",
            nickname = "/{orgId}/pbas",
            produces = "application/json",
            consumes = "application/json",
            httpMethod = 'POST'
    )
    @ApiResponses([
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 405, message = "Method not allowed"),
            @ApiResponse(code = 409, message = "Account already exists")
    ])
    @ApiImplicitParams([
            @ApiImplicitParam(
                    name = "orgId",
                    paramType = "path",
                    required = true,
                    value = "Organisation ID",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = "body",
                    paramType = "body",
                    required = true,
                    value = "New account details",
                    dataType = "AddAccountCommand"
            )
    ])
    def addOrgAccount(AddAccountCommand cmd) {
        // TODO
    }

    @ApiOperation(
            value="List all accounts belonging to a user",
            nickname = "/{orgId}/users/{userId}/pbas",
            produces = "application/json",
            httpMethod = "GET",
            response = String,
            responseContainer = "Set"
    )
    @ApiResponses([
            @ApiResponse(code = 404, message = "No accounts found"),
            @ApiResponse(code = 405, message = "Method not allowed")
    ])
    @ApiImplicitParams([
            @ApiImplicitParam(
                    name = "orgId",
                    paramType = "path",
                    required = true,
                    value = "Organisation ID",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = "userId",
                    paramType = "path",
                    required = true,
                    value = "User ID",
                    dataType = "string"
            )
    ])
    def listUserAccounts(Integer max) {
        // TODO
    }

    @ApiOperation(
            value = "Delete an account belonging to a user",
            nickname = "/{orgId}/users/{userId}/pbas/{pbaNumber}",
            httpMethod = 'DELETE'
    )
    @ApiResponses([
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 405, message = "Method not allowed")
    ])
    @ApiImplicitParams([
            @ApiImplicitParam(
                    name = "orgId",
                    paramType = "path",
                    required = true,
                    value = "Organisation ID",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = "userId",
                    paramType = "path",
                    required = true,
                    value = "User ID",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = "pbaNumber",
                    paramType = "path",
                    required = true,
                    value = "Account number",
                    dataType = "string"
            )
    ])
    def deleteUserAccount() {
        // TODO
    }

    @ApiOperation(
            value = "Add a new account to a user",
            nickname = "/{orgId}/users/{userId}/pbas",
            produces = "application/json",
            consumes = "application/json",
            httpMethod = 'POST'
    )
    @ApiResponses([
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 405, message = "Method not allowed"),
            @ApiResponse(code = 409, message = "Account already exists")
    ])
    @ApiImplicitParams([
            @ApiImplicitParam(
                    name = "orgId",
                    paramType = "path",
                    required = true,
                    value = "Organisation ID",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = "userId",
                    paramType = "path",
                    required = true,
                    value = "User ID",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = "body",
                    paramType = "body",
                    required = true,
                    value = "New account details",
                    dataType = "AddAccountCommand"
            )
    ])
    def addUserAccount(AddAccountCommand cmd) {
        // TODO
    }
}
