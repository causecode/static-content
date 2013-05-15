<g:hasErrors bean="${FAQInstance}">
    <ul class="text-error">
        <g:eachError bean="${FAQInstance}" var="error">
            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>>
                <g:message error="${error}" /></li>
        </g:eachError>
    </ul>
</g:hasErrors>

<div class="control-group ${hasErrors(bean: FAQInstance, field: 'body', 'error')}">
    <label class="control-label" for="body">
        <g:message code="FAQ.body.label" default="Body" />
    </label>
    <div class="controls">
        <g:textField name="body" required="" value="${FAQInstance?.body}"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: FAQInstance, field: 'title', 'error')}">
    <label class="control-label" for="title">
        <g:message code="FAQ.title.label" default="Title" />
    </label>
    <div class="controls">
        <g:textField name="title" required="" value="${FAQInstance?.title}"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: FAQInstance, field: 'subTitle', 'error')}">
    <label class="control-label" for="subTitle">
        <g:message code="FAQ.subTitle.label" default="Sub Title" />
    </label>
    <div class="controls">
        <g:textField name="subTitle" value="${FAQInstance?.subTitle}"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: FAQInstance, field: 'author', 'error')}">
    <label class="control-label" for="author">
        <g:message code="FAQ.author.label" default="Author" />
    </label>
    <div class="controls">
        <g:textField name="author" value="${FAQInstance?.author}"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: FAQInstance, field: 'publish', 'error')}">
    <label class="control-label" for="publish">
        <g:message code="FAQ.publish.label" default="Publish" />
    </label>
    <div class="controls">
        <g:checkBox name="publish" value="${FAQInstance?.publish}" />
    </div>
</div>

