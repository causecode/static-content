package com.cc.navigation

class Menu {
    String name
    static hasMany = [menuItem: MenuItem]

    static constraints = {
        name blank: false
    }
}
