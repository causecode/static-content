# Upgrading from version 2.5.4 to 2.5.5
The field `value` in domain class `com.causecode.content.meta.Meta` has been renamed to `content`. Thus, to upgrade to the new version of 
static content plugin, you will have to write a migration and change the column name from `value` to `content`.

# ContentGrailsPlugin Usage

1. `getShorthandAnnotatedControllers()` a dynamic method added to the metaClass of the **ContentService**, which gets the list of all the
    controllers that have been assigned an annotated value for example: `PageController: c`, `BlogController: blog`
     
2. `sanitizeWithDashes(String text)` method of **FriendlyUrlService**, gets a meaningful title (separated with dashes) out of the random
    string parameter passed, this is useful in the creation of **SEO Friendly URL**.
