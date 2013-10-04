<ol class="breadcrumb" id="content-breadcrumb">
    <g:each in="${baseURLMap }">
        <g:if test="${it.key == 'active' }">
            <li class="active">${it.value }</li>
        </g:if>
        <g:else>
            <li><a href="${it.key }">${it.value }</a></li>
        </g:else>
    </g:each>
    <g:each in="${urlMap }">
        <g:if test="${it.key == 'active' }">
            <li class="active">${it.value }</li>
        </g:if>
        <g:else>
            <li><a href="${it.key }">${it.value }</a></li>
        </g:else>
    </g:each>
</ol>