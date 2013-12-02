modules = {
    menuItem {
        resource id: 'js', url: [dir: 'js/', file: "menu-item-sorting.js"], minify: true
        resource id: 'css', url: [dir: 'css/', file: "menu-item-sorting.css"], minify: true
        dependsOn 'draggableAndSortable'
    }

    inputWidget {
        resource id:'js', url:[dir:'js/', file:"inputWidget.js"], minify: true
    }

    draggableAndSortable {
        resource id: 'js', url: [dir: 'js/jquery-ui', file: 'jquery-ui-1.10.3.custom.js']
    }

    wordAndCharacterCounter {
        resource url: [dir: "js", file: "word-and-character-counter.js"]
    }

    editorswitch {
        resource url: '/js/editor.js'
    }
}