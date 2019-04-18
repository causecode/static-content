package com.causecode.content.page

import com.causecode.content.Content
import com.causecode.content.ContentService
import com.causecode.content.meta.Meta
import com.causecode.util.NucleusUtils
import grails.buildtestdata.mixin.Build
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.test.mixin.TestFor
import grails.util.Holders
import org.springframework.dao.DataIntegrityViolationException
import spock.lang.Specification
import spock.lang.Unroll

/**
 * This class contains unit test cases for {@link PageController}
 */
@TestFor(PageController)
@Build([Page])
class PageControllerSpec extends Specification {

    void generatePageInstances(int numberOfInstances, String body = '', boolean publish = false, String title = 'far') {
        numberOfInstances.times {
            Page.build(publish: publish, body: body, title: title)
        }
    }

    void mockSpringSecurityUtils(boolean result) {
        Holders.grailsApplication.config.cc.plugins.content.contentManagerRole = 'manager'

        GroovyMock(SpringSecurityUtils, global: true)
        SpringSecurityUtils.ifAnyGranted(_) >> {
            return result
        }
    }

    void mockContentServiceCreate() {
        ContentService contentService = Mock(ContentService)
        contentService.create(_, _, _, _) >> { Map args, List metaTypes, List metaValues, Class clazz = Content ->
            return Page.build(body: args.body, title: args.title)
        }
        controller.contentService = contentService
    }

    void mockContentServiceUpdate() {
        ContentService contentService = Mock(ContentService)
        contentService.update(_, _, _, _) >> { Map args, Content contentInstance, List metaTypes, List metaValues ->
            return Page.build(body: args.body, title: args.title, subTitle: args.subTitle)
        } >> {
            return Page.buildWithoutSave(body: '', title: '', subTitle: '')
        }

        1 * contentService.createRevision(_, _, _) >> {
            return
        }

        controller.contentService = contentService
    }

    void mockContentServiceDelete() {
        ContentService contentService = Mock(ContentService)
        contentService.delete(_) >> {
            return
        } >> {
            throw new DataIntegrityViolationException('Error')
        }

        controller.contentService = contentService
    }

    void mockNucleusUtilsSave() {
        GroovyMock(NucleusUtils, global: true)

        2 * NucleusUtils.save(_, _, _) >> {
            return true
        } >> {
            return false
        }
    }

    void performSaveRequest() {
        controller.request.method = 'POST'
        controller.request.json = [body: '<p>Test</p>', title: 'Unit Test']
        controller.save()
    }

    void performShowRequest(Long id) {
        controller.response.reset()
        controller.request.method = 'GET'
        controller.params.id = id
        controller.params.subject = 'TestSubject'
        controller.show()
    }

    @Unroll
    void "test byTitle action for param #titlesParam"() {
        given: 'Instances of Page'
        mockSpringSecurityUtils(isContentManager)
        generatePageInstances(1, 'test', true, 'AboutUs')
        generatePageInstances(1, 'test', true, 'ContactUs')
        generatePageInstances(1, 'test', false, 'Footer')

        when: 'byTitle action is hit'
        controller.response.reset()
        controller.params.titles = titlesParam
        controller.request.method = 'GET'
        controller.byTitle()

        then: 'List of matching instances should be returned'
        controller.response.json.instanceList.size() == expectedSize

        where:
        titlesParam         | isContentManager | expectedSize
        null                | false            | 0
        'AboutUs'           | true             | 1
        'AboutUs,ContactUs' | true             | 2
        'dummy,AboutUs'     | true             | 1
        'dummy'             | true             | 0
        'Footer'            | true             | 1
        'Footer'            | false            | 0
    }

    @Unroll
    void "test index action when user is #userType"() {
        given: 'Instances of Page'
        mockSpringSecurityUtils(isContentManager)
        generatePageInstances(70, 'hello')
        generatePageInstances(50, 'world', true)

        when: 'index actions is hit'
        controller.params.max = max
        controller.params.offset = offset
        controller.params.query = query
        controller.params.publish = publish
        controller.request.method = 'GET'
        controller.index()

        then: 'List of Page instances is returned with totalCount'
        controller.modelAndView.model.instanceList.totalCount == totalCount
        controller.modelAndView.model.instanceList.size() == expectedSize
        controller.response.status == 200
        view == '/page/index'

        where:
        isContentManager | max     | offset  | query   | publish | expectedSize | totalCount | userType
        true             | 5       | 0       | null    | null    | 5            | 120        | 'contentManager'
        true             | 10      | 0       | null    | null    | 10           | 120        | 'contentManager'
        false            | 20      | 0       | null    | null    | 20           | 50         | 'not contentManager'
        false            | 50      | 40      | null    | null    | 10           | 50         | 'not contentManager'
        true             | 'dummy' | 0       | null    | null    | 10           | 120        | 'contentManager'
        false            | 10      | 'dummy' | null    | null    | 10           | 50         | 'not contentManager'
        true             | 10      | 0       | 'hello' | null    | 10           | 70         | 'contentManager'
        false            | 10      | 0       | 'hello' | null    | 10           | 50         | 'not contentManager'
        false            | 10      | 0       | 'world' | null    | 10           | 50         | 'not contentManager'
        true             | 10      | 0       | null    | true    | 10           | 50         | 'contentManager'
        true             | 10      | 0       | null    | false   | 10           | 70         | 'contentManager'
        false            | 10      | 0       | null    | true    | 10           | 50         | 'not contentManager'
        false            | 10      | 0       | null    | false   | 10           | 50         | 'not contentManager'
    }

    void "test getMetaTypeList action"() {
        when: 'getMetaTypeList action is hit'
        controller.request.method == 'GET'
        controller.metaTypeList

        then: 'List of typeList should be returned'
        controller.response.json.metaTypeList.size() == 6
        controller.response.json.metaTypeList == Meta.typeList
        controller.response.status == 200
    }

    void "test save action for various save cases"() {
        given: 'Mocked contentService and NucleusUtils call'
        mockContentServiceCreate()
        mockNucleusUtilsSave()

        when: 'save action is hit with valid parameters'
        performSaveRequest()

        then: 'Saved instance should be returned'
        model.pageInstance.id
        model.pageInstance.body == '<p>Test</p>'
        model.pageInstance.title == 'Unit Test'
        controller.response.status == 200

        when: 'save action is hit and instance cannot be saved'
        controller.response.reset()
        performSaveRequest()

        then: 'Error message should be returned'
        controller.response.json.message.contains('Error occurred while saving the Page.')
        controller.response.status == 422
    }

     void "test show action for various cases"() {
         given: 'An instance of Page'
         Page page = Page.build(body: 'test body')
         assert page.id

         when: 'show action is hit and id is not passed in parameters'
         controller.request.method = 'GET'
         controller.show()

         then: 'Server responds with appropriate error message'
         controller.response.json.message == 'Id cannot be null.'
         controller.response.status == 406

         when: 'show action is hit, subject param is provided but body does not contain predefined text'
         performShowRequest(page.id)

         then: 'Page instance should be returned without any changes in body'
         model.pageInstance.id
         model.pageInstance.body == 'test body'
         controller.response.status == 200

         when: 'show action is hit, subject param is provided and instance body contains predefind text'
         page = Page.build(body: 'mailto:jobs@causecode.com?subject="')
         assert page.id
         assert !page.body.contains('TestSubject')
         performShowRequest(page.id)

         then: 'Page instance should be returned and provided subject should be used'
         model.pageInstance.id
         model.pageInstance.body.contains('TestSubject')
         controller.response.status == 200
     }

    void "test update action for various cases"() {
        given: 'An instance of Page and mocked service call'
        mockContentServiceUpdate()

        Page page = Page.build(body: '<p>Unit Test<p>', subTitle: 'test', title: 'test')
        assert page.id

        when: 'update action is hit and instance is updated successfully'
        controller.request.method = 'PUT'
        controller.request.json = [id: page.id, body: '<p>Unit Test</p>', subTitle: 'Unit Test',
                title: 'Unit Test', createRevision: true]
        controller.update()

        then: 'Instance should be updated and updated instance should be returned'
        model.pageInstance.id
        model.pageInstance.title == 'Unit Test'
        model.pageInstance.subTitle == 'Unit Test'
        controller.response.status == 200

        when: 'update action is hit and instance has errors'
        controller.response.reset()
        controller.request.method = 'PUT'
        controller.update()

        then: 'Appropriate error message and status code is returned'
        controller.response.json.message == 'Error occurred while updating the Page.'
        controller.response.status == 422
    }

    void "test delete action for various cases"() {
        given: 'An instance of Page and mocked service call'
        mockContentServiceDelete()
        Page page = Page.build()
        assert page.id

        when: 'delete action is hit with valid parameters'
        controller.request.method = 'DELETE'
        controller.params.id = page.id
        controller.delete()

        then: 'Instance should be deleted successfully and success message should be returned'
        controller.response.json.message == 'Page deleted successfully.'
        controller.response.status == 200

        when: 'delete action is hit and instance cannot be deleted'
        controller.response.reset()
        controller.request.method == 'DELETE'
        controller.params.id = page.id
        controller.delete()

        then: 'Appropriate error message and status code should be returned'
        controller.response.json.message == "Cannot delete Page with id ${page.id}."
        controller.response.status == 406
    }

    void "test getPageInstanceFromParams method for various cases"() {
        when: 'getPageInstanceFromParams is called with invalid parameters'
        controller.pageInstanceFromParams

        then: 'Appropriate error message and status code is returned'
        controller.response.json.message == 'Id cannot be null.'
        controller.response.status == 406

        when: 'getPageInstanceFromParams is called and Page instance cannot be found'
        controller.response.reset()
        controller.params.id = 1111
        controller.pageInstanceFromParams

        then: 'Appropriate error message and status code is returned'
        controller.response.json.message == 'Page with id 1111 does not exist.'
        controller.response.status == 404

        when: 'getPageInstanceFromParams method is called and Page instance exist'
        Page page = Page.build()
        assert page.id

        controller.params.id = page.id
        Page pageInstance = controller.pageInstanceFromParams

        then: 'Page instance is returned'
        pageInstance
        pageInstance.id == page.id
    }
}
