<g:hasErrors bean="${newsInstance}">
    <ul class="text-error">
        <g:eachError bean="${newsInstance}" var="error">
            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>>
                <g:message error="${error}" /></li>
        </g:eachError>
    </ul>
</g:hasErrors>

<div class="control-group ${hasErrors(bean: newsInstance, field: 'body', 'error')}">
    <label class="control-label" for="body">
        <g:message code="news.body.label" default="Body" />
    </label>
    <div class="controls">
        <g:textField name="body" required="" value="${newsInstance?.body}"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: newsInstance, field: 'title', 'error')}">
    <label class="control-label" for="title">
        <g:message code="news.title.label" default="Title" />
    </label>
    <div class="controls">
        <g:textField name="title" required="" value="${newsInstance?.title}"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: newsInstance, field: 'subTitle', 'error')}">
    <label class="control-label" for="subTitle">
        <g:message code="news.subTitle.label" default="Sub Title" />
    </label>
    <div class="controls">
        <g:textField name="subTitle" value="${newsInstance?.subTitle}"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: newsInstance, field: 'author', 'error')}">
    <label class="control-label" for="author">
        <g:message code="news.author.label" default="Author" />
    </label>
    <div class="controls">
        <g:textField name="author" value="${newsInstance?.author}"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: newsInstance, field: 'publish', 'error')}">
    <label class="control-label" for="publish">
        <g:message code="news.publish.label" default="Publish" />
    </label>
    <div class="controls">
        <g:checkBox name="publish" value="${newsInstance?.publish}" />
    </div>
</div>

