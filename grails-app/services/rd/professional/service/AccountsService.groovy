package rd.professional.service

import grails.gorm.transactions.Transactional
import rd.professional.domain.Organisation
import rd.professional.domain.PaymentAccount
import rd.professional.domain.ProfessionalUser

@Transactional
class AccountsService {

    def findOrgAccountsByEmail(String email) {
        def organisation = Organisation.withCriteria {
            'users' {
                eq('emailId', email)
            }
        }.find()
        return organisation ? organisation.accounts : null
    }
}
