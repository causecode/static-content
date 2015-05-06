/* global controllers*/

'use strict';

controllers.controller('PageController', ['$scope', 'PageModel','PageLayoutModel', 'TextFormatModel' ,
        function($scope, PageModel, PageLayoutModel, TextFormatModel) {

    if (($scope.controllerName === 'page') && ($scope.actionName === 'show')) {
        $scope.pageInstance = PageModel.get({id: $scope.id});
        $scope.$watch('id', function(newId, oldId) {
            if (newId && oldId && newId !== oldId) {
                $scope.pageInstance = PageModel.get({id: $scope.id});
            }
        });
    }

    //Setting up default options for TinyMce editor
    $scope.tinymceOptions = {
    	plugins: [
                "advlist autolink autosave link image lists charmap print preview hr anchor pagebreak spellchecker",
                "searchreplace wordcount visualblocks visualchars code fullscreen insertdatetime media nonbreaking",
                "table contextmenu directionality emoticons template textcolor paste fullpage textcolor"
        ],

        toolbar1: "newdocument fullpage | bold italic underline strikethrough | alignleft aligncenter alignright alignjustify | styleselect 	formatselect fontselect fontsizeselect",
        toolbar2: "cut copy paste | searchreplace | bullist numlist | outdent indent blockquote | undo redo | link unlink anchor image media code | insertdatetime preview | forecolor backcolor",
        toolbar3: "table | hr removeformat | subscript superscript | charmap emoticons | print fullscreen | ltr rtl | spellchecker | visualchars visualblocks nonbreaking template pagebreak restoredraft",

        menubar: false,
        toolbar_items_size: 'small',

        style_formats: [
                {title: 'Bold text', inline: 'b'},
                {title: 'Red text', inline: 'span', styles: {color: '#ff0000'}},
                {title: 'Red header', block: 'h1', styles: {color: '#ff0000'}},
                {title: 'Example 1', inline: 'span', classes: 'example1'},
                {title: 'Example 2', inline: 'span', classes: 'example2'},
                {title: 'Table styles'},
                {title: 'Table row 1', selector: 'tr', classes: 'tablerow1'}
        ],

        templates: [
                {title: 'Test template 1', content: 'Test 1'},
                {title: 'Test template 2', content: 'Test 2'}
        ]
    };

    if ($scope.actionName === 'create') {
        $scope.contentInstance = new PageModel();
        $scope.contentInstance.metaList = [];
        $scope.contentInstance.textFormat = {};
        // Check if Editor is true or false for the Current User
        TextFormatModel.getEditorType(null, function(data) {
            $scope.hasTinyMce = data.editor;
            $scope.formatsAvailable = data.formatsAvailable;
            $scope.switchEditor = {
            tinyMce: $scope.hasTinyMce
        };
        });
    } else if (['edit'].indexOf($scope.actionName) > -1) {
        PageModel.get({id: $scope.id}, function(pageData) {
            $scope.contentInstance = pageData;
            $scope.contentInstance.metaList = [];
            console.log("page data",pageData);
            if (pageData.pageLayout) {
                PageLayoutModel.get({id: pageData.pageLayout.id}, function(pageLayoutInstance) {
                    $scope.contentInstance.pageLayout = pageLayoutInstance.id;
                });
            }
            //If a text-formatter is present for the current User
            if(pageData.textFormat) {
               TextFormatModel.get({id: pageData.textFormat.id}, function(textFormatInstance) {
                  $scope.contentInstance.textFormat.id = textFormatInstance.id;
                  TextFormatModel.getEditorType(null,function(data){        //Since we aren't passing any 'params' so 1st argument is null
                      $scope.hasTinyMce = data.editor;
                      $scope.formatsAvailable = data.formatsAvailable;
                      $scope.switchEditor = {
                          tinyMce: $scope.hasTinyMce
                      };
                  });
               })
            }
        });
    }

    PageModel.getMetaList(null, function(data){
        $scope.metaList = data.metaTypeList;
    },function() {});

    PageLayoutModel.getPageLayoutList(null, function(data){
        $scope.pageLayoutList = data.pageLayoutList;
        $scope.defaultLayoutName =  $scope.pageLayoutList[0].layoutName;
    },function() {});

    $scope.addForm = function() {
        $scope.contentInstance.metaList.push({});
    };
}]);
