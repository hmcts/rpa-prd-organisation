package rd.professional.domain


import grails.rest.Resource

@Resource(readOnly = false, formats = ['json'])
class PaymentAccount {

    UUID paymentAccountId = UUID.randomUUID()

    String pbaNumber

    static belongsTo = [
            organisation: Organisation,
            user: ProfessionalUser
    ]

    static constraints = {
        pbaNumber nullable: false, unique: true
        paymentAccountId nullable: false, unique: true
        user nullable: true
        organisation nullable: true
    }

    static mapping = {
        paymentAccountId type: 'uuid-binary', length: 16
    }

}
