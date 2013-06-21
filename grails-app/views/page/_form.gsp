<ckeditor:resources/>
<g:hiddenField name="id" value="${pageInstance.id }"/>

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

<div class="control-group ${hasErrors(bean: pageInstance, field: 'textFormat.id', 'error')}">
    <label class="control-label" for="textFormat.id">
        <g:message code="textFormat.id.label" default="Text Format" />
    </label>
    <div class="controls" id="drop-down">
        <g:select name="textFormat.id" from="${formatsAvailable }" optionKey="id" optionValue="name"
             value="${pageInstance?.textFormat }" />
    </div>
</div>

<r:script>
    var noEditorIdList = ${com.cc.content.format.TextFormat.findAllByEditor(false)*.id};
</r:script>

<div class="control-group ${hasErrors(bean: pageInstance, field: 'body', 'error')}">
    <label class="control-label" for="body">
        <g:message code="page.body.label" default="Body" />
    </label>
    <div class="controls" id="editor" >
        <g:if test="${!formatsAvailable*.editor.contains(true) }">
            <textarea name="body" rows="25" cols="60" style="width: 80%">
            <%= pageInstance?.body %>
            </textarea>
            <g:hiddenField name="editor" value="false"/>
        </g:if>
        <g:else>
            <r:require module="editorswitch"/>
            <g:if test="${pageInstance.id }">
                <g:render template="bodyEditor" model="['pageInstance':pageInstance ,
                 'textFormatInstance':pageInstance?.textFormat ]" />
            </g:if>
            <g:else>
                <g:render template="bodyEditor" model="['useEditor':true]"/>
            </g:else>
        </g:else>
    </div>
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