// $ANTLR 3.0.1 C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g 2008-02-10 12:20:44
package test;
import org.antlr.runtime.*;


import org.antlr.runtime.tree.*;

public class HtmlDocParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "DOC", "TITLE", "BODY", "TEXT", "'<html>'", "'</html>'", "'<title>'", "'</title>'", "'<body>'", "'</body>'"
    };
    public static final int DOC=4;
    public static final int BODY=6;
    public static final int TEXT=7;
    public static final int EOF=-1;
    public static final int TITLE=5;

        public HtmlDocParser(TokenStream input) {
            super(input);
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return tokenNames; }
    public String getGrammarFileName() { return "C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g"; }


    public static class html_doc_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start html_doc
    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:9:1: html_doc : '<html>' html_header html_body '</html>' -> ^( 'doc' html_header html_body ) ;
    public final html_doc_return html_doc() throws RecognitionException {
        html_doc_return retval = new html_doc_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal1=null;
        Token string_literal4=null;
        html_header_return html_header2 = null;

        html_body_return html_body3 = null;


        Object string_literal1_tree=null;
        Object string_literal4_tree=null;
        RewriteRuleTokenStream stream_9=new RewriteRuleTokenStream(adaptor,"token 9");
        RewriteRuleTokenStream stream_8=new RewriteRuleTokenStream(adaptor,"token 8");
        RewriteRuleSubtreeStream stream_html_body=new RewriteRuleSubtreeStream(adaptor,"rule html_body");
        RewriteRuleSubtreeStream stream_html_header=new RewriteRuleSubtreeStream(adaptor,"rule html_header");
        try {
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:10:2: ( '<html>' html_header html_body '</html>' -> ^( 'doc' html_header html_body ) )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:10:4: '<html>' html_header html_body '</html>'
            {
            string_literal1=(Token)input.LT(1);
            match(input,8,FOLLOW_8_in_html_doc42); 
            stream_8.add(string_literal1);

            pushFollow(FOLLOW_html_header_in_html_doc44);
            html_header2=html_header();
            _fsp--;

            stream_html_header.add(html_header2.getTree());
            pushFollow(FOLLOW_html_body_in_html_doc46);
            html_body3=html_body();
            _fsp--;

            stream_html_body.add(html_body3.getTree());
            string_literal4=(Token)input.LT(1);
            match(input,9,FOLLOW_9_in_html_doc48); 
            stream_9.add(string_literal4);


            // AST REWRITE
            // elements: html_body, html_header, DOC
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 10:45: -> ^( 'doc' html_header html_body )
            {
                // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:10:48: ^( 'doc' html_header html_body )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(adaptor.create(DOC, "DOC"), root_1);

                adaptor.addChild(root_1, stream_html_header.next());
                adaptor.addChild(root_1, stream_html_body.next());

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (Object)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end html_doc

    public static class html_header_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start html_header
    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:12:1: html_header : '<title>' TEXT '</title>' -> ^( 'title' TEXT ) ;
    public final html_header_return html_header() throws RecognitionException {
        html_header_return retval = new html_header_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal5=null;
        Token TEXT6=null;
        Token string_literal7=null;

        Object string_literal5_tree=null;
        Object TEXT6_tree=null;
        Object string_literal7_tree=null;
        RewriteRuleTokenStream stream_10=new RewriteRuleTokenStream(adaptor,"token 10");
        RewriteRuleTokenStream stream_TEXT=new RewriteRuleTokenStream(adaptor,"token TEXT");
        RewriteRuleTokenStream stream_11=new RewriteRuleTokenStream(adaptor,"token 11");

        try {
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:13:2: ( '<title>' TEXT '</title>' -> ^( 'title' TEXT ) )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:13:4: '<title>' TEXT '</title>'
            {
            string_literal5=(Token)input.LT(1);
            match(input,10,FOLLOW_10_in_html_header67); 
            stream_10.add(string_literal5);

            TEXT6=(Token)input.LT(1);
            match(input,TEXT,FOLLOW_TEXT_in_html_header69); 
            stream_TEXT.add(TEXT6);

            string_literal7=(Token)input.LT(1);
            match(input,11,FOLLOW_11_in_html_header71); 
            stream_11.add(string_literal7);


            // AST REWRITE
            // elements: TITLE, TEXT
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 13:30: -> ^( 'title' TEXT )
            {
                // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:13:33: ^( 'title' TEXT )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(adaptor.create(TITLE, "TITLE"), root_1);

                adaptor.addChild(root_1, stream_TEXT.next());

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (Object)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end html_header

    public static class html_body_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start html_body
    // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:15:1: html_body : '<body>' TEXT '</body>' -> ^( 'body' TEXT ) ;
    public final html_body_return html_body() throws RecognitionException {
        html_body_return retval = new html_body_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token string_literal8=null;
        Token TEXT9=null;
        Token string_literal10=null;

        Object string_literal8_tree=null;
        Object TEXT9_tree=null;
        Object string_literal10_tree=null;
        RewriteRuleTokenStream stream_TEXT=new RewriteRuleTokenStream(adaptor,"token TEXT");
        RewriteRuleTokenStream stream_13=new RewriteRuleTokenStream(adaptor,"token 13");
        RewriteRuleTokenStream stream_12=new RewriteRuleTokenStream(adaptor,"token 12");

        try {
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:16:2: ( '<body>' TEXT '</body>' -> ^( 'body' TEXT ) )
            // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:16:4: '<body>' TEXT '</body>'
            {
            string_literal8=(Token)input.LT(1);
            match(input,12,FOLLOW_12_in_html_body89); 
            stream_12.add(string_literal8);

            TEXT9=(Token)input.LT(1);
            match(input,TEXT,FOLLOW_TEXT_in_html_body91); 
            stream_TEXT.add(TEXT9);

            string_literal10=(Token)input.LT(1);
            match(input,13,FOLLOW_13_in_html_body93); 
            stream_13.add(string_literal10);


            // AST REWRITE
            // elements: TEXT, BODY
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"token retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 16:28: -> ^( 'body' TEXT )
            {
                // C:\\Documents and Settings\\James Boe\\Desktop\\DMS\\src\\com\\easyinsight\\conditions\\HtmlDoc.g:16:31: ^( 'body' TEXT )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(adaptor.create(BODY, "BODY"), root_1);

                adaptor.addChild(root_1, stream_TEXT.next());

                adaptor.addChild(root_0, root_1);
                }

            }



            }

            retval.stop = input.LT(-1);

                retval.tree = (Object)adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end html_body


 

    public static final BitSet FOLLOW_8_in_html_doc42 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_html_header_in_html_doc44 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_html_body_in_html_doc46 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_9_in_html_doc48 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_10_in_html_header67 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_TEXT_in_html_header69 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_html_header71 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_html_body89 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_TEXT_in_html_body91 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_html_body93 = new BitSet(new long[]{0x0000000000000002L});

}