import expression.Parser;
import lexer.Lexer;

public class Main {


    public static void main(String[] args) throws Exception {

        String file = "{\n" +
                            "float[10] arr ;\n" +
                            "float w ; " +
                            "float v;" +
                            "int j ; " +
                            "int i; " +
                            "float x ; " +
                            "w=100;  " +
                            "while ( true ) {        \n" +
                            "   do i=i+1 ;" +
                            "   while ( arr[i] < v) ;  \n" +
                            "                         " +
                            "   do j=j-1 ; " +
                            "   while ( arr[j] > v) ;  \n" +
                            "                          " +
                            "   if ( i == j)continue;\n" +
                            "   if ( i > j)break;      \n" +
                            "   x =arr[i] ; arr[i] = arr[j] ; arr[j] = x ;\n" +
                            "}\n" +
                            "if(i>0){continue;};\n" +
                      "}\n";
        Parser parser = new Parser(new Lexer(file));
        parser.program();

    }
}
