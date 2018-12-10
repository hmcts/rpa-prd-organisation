package prd.organisation.domain


import grails.rest.Resource

@Resource(readOnly = false, formats = ['json'])
class Domain {

    String host

    static belongsTo = [organisation: Organisation]

    static constraints = {
        host nullable: false
    }
}
