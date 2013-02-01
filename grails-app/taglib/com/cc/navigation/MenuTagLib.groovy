package com.cc.navigation

class MenuTagLib {
    static namespace = "com"
    def bootstrapMenu = { attrs, body ->
        def menuInstance = Menu.get(attrs.id)
        out << "<h1>"
        out << "${menuInstance.name}"
        out << "</h1>"
        out << '<div class="navbar navbar-static">'
        out << '<div class="navbar-inner">'
        out << '<div class="container" style="width: auto;">'
        out << "<ul class='nav'  role='navigation'>"
        menuInstance.menuItem.each {
                out << com.menu(id: it.id)
        }
        out << "</ul>"
        out << '</div>'
        out << '</div>'
        out << '</div>'
    }
    
    def menu = { attrs, body ->
        def menuItemInstance = MenuItem.get(attrs.id)
        if(menuItemInstance.childItems) {
            out << "<li class='dropdown'>"
            out << "<a  id='${menuItemInstance.id}' href='#' role='button' class='dropdown-toggle' data-toggle='dropdown'>"
            out << "${menuItemInstance.title}"
            out << "<b class='caret'></b>"
            out << "</a>"
            out << "<ul class='dropdown-menu' role='menu' aria-labelledby='${menuItemInstance.id}'>"
            menuItemInstance.childItems.each{
                    out << "<li>"
                    out << "<a href='${it.url}'>"
                    out << "${it.title}"
                    out << '</a>'
                    out << "</li>"
            }
            out << "</ul>"
            out << "</li>"
        }
        else {
            out << "<li>"
            out << "<a href='${menuItemInstance.url}'>"
            out << "${menuItemInstance.title}"
            out << '</a>'
            out << "</li>"
        }
    }
}
