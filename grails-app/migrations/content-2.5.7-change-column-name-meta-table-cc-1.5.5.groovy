databaseChangeLog = {

    changeSet(author: 'Hardik Modha', id: '28122016-01') {
        grailsChange {
            change {
                sql.execute("ALTER TABLE `cc_content_meta` CHANGE COLUMN `value` `content` VARCHAR(255) NOT NULL;");
            }
        }
    }
}