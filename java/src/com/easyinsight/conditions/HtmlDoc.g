grammar HtmlDoc;
options { output=AST; }
tokens {
	DOC='doc';
	TITLE='title';
	BODY='body';
}

html_doc
	: '<html>' html_header html_body '</html>' -> ^('doc' html_header html_body);

html_header
	: '<title>' TEXT '</title>' -> ^('title' TEXT) ;

html_body
	: '<body>' TEXT '</body>' -> ^('body' TEXT)	;

TEXT : (~('<'))*;
