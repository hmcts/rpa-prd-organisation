package rd.professional.service

import grails.gorm.transactions.Transactional
import rd.professional.domain.Organisation
import rd.professional.domain.ProfessionalUser

@Transactional
class AccountsService {

    def findOrgAccountsByEmail(String email) {
        return Organisation.withCriteria {
            'users' {
                eq('emailId', email)
            }
        }.accounts
    }

    def findUserAccountsByEmail(String email) {
        return ProfessionalUser.withCriteria {
            eq('emailId', email)
        }.accounts
    }
}
