<html lang="en">
<head>
  <meta charset="utf-8" />
  <title>jQuery UI Draggable + Sortable</title>
  <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
  <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
  <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
  <link rel="stylesheet" href="/resources/demos/style.css" />
  <style>
  ul { margin-bottom: 10px; }
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

<ul id="sortable">
  <li class="ui-state-default">
    <div>
        <ul id="sortable">
            <li class="ui-state-default">Item11</li>
            <li class="ui-state-default">
                <div>
			        <ul id="sortable">
			            <li class="ui-state-default">Item111</li>
			            <li class="ui-state-default">Item121</li>
			            <li class="ui-state-default">Item131</li>
			            <li class="ui-state-default">Item141</li>
			        </ul>
			    </div>
            </li>
            <li class="ui-state-default">Item13</li>
            <li class="ui-state-default">Item14</li>
        </ul>
    </div>
  </li>
  <li class="ui-state-default">Item 2</li>
  <li class="ui-state-default">Item 3</li>
  <li class="ui-state-default">Item 4</li>
  <li class="ui-state-default">Item 5</li>
</ul>
 
 
</body>
</html>