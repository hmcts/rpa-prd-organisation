package prd.organisation.service

import grails.gorm.transactions.Transactional
import grails.events.annotation.Subscriber
import grails.events.annotation.gorm.Listener
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEvent
import org.grails.datastore.mapping.engine.event.PostDeleteEvent
import org.grails.datastore.mapping.engine.event.PostInsertEvent
import org.grails.datastore.mapping.engine.event.PostUpdateEvent

import prd.organisation.domain.*

@Transactional
class PersistenceListenerService {

    def usersService

    Object domainFromEvent(AbstractPersistenceEvent event) {
        if ( event.entityObject instanceof Organisation ) {
            return ((Organisation) event.entityObject)
        } 
        else 
        if ( event.entityObject instanceof ProfessionalUser ) {
            return ((ProfessionalUser) event.entityObject)
        }        
        null
    }

    @Subscriber     
    void afterInsert(PostInsertEvent event) {
        Object domain = domainFromEvent(event)

        if (domain.class.name == 'prd.organisation.domain.Organisation') {
            Organisation organisation = (Organisation) domain

            Long id = organisation?.id

            String firstName    = organisation.primaryContactFirstName
            String lastName     = organisation.primaryContactLastName
            String email        = organisation.primaryContactEmail

            // then add to users
            log.debug "Creating entry in Professional Users table"
            Organisation persistedOrganisation = Organisation.findById(id)
            persistedOrganisation.addToUsers(new ProfessionalUser(firstName: firstName, lastName: lastName, emailId: email)).save(flush: true, failOnError: true)
        }
    }

    @Subscriber     
    void afterUpdate(PostUpdateEvent event) {
        Organisation organisation = domainFromEvent(event)

        Long id = organisation?.id

        if (id && organisation?.status == Status.APPROVED) {
            String firstName    = organisation.primaryContactFirstName
            String lastName     = organisation.primaryContactLastName
            String email        = organisation.primaryContactEmail
            // TODO - retrieve all roles for PUI application from Access Management microservice
            String[] roles      = ['PUI_CASE_MANAGER', 'PUI_ORGANISATION_MANAGER', 'PUI_FINANCE_MANAGER']
            
            try {
                // do REST call to PRD User to insert the user (this will kick off IDAM dynamic user registration)
                log.debug "*** Calling IDAM user creation API ***"
                log.debug "$firstName, $lastName, $email, $roles"
                usersService.createUser firstName, lastName, email, roles

            } catch (Exception e) {
                log.warn "An exception occurred invoking the IDAM user creation API: ${e.getMessage()}"
                //throw new RuntimeException("Could not invoke IDAM service!")

                // revert tp mark organisation as PENDING still
                Organisation.withTransaction {
                    Organisation persistedOrganisation = Organisation.get(id)

                    println "PERS ORG = $persistedOrganisation"

                    persistedOrganisation.status = Status.PENDING   
                    persistedOrganisation.save(flush: true, failOnError: true)

                    println "SAVED"                    
                }
            }
        }
    }

}
