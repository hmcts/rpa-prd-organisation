package prd.organisation.domain

import grails.validation.Validateable

enum Status implements Validateable {
    PENDING,
    APPROVED
}