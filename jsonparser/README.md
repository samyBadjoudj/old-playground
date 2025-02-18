jsonparser
==========

Basic JSON parser little exercise, lexer, token, visitor design pattern, first and follow sets, grammar,

Writing a parser:

----------------------------------------------------


When we see how a compiler/interpreter is written, we can notice different parts.

Front-end is composed by:

Lexer  : breaks your code in tokens (primitive part of your language) using syntax diagram of the grammar
Parser : Use the tokens and the grammar of your language to get the parse tree 
Type Checker -> Does the semantic analysis 

Intermediate layer:

Symbol table : Data structure (Stack of Hashtable for example) that binds an identifier 
to an information of the code (variables, scopes, functions...)

Back-end:

  Analysis : data-flow, dependance, alias analysis, call graph, control flow graph ...
  Optimization: inline expansion
  Code generation.

http://en.wikipedia.org/wiki/Compiler


----------------------------------------------------

To begin we have to understand the language we want to parse, so we introduce the notion of grammar. 

They are the rule of the language. So with this grammar we can write down in this new language.

Usually in computer science for a new language we use Context Free Language that means 
it has been written with a Context Free Grammar (CFG).

A CFG can be written : X->z

X is a non terminal symbol (produces symbol) and z could be a terminal (final value, 
for ex:1,'a',234) or non terminal symbal. We say it's a context free grammar when X can always be replaced.


---------------------------------------------------


So to write a parser we can do: 

Etablished a parsing table (check if there is no ambiguity in the grammar) 
then write a predictive parser that use this parsing table.

To establish this this parsing table first we need to get FIRST and FOLLOW sets of the grammar
(https://www.cs.uaf.edu/~cs331/notes/FirstFollow.pdf).

Then when we have checked that there is no ambiguity.
(http://en.wikibooks.org/wiki/Introduction_to_Programming_Languages/Ambiguity) we can write a predictive parser.
http://en.wikipedia.org/wiki/LL_parser

Or we can write it without using a parsing table as a structure in the code, 
we can use  the grammar and first and follow sets.


Here are the grammar on our case the JSON one ( I have put FIRSTS FOLLOWS sets :
<pre>
object
    {}
    { members } 
members
    pair
    pair , members
pair
    string : value
array
    []
    [ elements ]
elements
    value
    value , elements
value
    string | number | object | array | true | false | null 
    



         FIRST                                                                                           

Object     '{'                                                                                           
Members    '"'                                                                                           
Pair       '"'                                                                                           
Array      '['                                                                                           
Elements   '"' , number , '{' , '[' , 't|T' , 'f|F' , 'n|N'                                              
Values     '"' , number , '{' , '[' , 't|T' , 'f|F' , 'n|N'                                              


         FOLLOWS                                                                                         

Object    '}','"'                                                                                        
Members   '}'                                                                                            
Pair      '}','                                                                                          
Array     ']' , '"' , number , '{' , '[' , 't|T' , 'f|F' , 'n|N'        
Elements  ']'                                                                                            
Values    ',' ']'                                                                                        

</pre>


I let you dive into the code :


We have 3 elements :

We have the Lexer wich uses the SourceCode (the SourceCode class parses char by char the code) to split our code 
into tokens.

According to the grammar and the First and Flollow set we call the correct parser.

We pass a Visitor (to avoid to scatter the code), to print the what has been parsed.



TODO : 

- Be able to parse true, false, null, withour double quotes "
- Create a proper tree or other data structure with the data parsed
- Throw execpetion when is not well formatted (using FIRST and FOLLOW sets)







Output : 
<pre>
HumHun says:.ObjectParser            {
HumHun says:.PairParser            web-app
HumHun says:.PairParser            :
HumHun says:.ObjectParser            {
HumHun says:.PairParser            servlet
HumHun says:.PairParser            :
HumHun says:.ArrayParser            [
HumHun says:.ObjectParser            {
HumHun says:.PairParser            servlet-name
HumHun says:.PairParser            :
HumHun says:.ValueParser            cofaxCDS
HumHun says:.PairParser            ,
HumHun says:.PairParser            servlet-class
HumHun says:.PairParser            :
HumHun says:.ValueParser            org.cofax.cds.CDSServlet
HumHun says:.PairParser            ,
HumHun says:.PairParser            init-param
HumHun says:.PairParser            :
HumHun says:.ObjectParser            {
HumHun says:.PairParser            configGlossary:installationAt
HumHun says:.PairParser            :
HumHun says:.ValueParser            Philadelphia, PA
HumHun says:.PairParser            ,
HumHun says:.PairParser            configGlossary:adminEmail
HumHun says:.PairParser            :
HumHun says:.ValueParser            ksm@pobox.com
HumHun says:.PairParser            ,
HumHun says:.PairParser            configGlossary:poweredBy
HumHun says:.PairParser            :
HumHun says:.ValueParser            Cofax
HumHun says:.PairParser            ,
HumHun says:.PairParser            configGlossary:poweredByIcon
HumHun says:.PairParser            :
HumHun says:.ValueParser            /images/cofax.gif
HumHun says:.PairParser            ,
HumHun says:.PairParser            configGlossary:staticPath
HumHun says:.PairParser            :
HumHun says:.ValueParser            /content/static
HumHun says:.PairParser            ,
HumHun says:.PairParser            templateProcessorClass
HumHun says:.PairParser            :
HumHun says:.ValueParser            org.cofax.WysiwygTemplate
HumHun says:.PairParser            ,
HumHun says:.PairParser            templateLoaderClass
HumHun says:.PairParser            :
HumHun says:.ValueParser            org.cofax.FilesTemplateLoader
HumHun says:.PairParser            ,
HumHun says:.PairParser            templatePath
HumHun says:.PairParser            :
HumHun says:.ValueParser            templates
HumHun says:.PairParser            ,
HumHun says:.PairParser            templateOverridePath
HumHun says:.PairParser            :
HumHun says:.ValueParser            
HumHun says:.PairParser            ,
HumHun says:.PairParser            defaultListTemplate
HumHun says:.PairParser            :
HumHun says:.ValueParser            listTemplate.htm
HumHun says:.PairParser            ,
HumHun says:.PairParser            defaultFileTemplate
HumHun says:.PairParser            :
HumHun says:.ValueParser            articleTemplate.htm
HumHun says:.PairParser            ,
HumHun says:.PairParser            useJSP
HumHun says:.PairParser            :
HumHun says:.ValueParser            false
HumHun says:.PairParser            ,
HumHun says:.PairParser            jspListTemplate
HumHun says:.PairParser            :
HumHun says:.ValueParser            listTemplate.jsp
HumHun says:.PairParser            ,
HumHun says:.PairParser            jspFileTemplate
HumHun says:.PairParser            :
HumHun says:.ValueParser            articleTemplate.jsp
HumHun says:.PairParser            ,
HumHun says:.PairParser            cachePackageTagsTrack
HumHun says:.PairParser            :
HumHun says:.ValueParser            200
HumHun says:.PairParser            ,
HumHun says:.PairParser            cachePackageTagsStore
HumHun says:.PairParser            :
HumHun says:.ValueParser            200
HumHun says:.PairParser            ,
HumHun says:.PairParser            cachePackageTagsRefresh
HumHun says:.PairParser            :
HumHun says:.ValueParser            60
HumHun says:.PairParser            ,
HumHun says:.PairParser            cacheTemplatesTrack
HumHun says:.PairParser            :
HumHun says:.ValueParser            100
HumHun says:.PairParser            ,
HumHun says:.PairParser            cacheTemplatesStore
HumHun says:.PairParser            :
HumHun says:.ValueParser            50
HumHun says:.PairParser            ,
HumHun says:.PairParser            cacheTemplatesRefresh
HumHun says:.PairParser            :
HumHun says:.ValueParser            15
HumHun says:.PairParser            ,
HumHun says:.PairParser            cachePagesTrack
HumHun says:.PairParser            :
HumHun says:.ValueParser            200
HumHun says:.PairParser            ,
HumHun says:.PairParser            cachePagesStore
HumHun says:.PairParser            :
HumHun says:.ValueParser            100
HumHun says:.PairParser            ,
HumHun says:.PairParser            cachePagesRefresh
HumHun says:.PairParser            :
HumHun says:.ValueParser            10
HumHun says:.PairParser            ,
HumHun says:.PairParser            cachePagesDirtyRead
HumHun says:.PairParser            :
HumHun says:.ValueParser            10
HumHun says:.PairParser            ,
HumHun says:.PairParser            searchEngineListTemplate
HumHun says:.PairParser            :
HumHun says:.ValueParser            forSearchEnginesList.htm
HumHun says:.PairParser            ,
HumHun says:.PairParser            searchEngineFileTemplate
HumHun says:.PairParser            :
HumHun says:.ValueParser            forSearchEngines.htm
HumHun says:.PairParser            ,
HumHun says:.PairParser            searchEngineRobotsDb
HumHun says:.PairParser            :
HumHun says:.ValueParser            WEB-INF/robots.db
HumHun says:.PairParser            ,
HumHun says:.PairParser            useDataStore
HumHun says:.PairParser            :
HumHun says:.ValueParser            true
HumHun says:.PairParser            ,
HumHun says:.PairParser            dataStoreClass
HumHun says:.PairParser            :
HumHun says:.ValueParser            org.cofax.SqlDataStore
HumHun says:.PairParser            ,
HumHun says:.PairParser            redirectionClass
HumHun says:.PairParser            :
HumHun says:.ValueParser            org.cofax.SqlRedirection
HumHun says:.PairParser            ,
HumHun says:.PairParser            dataStoreName
HumHun says:.PairParser            :
HumHun says:.ValueParser            cofax
HumHun says:.PairParser            ,
HumHun says:.PairParser            dataStoreDriver
HumHun says:.PairParser            :
HumHun says:.ValueParser            com.microsoft.jdbc.sqlserver.SQLServerDriver
HumHun says:.PairParser            ,
HumHun says:.PairParser            dataStoreUrl
HumHun says:.PairParser            :
HumHun says:.ValueParser            jdbc:microsoft:sqlserver://LOCALHOST:1433;DatabaseName=goon
HumHun says:.PairParser            ,
HumHun says:.PairParser            dataStoreUser
HumHun says:.PairParser            :
HumHun says:.ValueParser            sa
HumHun says:.PairParser            ,
HumHun says:.PairParser            dataStorePassword
HumHun says:.PairParser            :
HumHun says:.ValueParser            dataStoreTestQuery
HumHun says:.PairParser            ,
HumHun says:.PairParser            dataStoreTestQuery
HumHun says:.PairParser            :
HumHun says:.ValueParser            SET NOCOUNT ON;select test='test';
HumHun says:.PairParser            ,
HumHun says:.PairParser            dataStoreLogFile
HumHun says:.PairParser            :
HumHun says:.ValueParser            /usr/local/tomcat/logs/datastore.log
HumHun says:.PairParser            ,
HumHun says:.PairParser            dataStoreInitConns
HumHun says:.PairParser            :
HumHun says:.ValueParser            10
HumHun says:.PairParser            ,
HumHun says:.PairParser            dataStoreMaxConns
HumHun says:.PairParser            :
HumHun says:.ValueParser            100
HumHun says:.PairParser            ,
HumHun says:.PairParser            dataStoreConnUsageLimit
HumHun says:.PairParser            :
HumHun says:.ValueParser            100
HumHun says:.PairParser            ,
HumHun says:.PairParser            dataStoreLogLevel
HumHun says:.PairParser            :
HumHun says:.ValueParser            debug
HumHun says:.PairParser            ,
HumHun says:.PairParser            maxUrlLength
HumHun says:.PairParser            :
HumHun says:.ValueParser            500
HumHun says:.ObjectParser            }
HumHun says:.ObjectParser            }
HumHun says:.ElementParser            ,
HumHun says:.ObjectParser            {
HumHun says:.PairParser            servlet-name
HumHun says:.PairParser            :
HumHun says:.ValueParser            cofaxEmail
HumHun says:.PairParser            ,
HumHun says:.PairParser            servlet-class
HumHun says:.PairParser            :
HumHun says:.ValueParser            org.cofax.cds.EmailServlet
HumHun says:.PairParser            ,
HumHun says:.PairParser            init-param
HumHun says:.PairParser            :
HumHun says:.ObjectParser            {
HumHun says:.PairParser            mailHost
HumHun says:.PairParser            :
HumHun says:.ValueParser            mail1
HumHun says:.PairParser            ,
HumHun says:.PairParser            mailHostOverride
HumHun says:.PairParser            :
HumHun says:.ValueParser            mail2
HumHun says:.ObjectParser            }
HumHun says:.ObjectParser            }
HumHun says:.ElementParser            ,
HumHun says:.ObjectParser            {
HumHun says:.PairParser            servlet-name
HumHun says:.PairParser            :
HumHun says:.ValueParser            cofaxAdmin
HumHun says:.PairParser            ,
HumHun says:.PairParser            servlet-class
HumHun says:.PairParser            :
HumHun says:.ValueParser            org.cofax.cds.AdminServlet
HumHun says:.ObjectParser            }
HumHun says:.ElementParser            ,
HumHun says:.ObjectParser            {
HumHun says:.PairParser            servlet-name
HumHun says:.PairParser            :
HumHun says:.ValueParser            fileServlet
HumHun says:.PairParser            ,
HumHun says:.PairParser            servlet-class
HumHun says:.PairParser            :
HumHun says:.ValueParser            org.cofax.cds.FileServlet
HumHun says:.ObjectParser            }
HumHun says:.ElementParser            ,
HumHun says:.ObjectParser            {
HumHun says:.PairParser            servlet-name
HumHun says:.PairParser            :
HumHun says:.ValueParser            cofaxTools
HumHun says:.PairParser            ,
HumHun says:.PairParser            servlet-class
HumHun says:.PairParser            :
HumHun says:.ValueParser            org.cofax.cms.CofaxToolsServlet
HumHun says:.PairParser            ,
HumHun says:.PairParser            init-param
HumHun says:.PairParser            :
HumHun says:.ObjectParser            {
HumHun says:.PairParser            templatePath
HumHun says:.PairParser            :
HumHun says:.ValueParser            toolstemplates/
HumHun says:.PairParser            ,
HumHun says:.PairParser            log
HumHun says:.PairParser            :
HumHun says:.ValueParser            1
HumHun says:.PairParser            ,
HumHun says:.PairParser            logLocation
HumHun says:.PairParser            :
HumHun says:.ValueParser            /usr/local/tomcat/logs/CofaxTools.log
HumHun says:.PairParser            ,
HumHun says:.PairParser            logMaxSize
HumHun says:.PairParser            :
HumHun says:.ValueParser            
HumHun says:.PairParser            ,
HumHun says:.PairParser            dataLog
HumHun says:.PairParser            :
HumHun says:.ValueParser            1
HumHun says:.PairParser            ,
HumHun says:.PairParser            dataLogLocation
HumHun says:.PairParser            :
HumHun says:.ValueParser            /usr/local/tomcat/logs/dataLog.log
HumHun says:.PairParser            ,
HumHun says:.PairParser            dataLogMaxSize
HumHun says:.PairParser            :
HumHun says:.ValueParser            
HumHun says:.PairParser            ,
HumHun says:.PairParser            removePageCache
HumHun says:.PairParser            :
HumHun says:.ValueParser            /content/admin/remove?cache=pages&id=
HumHun says:.PairParser            ,
HumHun says:.PairParser            removeTemplateCache
HumHun says:.PairParser            :
HumHun says:.ValueParser            /content/admin/remove?cache=templates&id=
HumHun says:.PairParser            ,
HumHun says:.PairParser            fileTransferFolder
HumHun says:.PairParser            :
HumHun says:.ValueParser            /usr/local/tomcat/webapps/content/fileTransferFolder
HumHun says:.PairParser            ,
HumHun says:.PairParser            lookInContext
HumHun says:.PairParser            :
HumHun says:.ValueParser            1
HumHun says:.PairParser            ,
HumHun says:.PairParser            adminGroupID
HumHun says:.PairParser            :
HumHun says:.ValueParser            4
HumHun says:.PairParser            ,
HumHun says:.PairParser            betaServer
HumHun says:.PairParser            :
HumHun says:.ValueParser            true
HumHun says:.ObjectParser            }
HumHun says:.ObjectParser            }
HumHun says:.ArrayParser            ]
HumHun says:.PairParser            ,
HumHun says:.PairParser            servlet-mapping
HumHun says:.PairParser            :
HumHun says:.ObjectParser            {
HumHun says:.PairParser            cofaxCDS
HumHun says:.PairParser            :
HumHun says:.ValueParser            /
HumHun says:.PairParser            ,
HumHun says:.PairParser            cofaxEmail
HumHun says:.PairParser            :
HumHun says:.ValueParser            /cofaxutil/aemail/*
HumHun says:.PairParser            ,
HumHun says:.PairParser            cofaxAdmin
HumHun says:.PairParser            :
HumHun says:.ValueParser            /admin/*
HumHun says:.PairParser            ,
HumHun says:.PairParser            fileServlet
HumHun says:.PairParser            :
HumHun says:.ValueParser            /static/*
HumHun says:.PairParser            ,
HumHun says:.PairParser            cofaxTools
HumHun says:.PairParser            :
HumHun says:.ValueParser            /tools/*
HumHun says:.ObjectParser            }
HumHun says:.PairParser            ,
HumHun says:.PairParser            taglib
HumHun says:.PairParser            :
HumHun says:.ObjectParser            {
HumHun says:.PairParser            taglib-uri
HumHun says:.PairParser            :
HumHun says:.ValueParser            cofax.tld
HumHun says:.PairParser            ,
HumHun says:.PairParser            taglib-location
HumHun says:.PairParser            :
HumHun says:.ValueParser            /WEB-INF/tlds/cofax.tld
HumHun says:.ObjectParser            }
HumHun says:.ObjectParser            }
HumHun says:.ObjectParser            }
</pre>
