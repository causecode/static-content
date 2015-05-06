dataSource {
    //Configuring Mysql DBMS
    pooled = true
    driverClassName = "com.mysql.jdbc.Driver"
    username = "root"
    password = "causecode.11"
    dialect: org.hibernate.dialect.MySQL5InnoDBDialect
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
//  cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
}

environments {
    development {
        dataSource {
            dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
            url = "jdbc:mysql://localhost:3306/content"
            username = "root"
            password = "causecode.11"
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:mysql://localhost:3306/content"
        }
    }
}