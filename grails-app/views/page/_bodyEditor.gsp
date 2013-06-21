<g:if test="${textFormatInstance && !textFormatInstance?.editor}">
    <textarea name="body" rows="25" cols="60" style="width: 80%"><%= pageInstance?.body %></textarea>
    <g:hiddenField name="editor" value="false"/>
</g:if>
<g:else>
    <g:if test="${useEditor }">
        <ckeditor:editor name="body" height="300px" width="80%">
            <%= pageInstance?.body %>
        </ckeditor:editor>
        <a class="btn btn-link" onclick="switchEditor(false)">Switch to Plain Text</a>
        <g:hiddenField name="editor" value="true"/>
    </g:if>
    <g:else>
        <g:if test="${pageInstance?.editor }">
            <ckeditor:editor name="body" height="300px" width="80%">
                <%= pageInstance?.body %>
            </ckeditor:editor>
            <a class="btn btn-link" onclick="switchEditor(false)">Switch to Plain Text</a>
            <g:hiddenField name="editor" value="true"/>
        </g:if>
        <g:else>
            <textarea name="body" rows="25" cols="60" style="width: 80%"><%= pageInstance?.body %></textarea>
            <a class="btn btn-link" onclick="switchEditor(true)">Switch to ckeditor</a>
            <g:hiddenField name="editor" value="false"/>
        </g:else>
    </g:else>
</g:else>