package com.cc.navigation

class MenuItem {
    String title
    String url
    static belongsTo = [parent: MenuItem]
    static hasMany = [childItems: MenuItem]
    
    static constraints = {
        url url: true
        parent nullable: true
    }
}
