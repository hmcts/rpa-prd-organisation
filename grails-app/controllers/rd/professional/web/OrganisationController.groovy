package rd.professional.web


import grails.gorm.transactions.Transactional
import io.swagger.annotations.*
import rd.professional.domain.ContactInformation
import rd.professional.domain.Domain
import rd.professional.domain.DxAddress
import rd.professional.domain.Organisation
import rd.professional.domain.PaymentAccount
import rd.professional.domain.Status
import rd.professional.service.OrganisationService
import rd.professional.web.command.AddAccountCommand
import rd.professional.web.command.AddDomainCommand
import rd.professional.web.command.OrganisationRegistrationCommand
import rd.professional.web.command.OrganisationUpdateCommand
import rd.professional.web.dto.OrganisationDto

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY

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
            response = OrganisationDto
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
            httpMethod = 'POST',
            response = OrganisationDto
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
    def save() {
        log.info "Creating organisation"
        def cmd = new OrganisationRegistrationCommand(request.getJSON())
        Organisation organisation = organisationService.registerOrganisation(cmd)
        respond new OrganisationDto(organisation), [status: CREATED]
    }

    @ApiOperation(
            value = "Update an organisation",
            nickname = "/{id}",
            produces = "application/json",
            consumes = "application/json",
            httpMethod = 'PUT',
            response = OrganisationDto
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
        def cmd = new OrganisationUpdateCommand(request.getJSON())
        Organisation organisation = queryForResource(params.id)
        if (organisation == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (cmd.address) {
            organisation.addToContacts(new ContactInformation(cmd.address))
        }
        if (cmd.dxAddress) {
            if (!DxAddress.find {
                dxNumber == cmd.dxAddress.dxNumber && dxExchange == cmd.dxAddress.dxExchange
            })
                organisation.dxAddress = new DxAddress(cmd.dxAddress)
        }
        if (cmd.domains) {
            for (AddDomainCommand domainCommand : cmd.domains) {
                if (!Domain.findByHost(domainCommand.domain))
                    organisation.addToDomains(new Domain(domainCommand))
            }
        }
        if (cmd.pbaAccounts) {
            for (AddAccountCommand accountCommand : cmd.pbaAccounts) {
                if (!PaymentAccount.findByPbaNumber(accountCommand.pbaNumber))
                    organisation.addToAccounts(new PaymentAccount(accountCommand))
            }
        }
        if (cmd.status)
            organisation.setStatus(Status.valueOf(cmd.status))
        if (cmd.sraId)
            organisation.setSraId(cmd.sraId)
        if (cmd.name)
            organisation.setName(cmd.name)

        organisation.validate()
        if (organisation.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond organisation.errors, [status: UNPROCESSABLE_ENTITY] // STATUS CODE 422
            return
        }

        organisation.save flush: true

        respond new OrganisationDto(organisation), [status: OK]
    }

    protected Organisation queryForResource(Serializable id) {
        organisationService.getForUuid(id)
    }
}
