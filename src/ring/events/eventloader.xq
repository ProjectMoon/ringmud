for $doc in //ring[@codebehind != ""]
return
<doc name="{util:document-name($doc)}">
	<codebehind>
		{data($doc/@codebehind)}
	</codebehind>
</doc>