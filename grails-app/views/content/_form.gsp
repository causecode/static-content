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
        <label class="control-label col-sm-2" for="pageLayout">
            <g:message code="page.pageLayout.label" default="Page Layout" />
        </label>
        <div class="col-sm-5">
            <g:select id="pageLayout" name="pageLayout.id" from="${com.cc.content.PageLayout.list()}" optionKey="id"
                value="${contentInstance?.pageLayout?.id}" noSelection="['null': '']" class="form-control" />
        </div>
    </div>
    <g:if test="${contentInstance.id }">
        <div class="form-group">
            <label class="control-label col-sm-2" for="createRevision">
                <g:message code="content.revision.create.label" default="Create Revision" />
                <i class="icon-question-sign" title="Page Revision will be created after saving new changes"
                    rel="tooltip" data-container="body"></i>
            </label>
            <div class="col-sm-5">
                <div class="checkbox">
                    <label>
                        <g:checkBox name="createRevision" checked="false" />
                    </label>
                </div>
            </div>
        </div>
    </g:if>
</g:if>

<div class="form-group ${hasErrors(bean: contentInstance, field: 'publish', 'error')}">
    <label class="control-label col-sm-2" for="publish">
        <g:message code="page.publish.label" default="Publish" />
    </label>
    <div class="col-sm-5">
        <div class="checkbox">
            <label>
                <g:checkBox name="publish" value="${contentInstance?.publish}" />
            </label>
        </div>
    </div>
</div>

<div class="page-header">
    <h2 class="inline">Meta Tags</h2>
    &nbsp;
    <a href="javascript:void(0)" onclick="$('.form-actions').before($('#meta-form').html())">
        <i class="icon-plus"></i>
    </a>
</div>
<g:each in="${contentInstance?.metaTags }" var="metaInstance">
    <g:render template="/meta/form" model="[metaInstance: metaInstance]" />
</g:each>

<g:if test="${contentInstance instanceof com.cc.content.page.Page && contentInstance.id }">
    <div class="page-header">
        <h2>Revisions</h2>
    </div>
    <ul>
        <g:each in="${contentRevisionList }" var="contentRevisionInstance">
            <li>
                Revised on
                <a href="${createLink(controller: 'contentRevision', id: contentRevisionInstance.id) }" target="_blank">
                    <g:formatDate date="${contentRevisionInstance.dateCreated }" timezone="${session.usersTimeZone }"
                        format="MM/dd/yyyy hh:mm a" />
                </a>&nbsp;
                <a href="#" id="load-revision" rel="tooltip" title="Load this revision" data-revision-id="${contentRevisionInstance.id }">
                    <i class="icon-exchange"></i>
                </a>&nbsp;
                <a href="#" id="delete-revision" rel="tooltip" title="Delete" data-revision-id="${contentRevisionInstance.id }">
                    <i class="icon-remove"></i>
                </a>
            </li>
        </g:each>
        <g:if test="${!contentRevisionList }">
            <li>Sorry, no revision found.</li>
        </g:if>
    </ul>
    <r:script>
        $("a#delete-revision").click(function() {
            var $this = $(this);
            var confirmDelete = confirm("Are you sure want to delete this.");
            if(!confirmDelete) return false;

            $.ajax({
                method: "POST",
                url: "/contentRevision/delete/" + $this.data('revision-id'),
                success: function(resp) {
                    $this.parents("li").fadeOut().remove();
                },
                error: function() {
                    showAlertMessage("Sorry, somthing went wrong.", "danger", {scrollToAlert: true});
                }
            })
            return false;
        })
        $("a#load-revision").click(function() {
            var $this = $(this);
            $.ajax({
                url: "/contentRevision/load/" + $this.data('revision-id'),
                success: function(resp) {
                    $("input#title").val(resp.title);
                    $("input#subTitle").val(resp.subTitle);
                    CKEDITOR.instances['body'].setData(resp.body);
                    showAlertMessage("Revision content loaded.", "info", {scrollToAlert: true});
                },
                error: function() {
                    showAlertMessage("Sorry, somthing went wrong.", "danger", {scrollToAlert: true});
                }
            })
            return false;
        })
    </r:script>
</g:if>

<g:if test="${!contentInstance?.metaTags }">
    <g:render template="/meta/form" />
</g:if>

<div class="hide" id="meta-form">
    <g:render template="/meta/form" />
</div>