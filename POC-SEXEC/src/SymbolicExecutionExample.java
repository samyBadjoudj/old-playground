import com.samy.symbolicexecution.scanners.SimpleExecutionScanner;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;

import javax.tools.*;
import java.io.IOException;

/**
 * User: Samy Badjoudj
 */
public class SymbolicExecutionExample {

    public static void main(String[] args) throws IOException {

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnosticsCollector = new DiagnosticCollector<JavaFileObject>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnosticsCollector, null, null);
        Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjects("/home/bibi/POC-Symbolic Execution Example/src/Computer.java");
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnosticsCollector, null, null, fileObjects);
        JavacTask javacTask = (JavacTask) task;
        Iterable<? extends CompilationUnitTree> parseResult = javacTask.parse();

        for (CompilationUnitTree compilationUnitTree : parseResult) {
            compilationUnitTree.accept(new SimpleExecutionScanner(), null);
        }

    }
}
