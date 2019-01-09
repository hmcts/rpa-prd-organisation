package rd.professional.web

import grails.gorm.transactions.Transactional
import io.swagger.annotations.*
import rd.professional.domain.Domain
import rd.professional.service.OrganisationService
import rd.professional.web.command.AddDomainCommand

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NO_CONTENT

@Api(
        value = "organisations/",
        description = "Domain APIs"
)
class DomainController extends AbstractExceptionHandlerController<Domain> {
    static responseFormats = ['json']

    DomainController() {
        super(Domain)
    }
    OrganisationService organisationService

    @ApiOperation(
            value = "List all domains belonging to an organisation",
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
        super.index(max)
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
    @Transactional
    def delete() {
        def organisationId = params.organisationId
        def domain = params.id
        def instance = Domain.where {
            organisation.organisationId == organisationId
            host == domain
        }.find()
        if (instance == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        deleteResource instance

        render status: NO_CONTENT
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
                    dataType = "rd.professional.web.command.AddDomainCommand"
            )
    ])
    @Transactional
    def save() {
        def organisation = organisationService.getForUuid(params.organisationId)
        if (!organisation) {
            notFound()
            return
        }

        def cmd = new AddDomainCommand(request.getJSON())
        def domain = new Domain(host: cmd.domain)

        organisation.addToDomains(domain)

        domain.validate()
        if (domain.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond domain.errors, status: 400
            return
        }

        saveResource organisation

        respond domain, [status: CREATED]
    }

    protected Domain queryForResource(Serializable id) {
        Domain.where {
            domainId == id
        }.find()
    }

    protected List<Domain> listAllResources(Map params) {
        Domain.where {
            organisation.organisationId == params.organisationId
        }.findAll()
    }
}
