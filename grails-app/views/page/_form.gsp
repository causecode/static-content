<ckeditor:resources />

<g:hasErrors bean="${pageInstance}">
    <ul class="text-error">
        <g:eachError bean="${pageInstance}" var="error">
            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>>
                <g:message error="${error}" />
            </li>
        </g:eachError>
    </ul>
</g:hasErrors>

<div class="form-group ${hasErrors(bean: pageInstance, field: 'title', 'error')}">
    <label class="control-label col-sm-2" for="title"> <g:message code="page.title.label" default="Title" />
    </label>
    <div class="col-sm-5">
        <g:textField name="title" required="" value="${pageInstance?.title}" class="form-control" />
    </div>
</div>

<div class="form-group ${hasErrors(bean: pageInstance, field: 'subTitle', 'error')}">
    <label class="control-label col-sm-2" for="subTitle"> <g:message code="page.subTitle.label"
            default="Sub Title" />
    </label>
    <div class="col-sm-5">
        <g:textField name="subTitle" value="${pageInstance?.subTitle}" class="form-control" />
    </div>
</div>

<div class="form-group ${hasErrors(bean: pageInstance, field: 'body', 'error')}">
    <label class="control-label col-sm-2" for="body"> <g:message code="page.body.label" default="Body" />
    </label>
    <div class="col-sm-5">
        <ckeditor:editor name="body" height="300px" width="120%">
            <%= pageInstance?.body %>
        </ckeditor:editor>
    </div>
</div>

<div class="form-group ${hasErrors(bean: pageInstance, field: 'pageLayout', 'error')}">
    <label class="control-label col-sm-2" for="pageLayout"> <g:message code="page.pageLayout.label"
            default="Page Layout" />
    </label>
    <div class="col-sm-5">
        <g:select id="pageLayout" name="pageLayout.id" from="${com.cc.content.PageLayout.list()}" optionKey="id"
            value="${pageInstance?.pageLayout?.id}" class="many-to-one" noSelection="['null': '']" class="form-control" />
    </div>
</div>

<div class="form-group ${hasErrors(bean: pageInstance, field: 'publish', 'error')}">
    <label class="control-label col-sm-2" for="publish"> <g:message code="page.publish.label" default="Publish" />
    </label>
    <div class="col-sm-5">
        <g:checkBox name="publish" value="${pageInstance?.publish}" />
    </div>
</div>

<div class="page-header">
    <h2 style="display: inline">Meta Tags</h2>
    <small style="padding-left: 43px"> <a href="javascript:void(0)"
        onclick="$('.form-actions').before($('#meta-form').html())"> <i class="icon-plus"></i>
    </a>
    </small>
</div>

<g:each in="${pageInstance?.metaTags }" var="metaInstance">
    <g:render template="/meta/form" model="[metaInstance: metaInstance]" />
</g:each>
<g:if test="${!pageInstance?.metaTags }">
    <g:render template="/meta/form" />
</g:if>

<div class="hide" id="meta-form">
    <g:render template="/meta/form" />
</div>