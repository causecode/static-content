<%@ page import="com.cc.blog.Blog"%>

<div class="form-group ${hasErrors(bean: blogInstance, field: 'title', 'error')} required">
    <label class="control-label col-sm-2" for="title"> 
        <g:message code="blog.title.label" default="Title" /><span class="required-indicator">*</span>
    </label>
    <div class="col-sm-5">
        <g:textField name="title" required="" value="${blogInstance?.title}" class="form-control" />
    </div>
</div>

<div class="form-group ${hasErrors(bean: blogInstance, field: 'subTitle', 'error')} required">
    <label class="control-label col-sm-2" for="subTitle"> <g:message code="blog.subTitle.label" default="Sub Title" /> <span
        class="required-indicator">*</span>
    </label>
    <div class="col-sm-5">
        <g:textField name="subTitle" required="" value="${blogInstance?.subTitle}" class="form-control" />
    </div>
</div>

<div class="form-group ${hasErrors(bean: blogInstance, field: 'body', 'error')} required">
    <label class="control-label col-sm-2" for="body"> <g:message code="blog.body.label" default="Body" /> <span
        class="required-indicator">*</span>
    </label>
    <div class="col-lg-5">
        <ckeditor:editor name="body" height="300px" width="120%">
            <%= blogInstance?.body %>
        </ckeditor:editor>
    </div>
</div>

<div class="form-group">
    <label class="control-label col-sm-2" for="tags"> <g:message code="blog.body.label" default="Tags" />
    </label>
    <g:set var="presentTags" value="" />
    <g:each in="${blogInstance?.tags}">
        <g:set var="presentTags" value="${presentTags +", " + it}" />
    </g:each>
    <div class="col-sm-5">
        <g:textField name="tags" required="" value="${presentTags}" class="form-control" />
    </div>
</div>



