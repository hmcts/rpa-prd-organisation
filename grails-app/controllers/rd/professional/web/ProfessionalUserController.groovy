package rd.professional.web

import grails.rest.RestfulController
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import rd.professional.domain.ProfessionalUser

@Api(
        value = "organisations/",
        description = "Professional User APIs"
)
class ProfessionalUserController extends RestfulController<ProfessionalUser> {
    static responseFormats = ['json']

    ProfessionalUserController() {
        super(ProfessionalUser)
    }
//ProfessionalUserService userService

    @ApiOperation(
            value="List all users for an organisation",
            nickname = "/{orgId}/users",
            produces = "application/json",
            httpMethod = "GET",
            response = ProfessionalUser,
            responseContainer = "Set"
    )
    @ApiResponses([
            @ApiResponse(code = 404, message = "No users found"),
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
    def index(Integer max) {
        // TODO
    }

    @ApiOperation(
            value = "Get the details of a user",
            nickname = "/{orgId}/users/{id}",
            produces = "application/json",
            httpMethod = 'GET',
            response = ProfessionalUser
    )
    @ApiResponses([
            @ApiResponse(code = 404, message = "Organisation not found"),
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
                    name = "id",
                    paramType = "path",
                    required = true,
                    value = "User ID",
                    dataType = "string"
            )
    ])
    def show() {
        // TODO
    }

    @ApiOperation(
            value = "Delete a user",
            nickname = "/{orgId}/users/{id}",
            httpMethod = 'DELETE'
    )
    @ApiResponses([
            @ApiResponse(code = 404, message = "User not found"),
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
                    name = "id",
                    paramType = "path",
                    required = true,
                    value = "User ID",
                    dataType = "string"
            )
    ])
    def delete() {
        // TODO
    }

    @ApiOperation(
            value = "Register a new user",
            nickname = "/{orgId}/users",
            produces = "application/json",
            consumes = "application/json",
            httpMethod = 'POST'
    )
    @ApiResponses([
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 405, message = "Method not allowed"),
            @ApiResponse(code = 409, message = "User already exists")
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
                    value = "User registration details",
                    dataType = "rd.professional.web.UserRegistrationCommand"
            )
    ])
    def register(UserRegistrationCommand cmd) {
        // TODO
    }

    @ApiOperation(
            value = "Update a user",
            nickname = "/{orgId}/users/{id}",
            produces = "application/json",
            consumes = "application/json",
            httpMethod = 'PUT'
    )
    @ApiResponses([
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 404, message = "User not found"),
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
                    name = "id",
                    paramType = "path",
                    required = true,
                    value = "User ID",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = "body",
                    paramType = "body",
                    required = true,
                    value = "User update details",
                    dataType = "rd.professional.web.UserUpdateCommand"
            )
    ])
    def updateUser(UserUpdateCommand cmd) {
        // TODO
    }
}
