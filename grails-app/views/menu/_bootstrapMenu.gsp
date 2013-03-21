<div class="navbar navbar-fixed-top">
	<div class="navbar-inner">
		<div class="container" style="width: 940px;" >
		<a class="brand" href="#">
			<img class="logo" src="${resource(dir: "images", file: "logo.png")}" />
		</a>
		<ul class="nav pull-right">
			<g:each in="${menuItemList}">
			    <com:menu id="${it.id}"></com:menu>
			</g:each>
		</ul>
		</div>
	</div>
</div>