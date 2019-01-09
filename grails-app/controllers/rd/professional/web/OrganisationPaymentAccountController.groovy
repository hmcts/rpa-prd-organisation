package rd.professional.web

import grails.gorm.transactions.Transactional
import io.swagger.annotations.*
import rd.professional.domain.PaymentAccount
import rd.professional.service.OrganisationService
import rd.professional.web.command.AddAccountCommand

import static org.springframework.http.HttpStatus.CREATED

@Api(
        value = "organisations/",
        description = "Payment Account APIs"
)
class OrganisationPaymentAccountController extends AbstractExceptionHandlerController<PaymentAccount> {
    static responseFormats = ['json']

    OrganisationService organisationService

    OrganisationPaymentAccountController() {
        super(PaymentAccount)
    }

    @ApiOperation(
            value = "List all accounts belonging to an organisation",
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
    def index(Integer max) {
        super.index(max)
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
    @Transactional
    def delete() {
        super.delete()
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
                    dataType = "rd.professional.web.command.AddAccountCommand"
            )
    ])
    @Transactional
    def save() {
        def organisation = organisationService.getForUuid(params.organisationId)
        if (!organisation) {
            notFound()
            return
        }

        def cmd = new AddAccountCommand(request.getJSON())
        def account = new PaymentAccount(pbaNumber: cmd.pbaNumber)

        organisation.addToAccounts(account)

        account.validate()
        if (account.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond account.errors, status: 400
            return
        }

        saveResource organisation

        respond account, [status: CREATED]
    }


    protected PaymentAccount queryForResource(Serializable id) {
        PaymentAccount.where {
            pbaNumber == id
        }.find()
    }

    protected List<PaymentAccount> listAllResources(Map params) {
        PaymentAccount.where {
            organisation.organisationId == params.organisationId
        }.findAll()
    }
}
