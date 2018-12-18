package rd.professional.web

import grails.gorm.transactions.Transactional
import grails.rest.RestfulController
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.HttpStatus
import rd.professional.domain.Organisation
import rd.professional.service.OrganisationService

@Api(
        value = "organisations/",
        description = "Organisation related APIs"
)
class OrganisationController extends RestfulController<Organisation> {
    static responseFormats = ['json']

    OrganisationController() {
        super(Organisation)
    }

    OrganisationService organisationService

    @ApiOperation(
            value="List all organisations",
            nickname = "/",
            produces = "application/json",
            httpMethod = "GET",
            response = Organisation,
            responseContainer = "Set"
    )
    @ApiResponses([
            @ApiResponse(code = 404, message = "No organisations found"),
            @ApiResponse(code = 405, message = "Method not allowed")
    ])
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Organisation.list(params), model: ["OrganisationCount": Organisation.count()]
    }

    @ApiOperation(
            value = "Get the details of an organisation",
            nickname = "/{id}",
            produces = "application/json",
            httpMethod = 'GET',
            response = Organisation
    )
    @ApiResponses([
            @ApiResponse(code = 404, message = "Organisation not found"),
            @ApiResponse(code = 405, message = "Method not allowed")
    ])
    @ApiImplicitParams([
            @ApiImplicitParam(
                    name = "id",
                    paramType = "path",
                    required = true,
                    value = "Organisation ID",
                    dataType = "string"
            )
    ])
    Organisation show() {
        respond organisationService.getForUuid(params.id)
    }

    @ApiOperation(
            value = "Delete an organisation",
            nickname = "/{id}",
            produces = "application/json",
            httpMethod = 'DELETE'
    )
    @ApiResponses([
            @ApiResponse(code = 404, message = "Organisation not found"),
            @ApiResponse(code = 405, message = "Method not allowed")
    ])
    @ApiImplicitParams([
            @ApiImplicitParam(
                    name = "id",
                    paramType = "path",
                    required = true,
                    value = "Organisation ID",
                    dataType = "string"
            )
    ])
    @Transactional
    def delete() {
        def instance = organisationService.getForUuid(params.id)
        if (instance) {
            instance.delete(flush: true)
            render status: HttpStatus.NO_CONTENT
        } else {
            notFound()
        }
    }

    @ApiOperation(
            value = "Register an organisation",
            nickname = "/",
            produces = "application/json",
            consumes = "application/json",
            httpMethod = 'POST'
    )
    @ApiResponses([
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 405, message = "Method not allowed"),
            @ApiResponse(code = 409, message = "Organisation already exists")
    ])
    @ApiImplicitParams([
            @ApiImplicitParam(
                    name = "body",
                    paramType = "body",
                    required = true,
                    value = "Organisation registration details",
                    dataType = "rd.professional.web.OrganisationRegistrationCommand"
            )
    ])
    @Transactional
    def save(OrganisationRegistrationCommand cmd) {
        log.info "Creating organisation"

        try {
            respond (organisationService.registerOrganisation(cmd), status: 201)
        } catch (Exception e) {
            response.status = 400
            response.outputStream << e.getMessage().bytes
        }
    }

    @ApiOperation(
            value = "Update an organisation",
            nickname = "/{id}",
            produces = "application/json",
            consumes = "application/json",
            httpMethod = 'PUT'
    )
    @ApiResponses([
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 404, message = "Organisation not found"),
            @ApiResponse(code = 405, message = "Method not allowed")
    ])
    @ApiImplicitParams([
            @ApiImplicitParam(
                    name = "id",
                    paramType = "path",
                    required = true,
                    value = "Organisation ID",
                    dataType = "string"
            ),
            @ApiImplicitParam(
                    name = "body",
                    paramType = "body",
                    required = true,
                    value = "Organisation update details",
                    dataType = "rd.professional.web.OrganisationUpdateCommand"
            )
    ])
    @Transactional
    def update(OrganisationUpdateCommand cmd) {
        try {
            respond(organisationService.updateOrganisation(params.id, cmd), status: 200)
        } catch (Exception e) {
            response.status = 400
            response.outputStream << e.getMessage().bytes
        }
    }
}
