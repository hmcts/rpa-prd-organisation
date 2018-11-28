import grails.rest.render.xml.*

import prd.organisation.domain.Organisation
import prd.organisation.config.AppInsights

beans = {
    organisationRenderer(XmlRenderer, Organisation) {
        includes = ['primaryContactFirstName', 'primaryContactEmail']
    }
    appInsights() {
    }
}