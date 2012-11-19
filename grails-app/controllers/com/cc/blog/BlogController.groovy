package com.cc.blog

import org.springframework.dao.DataIntegrityViolationException

class BlogController {
	def springSecurityService
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
     //   params.max = Math.min(max ?: 10, 100)
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
	  [blogInstanceList: blogs, blogInstanceTotal: blogs.getTotalCount()]
       //[blogInstanceList: Blog.list(params), blogInstanceTotal: Blog.count()]
    }

    def create() {
        [blogInstance: new Blog(params)]
    }

    def save() {
        
        def blogInstance = new Blog(params)
				bindData(blogInstance, params, [include: ['title', 'subTitle', 'body']])
				println  springSecurityService.getPrincipal()
				def principal= springSecurityService.getPrincipal()
			//	println principal.username
				if(principal == "anonymousUser")
				{
					blogInstance.author = principal
				}
				else
				{
					blogInstance.author = principal.id
					
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
		def userId = blogInstance.author
		if(userId.isNumber()) {
			//def principal= springSecurityService.getPrincipal()
			//Class userClass = principal.getClass()
			def userClass = Class.forName("grails.plugins.springsecurity.userLookup.userDomainClassName")
			userId.toInteger()
			userInstance = userClass.getById(userId)
			username= userInstance.username
		}
		else {
			username= "anonymousUser"
			}
        if (!blogInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'blog.label', default: 'Blog'), id])
            redirect(action: "list")
            return
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
		 render(view: "list", model: [blogInstanceList: blogList, blogInstanceTotal: blogList.size(), activeNav:"blog_nav"])
	   }
}
