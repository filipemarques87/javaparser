package com.github.javaparser.symbolsolver;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.symbolsolver.resolution.AbstractResolutionTest;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class _MyIssueTest extends AbstractResolutionTest {

    @Test
    void testOneInterface() {
        String code = "" +
                "@FunctionalInterface\n" +
                "interface InterfaceA {\n" +
                "    void func(String s);\n" +
                "}\n" +
                "class ClassA {\n" +
                "    void f() {\n" +
                "        InterfaceA ia = s -> {\n" +
                "            s.toUpperCase();\n" +
                "        };\n" +
                "    }\n" +
                "}";

        ParserConfiguration config = new ParserConfiguration();
        config.setSymbolResolver(new JavaSymbolSolver(new ReflectionTypeSolver(false)));
        StaticJavaParser.setConfiguration(config);

        CompilationUnit cu = StaticJavaParser.parse(code);

        MethodCallExpr expr = cu.findFirst(MethodCallExpr.class).get();
        assertEquals("java.lang.String.toUpperCase()", expr.resolve().getQualifiedSignature());
    }

    @Test
    void testTwoInterface() {
        String code = "" +
                "@FunctialInterface\n" +
                "interface InterfaceA {\n" +
                "    void funcA(String s);\n" +
                "}\n" +
                "interface InterfaceB {\n" +
                "    void funcB(String s);\n" +
                "}\n" +
                "class ClassA {\n" +
                "    void f() {\n" +
                "        InterfaceA iA = s1 -> {\n" +
                "            InterfaceB iB = s2 -> {\n" +
                "                s1.toUpperCase();\n" +
                "            };\n" +
                "        };\n" +
                "    }\n" +
                "}";

        ParserConfiguration config = new ParserConfiguration();
        config.setSymbolResolver(new JavaSymbolSolver(new ReflectionTypeSolver(false)));
        StaticJavaParser.setConfiguration(config);

        CompilationUnit cu = StaticJavaParser.parse(code);

        MethodCallExpr expr = cu.findFirst(MethodCallExpr.class).get();
        assertEquals("java.lang.String.toUpperCase()", expr.resolve().getQualifiedSignature());
    }
}
