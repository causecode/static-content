<br>
    <div class="form-inline">
        <label for="childTitle">Child Title<span class="required-indicator">*</span></label>
        <g:textField class="required" name="childTitle" value="${childItem?.title}"/>
        <label for="childUrl">Child Url<span class="required-indicator">*</span></label>
        <g:textField class="required url" name="childUrl" value="${childItem?.url}"/>
    </div>
<br>