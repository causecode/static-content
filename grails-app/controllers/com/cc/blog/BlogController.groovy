package com.cc.blog

import org.springframework.dao.DataIntegrityViolationException
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import grails.plugins.springsecurity.Secured;


class BlogController {
	def springSecurityService
	def springSecurityUiService
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
	   def blogList = Blog.createCriteria()
	   def blogs = blogList.list (max:params.max, offset:params.offset) {
		  order("dateCreated","desc")
	   }
	  [blogInstanceList: blogs, blogInstanceTotal: blogs.getTotalCount(), userClass: userClass()]
    }

	@Secured(['ROLE_ADMIN','ROLE_USER'])
    def create() {
        [blogInstance: new Blog(params)]
    }

    def save() {
        def blogInstance = new Blog(params)
		bindData(blogInstance, params, [include: ['title', 'subTitle', 'body']])
		def principal= springSecurityService.getPrincipal()
		if(principal == "anonymousUser") {
					blogInstance.author = principal
		}
		else {
					blogInstance.author = principal.id as String
					
		}
		if (!blogInstance.save(flush: true)) {
            render(view: "create", model: [blogInstance: blogInstance])
            return
        }
        flash.message = message(code: 'default.created.message', args: [message(code: 'blog.label', default: 'Blog'), blogInstance.id])
		params.tags =params.tags.toLowerCase()
		def seperatedTags = params.tags.tokenize(',')
		seperatedTags.each() {
		  blogInstance.addTag(it);
		}
		blogInstance.save()
		redirect(action: "show", id: blogInstance.id)
    }

    def show(Long id) {
		def username
        def blogInstance = Blog.get(id)
		if (!blogInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'blog.label', default: 'Blog'), id])
			redirect(action: "list")
			return
		}
		def userId = blogInstance.author
		if(userId.isNumber()) {
			userId.toInteger()
			def userInstance = userClass().get(userId)  
			username= userInstance.username
		}
		else {
			username= "anonymousUser"
		}
        [blogInstance: blogInstance, username : username]
    }

    def edit(Long id) {
        def blogInstance = Blog.get(id)
        if (!blogInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'blog.label', default: 'Blog'), id])
            redirect(action: "list")
            return
        }
        [blogInstance: blogInstance]
    }

    def update(Long id, Long version) {
         def blogInstance = Blog.get(id)
        if (!blogInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'blog.label', default: 'Blog'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (blogInstance.version > version) {
                blogInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'blog.label', default: 'Blog')] as Object[],
                          "Another user has updated this Blog while you were editing")
                render(view: "edit", model: [blogInstance: blogInstance])
                return
            }
        }

        blogInstance.properties = params

        if (!blogInstance.save(flush: true)) {
            render(view: "edit", model: [blogInstance: blogInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'blog.label', default: 'Blog'), blogInstance.id])
		params.tags =params.tags.toLowerCase()
		def seperatedTags = params.tags.tokenize(',')
		blogInstance.getTags().each() {
		  if(it in seperatedTags) {
			}
		  else {
		  blogInstance.removeTag(it)
		  }
		}
		seperatedTags.each() {
		  blogInstance.addTag(it);
		}
		blogInstance.save()
		 redirect(action: "show", id: blogInstance.id)
    }

    def delete(Long id) {
        def blogInstance = Blog.get(id)
        if (!blogInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'blog.label', default: 'Blog'), id])
            redirect(action: "list")
            return
        }

        try {
            blogInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'blog.label', default: 'Blog'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'blog.label', default: 'Blog'), id])
            redirect(action: "show", id: id)
        }
    }
	
	def findByTag() {
		def blogList= Blog.findAllByTag(params.tag)
		 render(view: "list", model: [blogInstanceList: blogList, blogInstanceTotal: blogList.size(), userClass: userClass()])
	}
	
	protected String lookupUserClassName() {
		SpringSecurityUtils.securityConfig.userLookup.userDomainClassName
	}

	protected Class<?> userClass() {
		grailsApplication.getDomainClass(lookupUserClassName()).clazz
	}
}
