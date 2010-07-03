for $entry in document("did.xml")//*[@uuid=$uuid]
return update delete $entry