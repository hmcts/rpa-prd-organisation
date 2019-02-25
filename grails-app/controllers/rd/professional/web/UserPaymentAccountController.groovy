package rd.professional.web

import grails.gorm.transactions.Transactional
import io.swagger.annotations.*
import rd.professional.domain.PaymentAccount
import rd.professional.service.UsersService
import rd.professional.web.command.AddAccountCommand
import rd.professional.web.dto.PaymentAccountDto

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT

@Api(
        value = "organisations/",
        description = "Payment Account APIs"
)
class UserPaymentAccountController extends AbstractDtoRenderingController<PaymentAccount, PaymentAccountDto> {
    static responseFormats = ['json']

    UsersService usersService

    UserPaymentAccountController() {
        super(PaymentAccount, PaymentAccountDto)
    }

    @ApiOperation(
            value = "List all accounts belonging to a user",
            nickname = "/{orgId}/users/{userId}/pbas",
            produces = "application/json",
            httpMethod = "GET",
            response = PaymentAccountDto,
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
    def index(Integer max) {
        super.index(max)
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
    @Transactional
    def delete() {
        def user = usersService.getForUuid(params.professionalUserId)
        if (user == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        def account = queryForResource(params.id)
        if (account == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        user.removeFromAccounts(account)
        user.save()
        account.save()

        render status: NO_CONTENT
    }

    @ApiOperation(
            value = "Set a different account for a user",
            nickname = "/{orgId}/users/{userId}/pbas",
            produces = "application/json",
            consumes = "application/json",
            httpMethod = 'POST',
            response = PaymentAccountDto
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
                    dataType = "rd.professional.web.command.AddAccountCommand"
            )
    ])
    @Transactional
    def save() {
        def user = usersService.getForUuid(params.professionalUserId)
        if (!user) {
            notFound()
            return
        }
        def cmd = new AddAccountCommand(request.getJSON())

        usersService.setPbaAccount(user, cmd.pbaNumber)

        user.validate()
        if (user.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond user.errors, status: 400
            return
        }

        saveResource user

        respond user.accounts[0], [status: CREATED]
    }

    protected PaymentAccount queryForResource(Serializable id) {
        PaymentAccount.findByPbaNumber(id)
    }

    protected List<PaymentAccount> listAllResources(Map params) {
        PaymentAccount.where {
            user.userId == params.professionalUserId
        }.findAll()
    }
}
