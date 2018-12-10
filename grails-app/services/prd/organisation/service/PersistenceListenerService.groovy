package prd.organisation.service

import grails.events.annotation.Subscriber
import grails.gorm.transactions.Transactional
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEvent
import org.grails.datastore.mapping.engine.event.PostUpdateEvent
import prd.organisation.domain.Organisation
import prd.organisation.domain.ProfessionalUser
import prd.organisation.domain.Status

@Transactional
class PersistenceListenerService {

    def usersService

    Object domainFromEvent(AbstractPersistenceEvent event) {
        if (event.entityObject instanceof Organisation) {
            return ((Organisation) event.entityObject)
        } else if (event.entityObject instanceof ProfessionalUser) {
            return ((ProfessionalUser) event.entityObject)
        }
        null
    }

    @Subscriber
    void afterUpdate(PostUpdateEvent event) {
        def organisation = domainFromEvent(event)
        if (organisation instanceof Organisation) {
            Long id = organisation?.id

            if (id && organisation?.status == Status.APPROVED) {
                Organisation persistedOrganisation = Organisation.findById(id)

                if (persistedOrganisation) {
                    log.info "Approved organisation detected ${persistedOrganisation.name}, approving superuser"
                    persistedOrganisation.users*.status = Status.APPROVED  // approve all for now (will only be one)
                }

            }
        }
    }

}
