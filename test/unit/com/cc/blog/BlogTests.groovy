package com.cc.blog



import java.util.Date;
import grails.test.mixin.*
import org.junit.*
import grails.test.*
import grails.test.mixin.domain.DomainClassUnitTestMixin;
import grails.test.mixin.services.ServiceUnitTestMixin;
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils;
import grails.plugins.springsecurity.SpringSecurityService;
/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestMixin([DomainClassUnitTestMixin, ServiceUnitTestMixin])
@Mock([Blog])
@TestFor(BlogController)
class BlogTests {
	def springSecurityService
	def springSecurityUiService

	void setUp() {
		def blogInstance = new Blog("dateCreated":new Date("17/10/2012"), "lastUpdated":new Date("17/11/2012"), "title":"Targeting Test Types and/or Phases", "subTitle":"To execute the JUnit integration tests you can run",
									"body":"Grails organises tests by phase and by type. A test phase relates to the state of the Grails application during the tests, and the type relates to the testing mechanism.",
									"author":"Bharti")
		mockDomain(Blog, [blogInstance])
		assert blogInstance.id == 1
		
		blogInstance = new Blog("dateCreated":new Date("21/11/2012"), "lastUpdated":new Date("28/11/2012"), "title":"Unit Testing", "subTitle":"The Test Mixins",
								"body":"The previous JUnit 3-style GrailsUnitTestCase class hierarchy is still present in Grails for backwards compatibility, but is now deprecated. The previous documentation on the subject can be found in the Grails 1.3.x documentation",
								"author":"Prachi")
		mockDomain(Blog, [blogInstance])
		assert blogInstance.id == 2

		blogInstance = new Blog("dateCreated":new Date("18/11/2012"), "lastUpdated":new Date("29/11/2012"), "title":"Unit Testing Controllers", "subTitle":"The Basics",
								"body":"You use the grails.test.mixin.TestFor annotation to unit test controllers. Using TestFor in this manner activates the grails.test.mixin.web.ControllerUnitTestMixin and its associated API.",
								"author":"anonymousUser")
		mockDomain(Blog, [blogInstance])
		assert blogInstance.id == 3

		blogInstance = new Blog("dateCreated":new Date("25/11/2012"), "lastUpdated":new Date("26/11/2012"), "title":"Testing View Rendering", "subTitle":"org.springframework.web.servlet.ModelAndView",
								"body":"To test view rendering you can inspect the state of the controller's modelAndView property (an instance of org.springframework.web.servlet.ModelAndView) or you can use the view and model properties provided by the mixin",
								"author":"Deepali")
		mockDomain(Blog, [blogInstance])
		assert blogInstance.id == 4
		
		defineBeans {
			springSecurityService(SpringSecurityService)
		}
	}
	
	void testIndex() {
		controller.index()
		assert response.redirectedUrl == '/blog/list'
	}
	
	void testList() {
		controller.metaClass.userClass = {return "UserClassName"}
		def result = controller.list(2)
		assert result.blogInstanceList.size() == 2
		assert result.blogInstanceTotal == 4
	}
	
	void testListWithoutMax() {
		controller.metaClass.userClass = {return "UserClassName"}
		def result = controller.list()
		assert result.blogInstanceList.size() == 2
		assert result.blogInstanceTotal == 4
	}
	
	void testCreate() {
		params.dateCreated = new Date("11/10/2012")
		params.lastUpdated = new Date("27/10/2012")
		params.title = "Testing Actions Which Return A Map"
		params.subTitle = "Testing XML and JSON Responses"
		params.body = "XML and JSON response are also written directly to the response. Grails' mocking capabilities provide some conveniences for testing XML and JSON response. For example consider the following action:"
		params.author = "Deepa"
		def result=controller.create()
		assert result.blogInstance.author == "Deepa"
	}
	
	void testSave() {
		params.dateCreated = new Date("11/10/2012")
		params.lastUpdated = new Date("27/10/2012")
		params.title = "Testing Actions Which Return A Map"
		params.subTitle = "Testing XML and JSON Responses"
		params.body = "XML and JSON response are also written directly to the response. Grails' mocking capabilities provide some conveniences for testing XML and JSON response. For example consider the following action:"
		params.author = "Deepa"
		params.tags = "Testing, XML, and, JSON, Responses"
		def mockSpringSecurityService =	mockFor(grails.plugins.springsecurity.SpringSecurityService)
		mockSpringSecurityService.demand.getPrincipal() { ->
				[id:1, username: 'admin', firstName:"Vishesh", lastName:"Duggar",
				password: '4cf6829aa93728e8f3c97df913fb1bfa95fe5810e2933a05943f8312a98d9cf2',
				confirmPassword: '4cf6829aa93728e8f3c97df913fb1bfa95fe5810e2933a05943f8312a98d9cf2',
				email: 'vishesh@causecode.com', enabled: true]
		}
		controller.springSecurityService = mockSpringSecurityService.createMock()
		Blog.metaClass.addTag{return true}
		controller.save()
		assert response.redirectedUrl == '/blog/show'+'/5'
	}
	
	void testSaveForInvalidParams() {
		params.dateCreated = new Date("11/10/2012")
		params.lastUpdated = new Date("27/10/2012")
		params.title = "Testing Actions Which Return A Map"
		params.subTitle = "Testing XML and JSON Responses"
		params.body
		params.author = "Deepa"
		params.tags = "Testing, XML, and, JSON, Responses"
		def mockSpringSecurityService =	mockFor(grails.plugins.springsecurity.SpringSecurityService)
		mockSpringSecurityService.demand.getPrincipal() { ->
				[id:1, username: 'admin', firstName:"Vishesh", lastName:"Duggar",
				password: '4cf6829aa93728e8f3c97df913fb1bfa95fe5810e2933a05943f8312a98d9cf2',
				confirmPassword: '4cf6829aa93728e8f3c97df913fb1bfa95fe5810e2933a05943f8312a98d9cf2',
				email: 'vishesh@causecode.com', enabled: true]
		}
		controller.springSecurityService = mockSpringSecurityService.createMock()
		Blog.metaClass.addTag{return true}
		controller.save()
		assert view == "/blog/create"
	}
	
	void testSaveWithoutParams() {
		params.dateCreated
		params.lastUpdated
		params.title
		params.subTitle
		params.body
		params.author
		params.tags
		def mockSpringSecurityService =	mockFor(grails.plugins.springsecurity.SpringSecurityService)
		mockSpringSecurityService.demand.getPrincipal() { ->
				[id:1, username: 'admin', firstName:"Vishesh", lastName:"Duggar",
				password: '4cf6829aa93728e8f3c97df913fb1bfa95fe5810e2933a05943f8312a98d9cf2',
				confirmPassword: '4cf6829aa93728e8f3c97df913fb1bfa95fe5810e2933a05943f8312a98d9cf2',
				email: 'vishesh@causecode.com', enabled: true]
		}
		controller.springSecurityService = mockSpringSecurityService.createMock()
		Blog.metaClass.addTag{return true}
		controller.save()
		assert view == "/blog/create"
	}
	
	
	void testSaveWithOutUserInstance() {
		params.dateCreated = new Date("11/10/2012")
		params.lastUpdated = new Date("27/10/2012")
		params.title = "Testing Actions Which Return A Map"
		params.subTitle = "Testing XML and JSON Responses"
		params.body = "XML and JSON response are also written directly to the response. Grails' mocking capabilities provide some conveniences for testing XML and JSON response. For example consider the following action:"
		params.author = "anonymousUser"
		params.tags = "Testing, XML, and, JSON, Responses"
		def mockSpringSecurityService =	mockFor(grails.plugins.springsecurity.SpringSecurityService)
		mockSpringSecurityService.demand.getPrincipal() { -> return "anonymousUser" }
		controller.springSecurityService = mockSpringSecurityService.createMock()
		Blog.metaClass.addTag{return true}
		controller.save()
		assert response.redirectedUrl == '/blog/show'+'/5'
		def result=controller.show(5)
		assert result.blogInstance.author == "anonymousUser"
	}
	
	void testShow() {
		controller.metaClass.userClass.get = {->
			[id:1, username: 'Prachi', firstName:"Prachi", lastName:"Kulkarni",
			password: '4cf6829aa93728e8f3c97df913fb1bfa95fe5810e2933a05943f8312a98d9cf2',
			confirmPassword: '4cf6829aa93728e8f3c97df913fb1bfa95fe5810e2933a05943f8312a98d9cf2',
			email: 'Prachi@causecode.com', enabled: true]}
		def result=controller.show(2)
		assert result.blogInstance.author == "Prachi"
	}
	
	void testShowWithOutId() {
		controller.metaClass.userClass.get = {->
			[id:1, username: 'Prachi', firstName:"Prachi", lastName:"Kulkarni",
			password: '4cf6829aa93728e8f3c97df913fb1bfa95fe5810e2933a05943f8312a98d9cf2',
			confirmPassword: '4cf6829aa93728e8f3c97df913fb1bfa95fe5810e2933a05943f8312a98d9cf2',
			email: 'Prachi@causecode.com', enabled: true]
		}
		def result=controller.show()
		assert response.redirectedUrl == '/blog/list'
	}
	
	void testShowAnonymousUser() {
		def result=controller.show(3)
		assert result.blogInstance.author == "anonymousUser"
	}
   
	void testEdit() {
		def result=controller.edit(2)
		assert result.blogInstance.author == "Prachi"
	}
	
	void testEditWithoutId() {
		def result=controller.edit()
		assert response.redirectedUrl == '/blog/list'
	}
	
	void testEditWithInvalidId() {
		def result=controller.edit(6)
		assert response.redirectedUrl == '/blog/list'
	}
	
	void testUpdate() {
		params.author="sona"
		params.tags = "Testing, XML, and, JSON, Responses"
		Blog.metaClass.getTags{return true}
		Blog.metaClass.addTag{return true}
		Blog.metaClass.removeTag{return true}
		controller.update(2, 2)
		assert response.redirectedUrl == '/blog/show'+'/2'
		controller.metaClass.userClass.get = {->
			[id:1, username: 'Prachi', firstName:"Prachi", lastName:"Kulkarni",
			password: '4cf6829aa93728e8f3c97df913fb1bfa95fe5810e2933a05943f8312a98d9cf2',
			confirmPassword: '4cf6829aa93728e8f3c97df913fb1bfa95fe5810e2933a05943f8312a98d9cf2',
			email: 'Prachi@causecode.com', enabled: true]
		}
		def updatedValue = controller.show(2)
		assert updatedValue.blogInstance.author == "sona"
	}
	
	void testUpdateWithInvalidId() {
		params.author="sona"
		controller.update(6, 2)
		assert response.redirectedUrl == '/blog/list'
	}
	
	void testDelete() {
		controller.metaClass.userClass = {return "UserClassName"}
		def result = controller.list()
		assert result.blogInstanceTotal == 4
		controller.delete(2)
		result = controller.list()
		assert result.blogInstanceTotal == 3
		assert response.redirectedUrl == '/blog/list'
	}
	
	void testDeleteWithoutId() {
		controller.metaClass.userClass = {return "UserClassName"}
		def result = controller.list()
		assert result.blogInstanceTotal == 4
		controller.delete()
		result = controller.list()
		assert result.blogInstanceTotal == 4
		assert response.redirectedUrl == '/blog/list'
	}
	
	void testDeleteWithInvalidId() {
		controller.metaClass.userClass = {return "UserClassName"}
		def result = controller.list()
		assert result.blogInstanceTotal == 4
		controller.delete(6)
		result = controller.list()
		assert result.blogInstanceTotal == 4
		assert response.redirectedUrl == '/blog/list'
	}
	
	void testFindByTag() {
		Blog.metaClass.'static'.findAllByTag = {return ["List"]}
		controller.metaClass.userClass = {return "UserClassName"}
		params.tag = "the"
		controller.findByTag()
		assert view == "/blog/list"
	}
}
