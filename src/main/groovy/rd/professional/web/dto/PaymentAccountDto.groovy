package rd.professional.web.dto

import rd.professional.domain.PaymentAccount

class PaymentAccountDto {

    String pbaNumber
    UUID userId
    UUID organisationId

    PaymentAccountDto(PaymentAccount account) {
        this.pbaNumber = account.pbaNumber
        this.userId = account.user ? account.user.userId : null
        this.organisationId = account.organisation.organisationId
    }
}
