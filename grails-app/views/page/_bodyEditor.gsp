<g:if test="${textFormatInstance && !textFormatInstance?.editor}">
    <textarea name="body" rows="25" cols="60" style="width: 80%"><%= pageInstance?.body %></textarea>
    <g:hiddenField name="editor" value="false"/>
</g:if>
<g:else>
    <g:if test="${useEditor }">
        <ckeditor:editor name="body" height="300px" width="80%">
            <%= pageInstance?.body %>
        </ckeditor:editor>
        <div class="btn-group">
            <a class="btn btn-primary" onclick="switchEditor(false)">Plain Text</a>
            <a class="btn btn-primary active" href="javascript:void(0)">Ckeditor</a>
        </div>
        <g:hiddenField name="editor" value="true"/>
    </g:if>
    <g:else>
        <g:if test="${pageInstance?.editor }">
            <ckeditor:editor name="body" height="300px" width="80%">
                <%= pageInstance?.body %>
            </ckeditor:editor>
            <div class="btn-group">
                <a class="btn btn-primary" onclick="switchEditor(false)">Plain Text</a>
                <a class="btn btn-primary active" href="javascript:void(0)">Ckeditor</a>
            </div>
            <g:hiddenField name="editor" value="true"/>
        </g:if>
        <g:else>
            <textarea name="body" rows="25" cols="60" style="width: 80%"><%= pageInstance?.body %></textarea>
            <div class="btn-group">
                <a class="btn btn-primary active" href="javascript:void(0)">Plain Text</a>
                <a class="btn btn-primary" onclick="switchEditor(true)">Ckeditor</a>
            </div>
            <g:hiddenField name="editor" value="false"/>
        </g:else>
    </g:else>
</g:else>