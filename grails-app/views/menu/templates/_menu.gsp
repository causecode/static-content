<com:canBeVisible instance="${menuInstance}">
    <nav class="navbar navbar-default ${menuCss ?: '' }" role="navigation" id="main-menu-${menuInstance.id }"
        data-menu-id="${menuInstance?.id}">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-${menuInstance.id }-collapse">
                    <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="/"> <g:if test="${brandLogo }">
                        <img class="logo img-responsive" src="${brandLogo }" />
                    </g:if> <g:if test="${brandName }">
                        <span style="${brandCss }" id="brand-name"> ${brandName }
                        </span>
                    </g:if>
                </a>
            </div>
            <div class="collapse navbar-collapse navbar-${menuInstance.id }-collapse">
                <ul class="nav navbar-nav navbar-right" role="navigation" id="menu-item-${menuInstance?.id }">
                    <g:each in="${menuItemList}">
                        <g:if test="${it && !it.parent }">
                            <g:render template="/menuItem/templates/menuItem" model="[menuItemInstance: it]" plugin="content" />
                        </g:if>
                    </g:each>
                </ul>
            </div>
        </div>
    </nav>
</com:canBeVisible>