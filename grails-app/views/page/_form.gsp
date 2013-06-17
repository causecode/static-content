<ckeditor:resources/>

<g:hasErrors bean="${pageInstance}">
    <ul class="text-error">
        <g:eachError bean="${pageInstance}" var="error">
            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>>
                <g:message error="${error}" /></li>
        </g:eachError>
    </ul>
</g:hasErrors>

<div class="control-group ${hasErrors(bean: pageInstance, field: 'title', 'error')}">
    <label class="control-label" for="title">
        <g:message code="page.title.label" default="Title" />
    </label>
    <div class="controls">
        <g:textField name="title" required="" value="${pageInstance?.title}"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: pageInstance, field: 'subTitle', 'error')}">
    <label class="control-label" for="subTitle">
        <g:message code="page.subTitle.label" default="Sub Title" />
    </label>
    <div class="controls">
        <g:textField name="subTitle" value="${pageInstance?.subTitle}"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: pageInstance, field: 'textFormat.name', 'error')}">
    <label class="control-label" for="textFormat.name">
        <g:message code="textFormat.name.label" default="Text Format" />
    </label>
    <div class="controls">
        <g:select name="textFormat.name" from="${formatsAvailable }" />
    </div>
</div>

<div class="control-group ${hasErrors(bean: pageInstance, field: 'body', 'error')}">
    <label class="control-label" for="body">
        <g:message code="page.body.label" default="Body" />
    </label>
    <g:if test="${editor}" >
        <div class="controls" >
            <ckeditor:editor name="body" height="300px" width="80%">
                <%= pageInstance?.body %>
            </ckeditor:editor>
        </div>
    </g:if>
    <g:else>
        <div class="controls" > 
            <textarea name="body" rows="25" cols="60" style="width: 80%">
                <%= pageInstance?.body %>
            </textarea>
        </div>
    </g:else>
</div>

<div class="control-group ">
    <label class="control-label">
        <g:message code="blank.label" default="" />
    </label>
    <g:if test="${editor}">
        <div class="controls">
            <g:link action="create" params="[editor:false]" class="btn btn-link">Switch to Plain Text</g:link>
        </div>
    </g:if>
    <g:else>
        <div class="controls">
            <g:link action="create" params="[editor:true]" class="btn btn-link">Switch to Ckeditor</g:link>
        </div>
    </g:else>
</div>

<div class="control-group ${hasErrors(bean: pageInstance, field: 'pageLayout', 'error')}">
    <label class="control-label" for="pageLayout">
        <g:message code="page.pageLayout.label" default="Page Layout" />
    </label>
    <div class="controls">
        <g:select id="pageLayout" name="pageLayout.id" from="${com.cc.content.PageLayout.list()}"
            optionKey="id" value="${pageInstance?.pageLayout?.id}" class="many-to-one" noSelection="['null': '']"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: pageInstance, field: 'publish', 'error')}">
    <label class="control-label" for="publish">
        <g:message code="page.publish.label" default="Publish" />
    </label>
    <div class="controls">
        <g:checkBox name="publish" value="${pageInstance?.publish}" />
    </div>
</div>

<div class="page-header">
    <h2 style="display: inline">Meta Tags</h2>
    <small style="padding-left: 43px">
        <a href="javascript:void(0)" onclick="$('.form-actions').before($('#meta-form').html())">
            <i class="icon-plus"></i>
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