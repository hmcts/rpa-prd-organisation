import grails.rest.render.xml.*

import prd.organisation.domain.Organisation

beans = {
    organisationRenderer(XmlRenderer, Organisation) {
        includes = ['primaryContactFirstName', 'primaryContactEmail']
    }
}