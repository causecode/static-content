/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.blog


import spock.lang.*

import com.cc.crm.BaseIntegrationTestCase


class BlogControllerSpec extends BaseIntegrationTestCase {

    BlogController controller
    Blog blogInstance

    def setup() {
        controller = new BlogController()

        Map params = getContentParams(1) + [publish: true, publishedDate: new Date()]
        blogInstance = new Blog(params)
        assert blogInstance.save()
        assert blogInstance.id
    }

    def cleanup() {
    }

    void "test show action with default parameteres"() {
        given: "Populating parameters"
        controller.params.id = blogInstance.id

        when: "Blog ID parameter passed."
        controller.request.method = "GET"
        controller.show()

        then: "Redirected to blog show angular based URL."
        controller.response.redirectedUrl.contains('/blog/show/' + blogInstance.id)
    }

    void "test show action for Google crawler request"() {
        given: "Populating parameters"

        controller.params.id = blogInstance.id
        controller.params._escaped_fragment_ = true

        and: "Populating show GSP content"
        List<Blog> blogInstanceList = Blog.findAllByPublish(true, [max: 5, sort: 'publishedDate', order: 'desc'])
        Map result = [blogInstance: blogInstance, comments: [], tagList: [], blogInstanceList: blogInstanceList, 
            blogInstanceTags: []]
        String blogContent = controller.g.render(template: "/blog/show", model: result)

        when:"Blog ID and _escaped_fragment_ parameter passed."
        controller.request.method = "GET"
        controller.show()

        then: "Redirected to blog show angular based URL."
        controller.response.text == blogContent
    }

    void "test show action for ajax request "() {
        given: "Populating parameters"
        controller.params.id = blogInstance.id

        when: "Ajax request comes in for show action"
        controller.request.method = "GET"
        controller.request.makeAjaxRequest()
        controller.show()

        then: "Should respond json data containing page instance"
        controller.response.json["blogInstance"]
        controller.response.json["blogInstance"].id
        controller.response.json["blogInstance"].title == blogInstance.title
        controller.response.json["blogInstance"].body == blogInstance.body
        controller.response.json["blogInstance"].subTitle == blogInstance.subTitle
        controller.response.json["blogInstance"].publish
    }
}
