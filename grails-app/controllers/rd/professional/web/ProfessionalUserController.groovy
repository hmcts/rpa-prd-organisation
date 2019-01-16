package rd.professional.web

import grails.gorm.transactions.Transactional
import io.swagger.annotations.*
import rd.professional.domain.ContactInformation
import rd.professional.domain.PaymentAccount
import rd.professional.domain.ProfessionalUser
import rd.professional.service.OrganisationService
import rd.professional.web.command.UserRegistrationCommand
import rd.professional.web.dto.ProfessionalUserDto

import static org.springframework.http.HttpStatus.CREATED

@Api(
        value = "organisations/",
        description = "Professional User APIs"
)
class ProfessionalUserController extends AbstractDtoRenderingController<ProfessionalUser, ProfessionalUserDto> {
    static responseFormats = ['json']

    ProfessionalUserController() {
        super(ProfessionalUser, ProfessionalUserDto)
    }

    OrganisationService organisationService

    @ApiOperation(
            value = "List all users for an organisation",
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
        super.index(max)
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
        super.show()
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
    @Transactional
    def delete() {
        super.delete()
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
                    dataType = "rd.professional.web.command.UserRegistrationCommand"
            )
    ])
    @Transactional
    def save() {
        def organisation = organisationService.getForUuid(params.organisationId)
        if (!organisation) {
            notFound()
            return
        }

        def cmd = new UserRegistrationCommand(request.getJSON())
        def user = new ProfessionalUser(
                firstName: cmd.firstName,
                lastName: cmd.lastName,
                emailId: cmd.email
        )
        if (cmd.pbaAccounts) {
            cmd.pbaAccounts.each { account -> user.addToAccounts(new PaymentAccount(pbaNumber: account.pbaNumber)) }
        }
        if (cmd.address) {
            user.addToContacts(new ContactInformation(
                    address: cmd.address.address
            ))
        }

        organisation.addToUsers(user)

        user.validate()
        if (user.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond user.errors, status: 400
            return
        }

        saveResource organisation

        respond new ProfessionalUserDto(user), [status: CREATED]
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
                    dataType = "rd.professional.web.command.UserUpdateCommand"
            )
    ])
    @Transactional
    def update() {
        super.update()
    }

    protected ProfessionalUser queryForResource(Serializable id) {
        ProfessionalUser.where {
            userId == id
        }.find()
    }

    protected List<ProfessionalUser> listAllResources(Map params) {
        ProfessionalUser.where {
            organisation.organisationId == params.organisationId
        }.findAll()
    }
}
