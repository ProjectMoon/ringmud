(: XQuery script for loading resources from the database. Allows us to add docname attribute :)
for $doc in //*[@id="loc1"]
let $elementName := node-name($doc/.)
let $docname := util:document-name($doc)
return
element {$elementName} {
	attribute docname { $docname },
	$doc/./*
}









