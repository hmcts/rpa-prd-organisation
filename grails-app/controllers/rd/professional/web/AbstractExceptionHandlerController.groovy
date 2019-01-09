package rd.professional.web

import grails.rest.RestfulController
import rd.professional.exception.HttpException

abstract class AbstractExceptionHandlerController<T> extends RestfulController<T> {
    static responseFormats = ['json', 'xml']

    AbstractExceptionHandlerController(Class<T> resource) {
        super(resource)
    }

    def connectException(final ConnectException exception) {
        logException exception
        render view: 'error', model: [exception: exception]
    }

    def httpException(final HttpException exception) {
        respond exception.getMessage(), status: exception.statusCode
    }


    /** Log exception */
    private void logException(final Exception exception) {
        log.error "Exception occurred. ${exception?.message}", exception
    }
}
