package rd.professional.web

import grails.rest.RestfulController
import io.swagger.annotations.*
import rd.professional.domain.Domain

@Api(
        value = "organisations/",
        description = "Domain APIs"
)
class DomainsController extends RestfulController<Domain> {
    static responseFormats = ['json']

    DomainsController() {
        super(Domain)
    }
    //DomainService domainService

    @ApiOperation(
            value="List all domains belonging to an organisation",
            nickname = "/{orgId}/domains",
            produces = "application/json",
            httpMethod = "GET",
            response = String,
            responseContainer = "Set"
    )
    @ApiResponses([
            @ApiResponse(code = 404, message = "No domains found"),
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
            value = "Delete an domain",
            nickname = "/{orgId}/domains/{domain}",
            httpMethod = 'DELETE'
    )
    @ApiResponses([
            @ApiResponse(code = 404, message = "Domain not found"),
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
                    name = "domain",
                    paramType = "path",
                    required = true,
                    value = "Domain name",
                    dataType = "string"
            )
    ])
    def delete() {
        // TODO
    }

    @ApiOperation(
            value = "Add a new domain",
            nickname = "/{orgId}/domains",
            produces = "application/json",
            consumes = "application/json",
            httpMethod = 'POST'
    )
    @ApiResponses([
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 405, message = "Method not allowed"),
            @ApiResponse(code = 409, message = "Domain already exists")
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
                    value = "New domain details",
                    dataType = "rd.professional.web.AddDomainCommand"
            )
    ])
    def register(AddDomainCommand cmd) {
        // TODO
    }
}
