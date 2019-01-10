package rd.professional.web

import grails.converters.JSON
import grails.gorm.transactions.Transactional
import io.swagger.annotations.*
import rd.professional.domain.Organisation
import rd.professional.service.OrganisationService
import rd.professional.web.command.OrganisationRegistrationCommand
import rd.professional.web.dto.OrganisationDto

import static org.springframework.http.HttpStatus.OK

@Api(
        value = "organisations/",
        description = "Organisation related APIs"
)
class OrganisationController extends AbstractDtoRenderingController<Organisation, OrganisationDto> {
    static responseFormats = ['json']

    OrganisationController() {
        super(Organisation, OrganisationDto)
    }

    OrganisationService organisationService

    @ApiOperation(
            value = "List all organisations",
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
        super.index(max)
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
        super.show()
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
        super.delete()
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
                    dataType = "rd.professional.web.command.OrganisationRegistrationCommand"
            )
    ])
    @Transactional
    def save(OrganisationRegistrationCommand cmd) {
        log.info "Creating organisation"
        try {
            respond(new OrganisationDto(organisationService.registerOrganisation(cmd)), status: 201)
        } catch (Exception e) {
            render e.getMessage(), status: 400
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
                    dataType = "rd.professional.web.command.OrganisationUpdateCommand"
            )
    ])
    @Transactional
    def update() {
        super.update()
    }

    protected Organisation queryForResource(Serializable id) {
        organisationService.getForUuid(id)
    }
}
