<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName" value="${message(code: 'FAQ.label', default: 'FAQs')}" />
<title><g:message code="default.faq.list.label" args="[entityName]" /></title>
</head>
<body>
    <content tag="breadcrumb"> <content:breadcrumb map="['active': 'FAQs']" /> </content>

    <div class="page-header">
        <h2>
            <g:message code="default.faq.list.label" args="[entityName]" />
        </h2>
    </div>

    <g:each in="${FAQInstanceList}" var="FAQInstanceInstance" status="index">
        <div id="FAQQuestion" style="margin-bottom: 6px; margin-left: 20px;">
            <span> ${index + 1}.&nbsp;
            </span> <a href="#faq-${index}"> ${FAQInstanceInstance.title}
            </a> <br>
        </div>
    </g:each>
    <g:set var="limit" value="${(params.offset.toInteger() + params.max.toInteger()) }"></g:set>
    <span class="muted help-block"> <small>Showing: <strong> ${params.offset.toInteger() + 1 }-${limit > FAQInstanceTotal ? FAQInstanceTotal : limit }</strong>
            of <strong> ${FAQInstanceTotal }
        </strong>
    </small>
    </span>
    <div class="pagination">
        <g:paginate total="${FAQInstanceTotal}" />
    </div>
    <h2>Answers</h2>
    <g:each in="${FAQInstanceList}" var="FAQInstanceInstance" status="index">
        <div id="faq-${index}" style="margin-bottom: 10px; margin-left: 20px;">
            <h4 class="inline">${index + 1}.&nbsp;${FAQInstanceInstance.title}</h4>
            <br>
            ${FAQInstanceInstance.body}
        </div>
    </g:each>
    <g:set var="limit" value="${(params.offset.toInteger() + params.max.toInteger()) }"></g:set>
    <span class="muted help-block"> <small>Showing: <strong> ${params.offset.toInteger() + 1 }-${limit > FAQInstanceTotal ? FAQInstanceTotal : limit }</strong>
            of <strong> ${FAQInstanceTotal }
        </strong>
    </small>
    </span>
    <div class="pagination">
    <g:paginate total="${FAQInstanceTotal}" />
    </div>

</body>
</html>