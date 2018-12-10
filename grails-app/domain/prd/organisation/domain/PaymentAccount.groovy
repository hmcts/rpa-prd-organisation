package prd.organisation.domain


import grails.rest.Resource

@Resource(readOnly = false, formats = ['json'])
class PaymentAccount {

    String pbaNumber

    static belongsTo = [organisation: Organisation]

    static constraints = {
        pbaNumber nullable: false, unique: true
    }

}
