package com.cc.page

import org.springframework.dao.DataIntegrityViolationException

class PageController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        if(!params.max) {
            params.max=2
       }
       if(!params.offset) {
            params.offset=0
       }
       def pageList = Page.createCriteria()
       def pages = pageList.list (max:params.max, offset:params.offset) {
          order("dateCreated","desc")
       }
      [pageInstanceList: pages, pageInstanceTotal: pages.getTotalCount()]
    }

    def create() {
        [pageInstance: new Page(params)]
    }

    def save() {
        def pageInstance = new Page(params)
		println  springSecurityService.getPrincipal()
		def principal= springSecurityService.getPrincipal()
	//	println principal.username
		if(principal == "anonymousUser")
		{
			pageInstance.author = principal
		}
		else
		{
			pageInstance.author = principal.id
			
		}
        if (!pageInstance.save(flush: true)) {
            render(view: "create", model: [pageInstance: pageInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'page.label', default: 'Page'), pageInstance.id])
        redirect(action: "show", id: pageInstance.id)
    }

    def show(Long id) {
        def pageInstance = Page.get(id)
        if (!pageInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'page.label', default: 'Page'), id])
            redirect(action: "list")
            return
        }

        [pageInstance: pageInstance]
    }

    def edit(Long id) {
        def pageInstance = Page.get(id)
        if (!pageInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'page.label', default: 'Page'), id])
            redirect(action: "list")
            return
        }

        [pageInstance: pageInstance]
    }

    def update(Long id, Long version) {
        def pageInstance = Page.get(id)
        if (!pageInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'page.label', default: 'Page'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (pageInstance.version > version) {
                pageInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'page.label', default: 'Page')] as Object[],
                          "Another user has updated this Page while you were editing")
                render(view: "edit", model: [pageInstance: pageInstance])
                return
            }
        }

        pageInstance.properties = params

        if (!pageInstance.save(flush: true)) {
            render(view: "edit", model: [pageInstance: pageInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'page.label', default: 'Page'), pageInstance.id])
        redirect(action: "show", id: pageInstance.id)
    }

    def delete(Long id) {
        def pageInstance = Page.get(id)
        if (!pageInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'page.label', default: 'Page'), id])
            redirect(action: "list")
            return
        }

        try {
            pageInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'page.label', default: 'Page'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'page.label', default: 'Page'), id])
            redirect(action: "show", id: id)
        }
    }
}
