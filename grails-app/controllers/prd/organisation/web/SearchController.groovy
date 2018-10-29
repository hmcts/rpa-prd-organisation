package prd.organisation.web

import grails.rest.*
import grails.converters.*

import prd.organisation.domain.Organisation

class SearchController {
	static responseFormats = ['json']
	
    def approvedOrganisations() { 
        def query = Organisation.where { 
            status == 'APPROVED'
        }
        respond query.list() 
    }

    def pendingOrganisations() { 
        def query = Organisation.where { 
            status == 'PENDING'
        }
        respond query.list() 
    }

}
