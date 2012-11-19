package com.cc.content

class Content {
	Date dateCreated
	Date lastUpdated
	String title
	String subTitle
	String body
	String author
	
	static mapping = {
		body type: 'text'
	}
	
    static constraints = {
		body size: 0..2000, blank: false
		title nullable: false, blank: false
		subTitle nullable: false, blank: false
		author nullable: true
    }
}
