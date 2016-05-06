<!--  Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are not permitted. -->

<%@ page import="com.causecode.content.PageLayout"%>

<div class="form-group ${hasErrors(bean: pageLayoutInstance, field: 'layoutName', 'error')} ">
    <label class="control-label col-sm-2" for="layoutName"> <g:message code="pageLayout.layoutName.label"
            default="Layout Name" />
    </label>
    <div class="col-sm-5">
        <g:textField name="layoutName" value="${pageLayoutInstance?.layoutName}" class="form-control" />
    </div>
</div>

