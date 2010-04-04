(: XQuery script for loading resources from the database. Allows us to add docname attribute :)
(: $id is replaced by the code to look up the id we want. :)
for $doc in //*[@id="$id"]
let $elementName := node-name($doc/.)
let $docname := util:document-name($doc)
return
element {$elementName} {
	attribute id { $doc/@id },
	attribute docname { $docname },
	$doc/./*
}