<html lang="en">
<head>
  <meta charset="utf-8" />
  <title>jQuery UI Draggable + Sortable</title>
  <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
  <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
  <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
  <link rel="stylesheet" href="/resources/demos/style.css" />
  <style>
  ul { list-style-type: none; margin: 0; padding: 0; margin-bottom: 10px; }
  li { margin: 5px; padding: 5px; width: 150px; }
  </style>
  <script>
  $(function() {
    $( "#sortable" ).sortable({
      revert: true
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
  <li class="ui-state-default">
    <ul id="sortable" style="margin-left:40px">
        <li class="ui-state-default" >
            <ul id="sortable" style="margin-left:80px">
                <li class="ui-state-default" >Item 1</li>
                <li class="ui-state-default">
                    <ul><li></li></ul>
                </li>
                <li class="ui-state-default">Item 1</li>
                <li class="ui-state-default">Item 1</li>
            </ul>
         </li>
        <li class="ui-state-default">Item 1</li>
        <li class="ui-state-default">Item 1</li>
        <li class="ui-state-default">Item 1</li>
    </ul>
  </li>
  <li class="ui-state-default">Item 2<ul id="sortable" style="margin-left:40px"></ul></li>
  <li class="ui-state-default">Item 3</li>
  <li class="ui-state-default">Item 4</li>
  <li class="ui-state-default">Item 5</li>
</ul>
 
 
</body>
</html>