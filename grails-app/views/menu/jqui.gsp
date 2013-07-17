<!doctype html>
 
<html lang="en">
<head>
  <meta charset="utf-8" />
  <title>jQuery UI Draggable + Sortable</title>
  <link rel="stylesheet" href="/css/jquery-ui/jquery-ui-1.10.3.custom.css" />
  <script src="/js/jquery-ui/jquery-1.9.1.js"></script>
  <script src="/js/jquery-ui/jquery-ui-1.10.3.custom.js"></script>
  <style>
  ul { list-style-type: none; margin: 0; padding: 0; margin-bottom: 10px; }
  li { margin: 5px; padding: 5px; width: 150px; }
  </style>
  <script>
  $(function() {
    $( "#sortable" ).sortable({
      revert: true
    });
    $( "#draggable" ).draggable({
      connectToSortable: "#sortable",
      helper: "clone",
      revert: "invalid"
    });
    $( "ul, li" ).disableSelection();
  });
  </script>
</head>
<body>
 
<ul>
  <li id="draggable" class="ui-state-highlight">Drag me down</li>
</ul>
 
<ul id="sortable">
  <li class="ui-state-default">Item 1</li>
  <li class="ui-state-default">Item 2</li>
  <li class="ui-state-default">Item 3</li>
  <li class="ui-state-default">Item 4</li>
  <li class="ui-state-default">Item 5</li>
</ul>
 
 
</body>
</html>