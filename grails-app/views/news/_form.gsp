<g:hasErrors bean="${newsInstance}">
    <ul class="text-error">
        <g:eachError bean="${newsInstance}" var="error">
            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>>
                <g:message error="${error}" />
            </li>
        </g:eachError>
    </ul>
</g:hasErrors>

<div class="form-group ${hasErrors(bean: newsInstance, field: 'body', 'error')}">
    <label class="control-label col-sm-2" for="body"> <g:message code="news.body.label" default="Body" />
    </label>
    <div class="col-sm-5">
        <g:textField name="body" required="" value="${newsInstance?.body}" class="form-control" />
    </div>
</div>

<div class="form-group ${hasErrors(bean: newsInstance, field: 'title', 'error')}">
    <label class="control-label col-sm-2" for="title"> <g:message code="news.title.label" default="Title" />
    </label>
    <div class="col-sm-5">
        <g:textField name="title" required="" value="${newsInstance?.title}" class="form-control" />
    </div>
</div>

<div class="form-group ${hasErrors(bean: newsInstance, field: 'subTitle', 'error')}">
    <label class="control-label col-sm-2" for="subTitle"> <g:message code="news.subTitle.label"
            default="Sub Title" />
    </label>
    <div class="col-sm-5">
        <g:textField name="subTitle" value="${newsInstance?.subTitle}" class="form-control" />
    </div>
</div>

<div class="form-group ${hasErrors(bean: newsInstance, field: 'author', 'error')}">
    <label class="control-label col-sm-2" for="author"> <g:message code="news.author.label" default="Author" />
    </label>
    <div class="col-sm-5">
        <g:textField name="author" value="${newsInstance?.author}" class="form-control" />
    </div>
</div>

<div class="form-group ${hasErrors(bean: newsInstance, field: 'publish', 'error')}">
    <label class="control-label col-sm-2" for="publish"> <g:message code="news.publish.label" default="Publish" />
    </label>
    <div class="col-sm-5">
        <g:checkBox name="publish" value="${newsInstance?.publish}" />
    </div>
</div>

