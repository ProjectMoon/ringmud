(: XQuery script to load all locations with docname attributes :)
for $doc in //location
let $elementName := node-name($doc/.)
let $docname := util:document-name($doc)
return
element {$elementName} {
	attribute id { $doc/@id },
	attribute docname { $docname },
	$doc/./*
}