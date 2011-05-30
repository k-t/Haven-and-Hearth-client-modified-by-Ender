package haven.scripting;

import java.lang.reflect.*;

import org.codehaus.groovy.ast.*;
import org.codehaus.groovy.ast.expr.*;
import org.codehaus.groovy.ast.stmt.*;
import org.codehaus.groovy.classgen.GeneratorContext;
import org.codehaus.groovy.control.*;
import org.codehaus.groovy.control.customizers.CompilationCustomizer;

/** Adds to every compiled class a set of static methods that wrap public methods of ScriptGlobal instance.
 * It allows to imitate global functions in the scripts. 
 */
public class CustomCustomizer extends CompilationCustomizer {

    public CustomCustomizer() {
        super(CompilePhase.CANONICALIZATION);
    }

    @Override
    public void call(SourceUnit unit, GeneratorContext ctx, ClassNode cn)
            throws CompilationFailedException {
        ClassNode gcn = ClassHelper.makeCached(ScriptGlobal.class); 
        // add wrappers for ScriptGlobal methods
        for (MethodNode m : gcn.getMethods()) {
            if (m.isPublic() && !m.isStatic()) {
                cn.addMethod(m.getName(), Modifier.PRIVATE | Modifier.STATIC,
                        m.getReturnType(),
                        m.getParameters(),
                        m.getExceptions(),
                        createScriptGlobalWrapperAst(m));
            }
        }
    }
    
    private Statement createScriptGlobalWrapperAst(MethodNode m) {
        return new ExpressionStatement(
                new MethodCallExpression(
                        createGetInstanceExpr(),
                        m.getName(),
                        new ArgumentListExpression(m.getParameters())));
    }
    
    private Expression createGetInstanceExpr() {
        return new StaticMethodCallExpression(
                ClassHelper.makeCached(ScriptGlobal.class),
                "getInstance",
                new ArgumentListExpression());
    }
}
