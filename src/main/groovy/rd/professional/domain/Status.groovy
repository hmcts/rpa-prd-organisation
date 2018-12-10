package rd.professional.domain

import grails.validation.Validateable

enum Status implements Validateable {
    PENDING,
    APPROVED
}