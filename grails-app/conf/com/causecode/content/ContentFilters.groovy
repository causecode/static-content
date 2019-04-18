package com.causecode.content

/**
 * @author Shashank Agrawal
 *
 */
class ContentFilters {

    /**
     * Dependency injection for the contentService
     */
    ContentService contentService

    /**
     * Filter to check if current user have authority to view the page or blog instance.
     */
    def filters = {
        all(controller: 'page|blog', action: 'show') {
            before = {
                if(params.id) {
                    if(!contentService.isVisible(params.id)) {
                        log.info "Not accessible page visited."
                        redirect controller: controllerName, action: "list"
                        return false
                    }
                }
                return true
            }
        }
    }
}
