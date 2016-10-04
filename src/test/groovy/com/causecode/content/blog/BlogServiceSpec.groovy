package com.causecode.content.blog

import com.causecode.BaseTestSetup
import grails.plugins.taggable.Tag
import grails.plugins.taggable.TagLink
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

import java.util.regex.Pattern

@TestFor(BlogService)
@Mock([Blog, Tag, TagLink])
class BlogServiceSpec extends Specification implements BaseTestSetup {

    // Method findBlogContentTypeByValue
    @Unroll
    void "test findBlogContentTypeByValue method when parameter is passed"() {
        when: 'findBlogContentTypeByValue method is called'
        BlogContentType blogContentTypeInstance = service.findBlogContentTypeByValue(parameter)

        then: 'Valid Response'
        blogContentTypeInstance == result

        where:
        parameter    | result
        'tinymce'    | BlogContentType.TINYMCE
        'markdown'   | BlogContentType.MARKDOWN
        'rawcontent' | BlogContentType.RAWCONTENT
    }

    // Method queryModifierBasedOnFilter
    void "test queryModifierBasedOnFilter method when blogInstance and convertToMarkDown is passed"() {
        given: 'Instance of query, tag, monthYearFilter, queryFilter, monthFilter'
        StringBuilder query = new StringBuilder('SELECT * from user')
        String tag = 'Grails'
        Map monthYearFilters = [month: 'september', year: '1991']
        String queryFilter = 'My new Blog'
        String monthFilter = 'september'

        when: 'queryModifierBasedOnFilter method is called'
        StringBuilder resultQuery = service.queryModifierBasedOnFilter(query, tag, monthYearFilters, queryFilter, monthFilter)

        then: 'Valid query instance'
        resultQuery.contains('SELECT * from user, grails.plugins.taggable.TagLink tagLink WHERE b.id = tagLink.tagRef AND')
    }

    // Method updatedMonthFilterListBasedOnPublishedDate
    void "test updatedMonthFilterListBasedOnPublishedDate method when monthFilterList is passed"() {
        given: 'Instance of monthFilterList'
        List<String> monthFilterList = ['jan', 'feb', 'march', 'april']

        when: 'updatedMonthFilterListBasedOnPublishedDate method is called'
        List<String> resultList = service.updatedMonthFilterListBasedOnPublishedDate(monthFilterList)

        then: 'Valid Response'
        resultList != null
    }

    // Method getBlogInstanceList
/*    void "test getBlogInstanceList method when monthFilterList is passed"() {
        given: 'Instance of monthFilterList'
        Pattern patternTag = Pattern.compile('(?s)<p(.*?)>(.*?)<\\/p>') // HTML_PATTERN_TAG
        List<Map> blogList = [getBlogInstance(1), getBlogInstance(2)]

        when: 'updatedMonthFilterListBasedOnPublishedDate method is called'
        List<Blog> resultList = service.getBlogInstanceList(blogList, patternTag)

        then: 'Valid Response'
        resultList != null
    }*/
}
