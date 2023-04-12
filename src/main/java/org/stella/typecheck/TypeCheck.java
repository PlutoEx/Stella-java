package org.stella.typecheck;

import org.syntax.stella.Absyn.*;
import org.stella.typecheck.VisitTypeCheck;
import java.util.HashMap;

public class TypeCheck
{
    public static void typecheckProgram(Program program) throws Exception
    {
        VisitTypeCheck v = new VisitTypeCheck();
//        program.accept(v.new ProgramVisitor<>(), null /* initial context information*/);
        program.accept(v.new ProgramVisitor(), v.new ContextAndExpectedType(new HashMap<>(), null) /* initial context information*/);
    }
}
