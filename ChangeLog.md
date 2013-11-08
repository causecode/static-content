# Content Plugin

A plugin used to manage contents like static pages, menus etc. at one place.

## ChnageLog

### Version 2.2

#### Breaking change

1. Page domain package refactored to com.cc.content.page

#### Database change

1. Added new domain for content revision,
2. Removed version from join class,
3. Modify value for column **class** in **cc_content_content** to replace **com.cc.page.Page** to **com.cc.content.page.Page**.

#### New Features

1. Added feature to support content revision for Page.

### Version 1.9.3 to 1.9.9, 2.0, 2.1

1. Small fixes & helper methods for input widget, blog etc, merged commit containg fix for out of bound request exception.
2. Added some more meta properties. (From 1.9.7),
3. Fixed creating & sorting menu item.

### Version 1.9.2

#### Breaking change

1. Added new config for role ```cc.plugins.content.RoleForMenuItem = ["ROLE_ADMIN","ROLE_CONTENT_MANAGER","ROLE_EMPLOYEE","ROLE_USER"]```

#### Database change

1. *roles* field in menu_item made nullable.

#### Improvement

1. Added simple-captcha validation on adding a comment.
2. Comments now can be deleted by role content manager.

### Version 1.9.1

#### Breaking change

1. Changed package name of *Menu, MenuItem, Blog, Comment, BlogComment*.

#### Database changes

1. Added new InputWidget domain,
2. Change in structure of menu & menu item.

#### Improvements

1. Bootstrap upgraded to 3.0,
2. Blog/comment UI & code restructured,
3. Added meta tag property in blog show page.