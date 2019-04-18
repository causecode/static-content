import com.causecode.marshaller.BlogDomainMarshaller
import com.causecode.marshaller.MenuItemMarshaller
import grails.converters.JSON
import grails.core.GrailsApplication

/**
 * The class is used for App initialization settings.
 */
class ContentBootStrap {

    GrailsApplication grailsApplication

    def init = { servletContext ->
        log.debug 'Content Bootstrap started executing ..'

        JSON.registerObjectMarshaller(new BlogDomainMarshaller())
        JSON.registerObjectMarshaller(new MenuItemMarshaller())

        log.debug 'Content Bootstrap finished executing.'
    }
}
