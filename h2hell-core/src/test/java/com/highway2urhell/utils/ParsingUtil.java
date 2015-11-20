package com.highway2urhell.utils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParsingUtil {
    public static String[] extractPackages(String in) throws ParseException, FileNotFoundException {
        List<String> classes = extractImports(in);
        Set<String> ret = new HashSet<String>();
        for (String aClass : classes) {
            ret.add(aClass.substring(0, aClass.lastIndexOf(".")));
        }

        return ret.toArray(new String[]{});
    }

    public static List<String> extractImports(String in) throws ParseException, FileNotFoundException {
        CompilationUnit parse = JavaParser.parse(new FileInputStream(in));
        ImportVisitor importVisitor = new ImportVisitor();
        importVisitor.visit(parse, "");
        return importVisitor.impDec;
    }

    private static class ImportVisitor extends VoidVisitorAdapter<String> {
        public final List<String> impDec = new ArrayList<String>();

        @Override
        public void visit(ImportDeclaration n, String a) {
            impDec.add("" + n.getName());
        }
    }

    public static String extractBody(String in, String methodName) throws ParseException, FileNotFoundException {
        return extractMethod(JavaParser.parse(new FileInputStream(in)), methodName);
    }

    public static String extractBody(InputStream in, String methodName) throws ParseException {
        return extractMethod(JavaParser.parse(in), methodName);
    }

    private static String extractMethod(CompilationUnit cu, String methodName) {
        return new MethodBodyVisitor().visit(cu, methodName);
    }

    private static class MethodBodyVisitor extends GenericVisitorAdapter<String, String> {
        @Override
        public String visit(MethodDeclaration n, String arg) {
            return arg.equals(n.getName()) ? "" + n.getBody() : null;
        }
    }

}
