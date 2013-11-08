<ckeditor:resources />

<g:hasErrors bean="${contentInstance}">
    <ul class="text-danger field-error icons-ul">
        <g:eachError bean="${contentInstance}" var="error">
            <li>
                <i class="icon-li icon-exclamation-sign"></i><g:message error="${error}" />
            </li>
        </g:eachError>
    </ul>
</g:hasErrors>

<div class="form-group ${hasErrors(bean: contentInstance, field: 'title', 'error')}">
    <label class="control-label col-sm-2" for="title"> <g:message code="page.title.label" default="Title" />
    </label>
    <div class="col-sm-5">
        <g:textField name="title" required="" value="${contentInstance?.title}" class="form-control"
            autofocus="" />
    </div>
</div>

<div class="form-group ${hasErrors(bean: contentInstance, field: 'subTitle', 'error')}">
    <label class="control-label col-sm-2" for="subTitle"> <g:message code="page.subTitle.label"
            default="Sub Title" />
    </label>
    <div class="col-sm-5">
        <g:textField name="subTitle" value="${contentInstance?.subTitle}" class="form-control" />
    </div>
</div>

<div class="form-group ${hasErrors(bean: contentInstance, field: 'body', 'error')}">
    <label class="control-label col-sm-2" for="body"> <g:message code="page.body.label" default="Body" />
    </label>
    <div class="col-sm-8">
        <ckeditor:editor name="body" height="300px" width="120%">
            <%= contentInstance?.body %>
        </ckeditor:editor>
    </div>
</div>

<g:if test="${contentInstance instanceof com.cc.content.blog.Blog }">
    <div class="form-group">
        <label class="control-label col-sm-2" for="tags"><g:message code="blog.body.label" default="Tags" /></label>
        <div class="col-sm-5">
            <g:textField name="tags" value="${contentInstance?.tags?.join(', ')}" class="form-control" />
        </div>
    </div>
</g:if>

<g:if test="${contentInstance instanceof com.cc.content.page.Page }">
    <div class="form-group ${hasErrors(bean: contentInstance, field: 'pageLayout', 'error')}">
        <label class="control-label col-sm-2" for="pageLayout"> <g:message code="page.pageLayout.label"
                default="Page Layout" />
        </label>
        <div class="col-sm-5">
            <g:select id="pageLayout" name="pageLayout.id" from="${com.cc.content.PageLayout.list()}" optionKey="id"
                value="${contentInstance?.pageLayout?.id}" class="many-to-one" noSelection="['null': '']" class="form-control" />
        </div>
    </div>
</g:if>

<div class="form-group ${hasErrors(bean: contentInstance, field: 'publish', 'error')}">
    <label class="control-label col-sm-2" for="publish"> <g:message code="page.publish.label" default="Publish" />
    </label>
    <div class="col-sm-5">
        <g:checkBox name="publish" value="${contentInstance?.publish}" />
    </div>
</div>

<div class="page-header">
    <h2 style="display: inline">Meta Tags</h2>
    <small style="padding-left: 43px"> <a href="javascript:void(0)"
        onclick="$('.form-actions').before($('#meta-form').html())"> <i class="icon-plus"></i>
    </a>
    </small>
</div>

<g:each in="${contentInstance?.metaTags }" var="metaInstance">
    <g:render template="/meta/form" model="[metaInstance: metaInstance]" />
</g:each>
<g:if test="${!contentInstance?.metaTags }">
    <g:render template="/meta/form" />
</g:if>

<div class="hide" id="meta-form">
    <g:render template="/meta/form" />
</div>