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

<div class="control-group ${hasErrors(bean: pageInstance, field: 'body', 'error')}">
    <label class="control-label" for="body">
        <g:message code="page.body.label" default="Body" />
    </label>
    <div class="controls">
        <ckeditor:editor name="body" height="300px" width="80%">
            <%= pageInstance?.body %>
        </ckeditor:editor>
    </div>
</div>

<div class="control-group ${hasErrors(bean: pageInstance, field: 'pageLayout', 'error')}">
    <label class="control-label" for="pageLayout">
        <g:message code="page.pageLayout.label" default="Page Layout" />
    </label>
    <div class="controls">
        <g:select id="pageLayout" name="pageLayout.id" from="${com.cc.content.PageLayout.list()}" optionKey="id" value="${pageInstance?.pageLayout?.id}" class="many-to-one" noSelection="['null': '']"/>
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