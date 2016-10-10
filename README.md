# Upgrading from previous version "static-content" to new version
Renaming `com.causecode.content.meta.Meta` domain field `value` to `content` need to write DB migration for fields `value` to `content`

# ContentGrailsPlugin Usage

1. `getShorthandAnnotatedControllers()` a dynamic method added to the metaClass of the **ContentService**, which gets the list of all the
    controllers that have been assigned an annotated value for example: `PageController: c`, `BlogController: blog`
     
2. `sanitizeWithDashes(String text)` method of **FriendlyUrlService**, gets a meaningful title (separated with dashes) out of the random
    string parameter passed, this is useful in the creation of **SEO Friendly URL**.
