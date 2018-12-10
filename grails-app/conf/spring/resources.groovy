import grails.rest.render.xml.XmlRenderer
import rd.professional.domain.Organisation

beans = {
    organisationRenderer(XmlRenderer, Organisation) {
        includes = ['primaryContactFirstName', 'primaryContactEmail']
    }

    appInsights() {
    }
}