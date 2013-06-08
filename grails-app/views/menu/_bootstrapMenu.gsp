<div class="navbar navbar-fixed-top">
	<div class="navbar-inner">
	
<!-- 	<div class="container" style="width: 940px;" >		-->
	
		<div class="container" >							<!-- inline style removed -->
		<a class="brand" href="#">
			<img class="logo" src="/images/logo.png" />
		</a>
		<ul class="nav pull-right">
			<g:each in="${menuItemList}">
			    <com:menu id="${it.id}"></com:menu>
			</g:each>
		</ul>
		</div>
	</div>
</div>