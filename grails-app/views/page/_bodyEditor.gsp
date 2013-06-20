<g:if test="${(textFormatInstance && !textFormatInstance?.editor) || !editorFlag}">
    <div>
        <textarea name="body" rows="25" cols="60" style="width: 80%">
        <%= pageInstance?.body %>
        </textarea>
        <g:hiddenField name="editor" value="false"/>
    </div>
</g:if>
<g:else>
    <g:if test="${switchEditorFlag }">
        <div>
            <ckeditor:editor name="body" height="300px" width="80%">
                <%= pageInstance?.body %>
            </ckeditor:editor>
        </div>
        <div>
            <a class="btn btn-link" onclick="switchEditor('true','false')">Switch to Plain Text</a>
        </div>
        <g:hiddenField name="editor" value="true"/>
    </g:if>
    <g:else>
        <g:if test="${pageInstance?.editor }">
            <div>
                <ckeditor:editor name="body" height="300px" width="80%">
                    <%= pageInstance?.body %>
                </ckeditor:editor>
            </div>
            <div>
                <a class="btn btn-link" onclick="switchEditor('true','false')">Switch to Plain Text</a>
            </div>
            <g:hiddenField name="editor" value="true"/>
        </g:if>
        <g:else>
            <div>
                <textarea name="body" rows="25" cols="60" style="width: 80%">
                <%= pageInstance?.body %>
                </textarea>
            </div>
            <div>
                <a class="btn btn-link" onclick="switchEditor('true','true')">Switch to ckeditor</a>
            </div>
            <g:hiddenField name="editor" value="false"/>
        </g:else>
    </g:else>
</g:else>