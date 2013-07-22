 
<html lang="en">
<head>
  <meta charset="utf-8" />
  <title>jQuery UI Draggable - Constrain movement</title>
  <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
  <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
  <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
  <link rel="stylesheet" href="/resources/demos/style.css" />
  <style>
  .draggable { width: 90px; height: 90px; padding: 0.5em; float: left; margin: 0 10px 10px 0; }
  #draggable, #draggable2 { margin-bottom:20px; }
  #draggable { cursor: n-resize; }
  #draggable2 { cursor: e-resize; }
  #containment-wrapper { width: 95%; height:150px; border:2px solid #ccc; padding: 10px; }
  h3 { clear: left; }
  </style>
  <script>
  $(function() {
 
    $( "#draggable3" ).draggable({ containment: "#containment-wrapper", scroll: false });
    $( "#draggable5" ).draggable({ containment: "parent" });
  });
  </script>
</head>
<body>
 

 
<h3>Or to within another DOM element:</h3>
<div id="containment-wrapper">
  <div id="draggable3" class="draggable ui-widget-content">
    <p>I'm contained within the box</p>
  </div>
 
  <div class="draggable ui-widget-content">
    <p id="draggable5" class="ui-widget-header">I'm contained within my parent</p>
  </div>
</div>
 
 
</body>
</html>