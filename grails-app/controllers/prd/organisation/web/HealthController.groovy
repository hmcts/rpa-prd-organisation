package prd.organisation.web


import grails.rest.*
import grails.converters.*
import prd.organisation.domain.*

class HealthController {
	static responseFormats = ['json']
	
    def index() { 
        render '{"status": "UP"}'
    }
}
