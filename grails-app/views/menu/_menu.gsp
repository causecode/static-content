<g:if test="${menuItemInstance.childItems}">
    <li class='dropdown'>
    	<a id='${menuItemInstance.id}' href='#' role='button' class='dropdown-toggle' data-toggle='dropdown'>
    		${menuItemInstance.title}<b class='caret'></b></a>
            <ul class='dropdown-menu' role='menu' aria-labelledby='${menuItemInstance.id}'>
	            <g:each in="${menuItemInstance.childItems}">
	   				 <li>
                		<a href='${it.url}'>${it.title}</a>
               		</li>
				</g:each>
            </ul>
    </li>
</g:if>
<g:else>
    <li>
    	<a href='${menuItemInstance.url}'>${menuItemInstance.title}</a>
    </li>
</g:else>
       