package rd.professional.web

import grails.gorm.transactions.Transactional
import rd.professional.domain.PaymentAccount
import rd.professional.service.OrganisationService
import rd.professional.web.command.AddAccountCommand
import rd.professional.web.dto.PaymentAccountDto

import static org.springframework.http.HttpStatus.CREATED

class OrganisationPaymentAccountController extends AbstractDtoRenderingController<PaymentAccount, PaymentAccountDto> {
    static responseFormats = ['json']

    OrganisationService organisationService

    OrganisationPaymentAccountController() {
        super(PaymentAccount, PaymentAccountDto)
    }

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
