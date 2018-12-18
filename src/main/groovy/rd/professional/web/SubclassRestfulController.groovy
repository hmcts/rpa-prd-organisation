package rd.professional.web


import grails.rest.RestfulController
import prd.organisation.domain.*
import rd.professional.domain.ContactInformation
import rd.professional.domain.Domain
import rd.professional.domain.Organisation
import rd.professional.domain.PaymentAccount
import rd.professional.domain.ProfessionalUser

class SubclassRestfulController<T> extends RestfulController<T> {
    SubclassRestfulController(Class<T> domainClass) {
        this(domainClass, false)
    }

    SubclassRestfulController(Class<T> domainClass, boolean readOnly) {
        super(domainClass, readOnly)
    }

    @Override
    protected T createResource() {
        println "CREATE RESOURCE"

        // create the REST resource domain object
        T child = resource.newInstance()

        // bind to request
        def objToBind = getObjectToBind()
        bindData child, objToBind

        // lookup the parent resource (if creating a nested resource under a parent resource)
        def parentIdPropertyName = params.keySet().find { it -> it =~ /.*Id/ }
        if (parentIdPropertyName) {
            def parentResourceName = ((parentIdPropertyName =~ /(.*)Id/)[0][1]).capitalize()

            def parent
            switch (parentResourceName) {
                case 'Organisation':
                    parent = Organisation.get(params.organisationId)
                    child.organisation = parent

                    break
            }
        }

        return child
    }

    @Override
    protected List<T> listAllResources(Map params) {
        String parentResourceLinkIdName = getParentResourceLinkIdNameIfExists(params)
        if (parentResourceLinkIdName) {
            String parentResource = getParentResourceNameIfExists(params)

            def unfiltered = resource.list(params)
            unfiltered.each { println it.organisationId }
            def owningOrgId = params."$parentResourceLinkIdName"

            return unfiltered.findAll { it -> it.organisationId == Long.parseLong(owningOrgId) }
        }

        super.listAllResources(params)
    }

    private boolean isResourceOwnedByOrganisation() {
        return (T instanceof ProfessionalUser || T instanceof ContactInformation || T instanceof PaymentAccount || T instanceof Domain)
    }

    @Override
    protected T queryForResource(Serializable id) {
        if (isResourceOwnedByOrganisation()) {
            def orgId = params.organisationId
            T.where {
                id == id && organisation.id == orgId
            }.find()
        } else {
            println "QFR - ${this.resource} - id = $id"
            super.queryForResource(id)
        }
    }

    String getParentResourceLinkIdNameIfExists(Map params) {
        params.keySet().find { it -> it =~ /.*Id/ }
    }

    String getParentResourceNameIfExists(Map params) {
        def parentResourceLinkIdName = getParentResourceLinkIdNameIfExists(params)
        parentResourceLinkIdName ? ((parentResourceLinkIdName =~ /(.*)Id/)[0][1]).capitalize() : null
    }

}