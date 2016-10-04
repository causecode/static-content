package com.causecode.content.meta

import com.causecode.BaseTestSetup
import com.causecode.content.Content
import com.causecode.content.ContentMeta
import com.causecode.content.blog.BlogContentType
import grails.plugins.taggable.Tag
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@Mock([Meta, ContentMeta, Content])
@TestFor(MetaController)
class MetaControllerSpec extends Specification implements BaseTestSetup {

    void "test something"() {
        given: ''
        Meta metaInstance = getMetaInstance(1)

        when: ''
        controller.params.id = metaInstance.id
        boolean boolResult = controller.validate()

        then: ''
        boolResult == true
    }

//    void "test something delete"() { // MetaInstance not found
//        given: ''
//        ContentMeta contentMetaInstance = getContentMetaInstance(1)
//        Meta metaInstance = contentMetaInstance.meta
//        when: ''
//        //controller.params.id = metaInstance.id
//
//        controller.deleteMeta((Long) metaInstance.id)
//
//        then: ''
//        response.dump()
//    }
}
