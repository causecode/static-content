modules = {
    menuItem {
        resource id:'js', url:[dir:'js/', file:"editMenuItemOrder.js"], disposition:'defer', minify: true
        resource id:'css', url:[dir:'css/', file:"menuItem.css"], disposition:'defer', minify: true
        dependsOn 'draggableAndSortable'
    }
    
    inputWidget {
        resource id:'js', url:[dir:'js/', file:"inputWidget.js"], disposition:'defer', minify: true
        resource id:'css', url:[dir:'css/', file:"inputWidget.css"], disposition:'defer', minify: true
    }
    draggableAndSortable {
        resource id: 'js', url: [dir: 'js/jquery-ui', file: 'jquery-ui-1.10.3.custom.js']
    }
}