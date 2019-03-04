package rd.professional.web

import grails.gorm.transactions.Transactional
import rd.professional.domain.PaymentAccount
import rd.professional.service.UsersService
import rd.professional.web.command.AddAccountCommand
import rd.professional.web.dto.PaymentAccountDto

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NO_CONTENT

class UserPaymentAccountController extends AbstractDtoRenderingController<PaymentAccount, PaymentAccountDto> {
    static responseFormats = ['json']

    UsersService usersService

    UserPaymentAccountController() {
        super(PaymentAccount, PaymentAccountDto)
    }

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
