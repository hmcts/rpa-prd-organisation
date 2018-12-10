import grails.rest.render.xml.XmlRenderer
import prd.organisation.domain.Organisation

beans = {
    organisationRenderer(XmlRenderer, Organisation) {
        includes = ['primaryContactFirstName', 'primaryContactEmail']
    }

    appInsights() {
    }
}