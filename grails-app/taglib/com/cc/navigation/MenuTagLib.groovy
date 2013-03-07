/* Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are not permitted.*/


package com.cc.navigation

class MenuTagLib {
    static namespace = "com"
    def bootstrapMenu = { attrs, body ->
        if(Menu.get(attrs.id)) {
            def menuInstance = Menu.get(attrs.id)
            out << '<div class="navbar navbar-fixed-top">'
            out << '<div class="navbar-inner">'
            out << '<div class="container" style="width: auto;">'
            out << '<a style="margin-left : 200px;" class="brand" href="#"><img class="logo" src="'+resource(dir: "images", file: "logo.png")+'" /> <span class="name">'+menuInstance.name+'</span></a>'
            out << "<ul class='nav'  role='navigation'>"
            def menuItem = MenuItem.createCriteria()
            def menuItemList = menuItem.list {
                eq("menu", menuInstance)
            }
            menuItemList.each {
                    out << com.menu(id: it.id)
            }
            out << "</ul>"
            out << '</div>'
            out << '</div>'
            out << '</div>'
        }
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
