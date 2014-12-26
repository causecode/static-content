<%@ page contentType="text/html"%>
<html>
<head>
</head>
<body>
	<div>
		<p>
			Hello,
		</p>
		<p>
			You have one comment on blog <b>${blogInstance.title}</b>
			by
			${commentInstance.name}
		</p>
		<p>
			<i> ${commentInstance.commentText}</i>
		</p>
		<p>Thank you,</p>
		<p>CauseCode Team</p>
	</div>
</body>
</html>