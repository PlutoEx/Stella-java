// File generated by the BNF Converter (bnfc 2.9.4.1).

package org.stella.typecheck;

import org.syntax.stella.Absyn.*;
import org.syntax.stella.PrettyPrinter;

import java.util.HashMap;

/*** Visitor Design Pattern for TypeCheck. ***/

public class VisitTypeCheck
{
    public final class ContextAndExpectedType {
        HashMap<String, org.syntax.stella.Absyn.Type> context;
        org.syntax.stella.Absyn.Type expectedType;

        public ContextAndExpectedType(HashMap<String, Type> context, Type expectedType) {
            this.context = context;
            this.expectedType = expectedType;
        }
    }

    public class TypeError extends RuntimeException {
        public TypeError(String message) {
            super(message);
        }
    }

    public Type compareTypes(Expr e, Type actualType, Type expectedType) {
        if (expectedType == null) {
            return actualType;
        }
        if (actualType.equals(expectedType)) {
            return expectedType;
        }
        throw new TypeError("expected " + PrettyPrinter.print(expectedType) + " but got " + PrettyPrinter.print(actualType) + " for expression " + PrettyPrinter.print(e));
    }

    public class ProgramVisitor implements org.syntax.stella.Absyn.Program.Visitor<org.syntax.stella.Absyn.Type, ContextAndExpectedType>
    {
        public org.syntax.stella.Absyn.Type visit(org.syntax.stella.Absyn.AProgram p, ContextAndExpectedType arg)
        { /* Code for AProgram goes here */
            p.languagedecl_.accept(new LanguageDeclVisitor(), arg);
            for (org.syntax.stella.Absyn.Extension x: p.listextension_) {
                x.accept(new ExtensionVisitor(), arg);
            }
            for (org.syntax.stella.Absyn.Decl x: p.listdecl_) {
                x.accept(new DeclVisitor(), arg);
            }
            return null;
        }
    }
    public class LanguageDeclVisitor implements org.syntax.stella.Absyn.LanguageDecl.Visitor<org.syntax.stella.Absyn.Type, ContextAndExpectedType>
    {
        public org.syntax.stella.Absyn.Type visit(org.syntax.stella.Absyn.LanguageCore p, ContextAndExpectedType arg)
        { /* Code for LanguageCore goes here */
            return null;
        }
    }
    public class ExtensionVisitor implements org.syntax.stella.Absyn.Extension.Visitor<org.syntax.stella.Absyn.Type, ContextAndExpectedType>
    {
        public org.syntax.stella.Absyn.Type visit(org.syntax.stella.Absyn.AnExtension p, ContextAndExpectedType arg)
        { /* Code for AnExtension goes here */
            for (String x: p.listextensionname_) {
                //x;
            }
            return null;
        }
    }
    public class DeclVisitor implements org.syntax.stella.Absyn.Decl.Visitor<org.syntax.stella.Absyn.Type, ContextAndExpectedType>
    {
        public org.syntax.stella.Absyn.Type visit(org.syntax.stella.Absyn.DeclFun p, ContextAndExpectedType arg)
        { /* Code for DeclFun goes here */
            System.out.println("Visiting declaration of function " + p.stellaident_);

            for (org.syntax.stella.Absyn.Annotation x: p.listannotation_) {
                x.accept(new AnnotationVisitor(), arg);
            }
            //p.stellaident_;
            for (org.syntax.stella.Absyn.ParamDecl x: p.listparamdecl_) {
                x.accept(new ParamDeclVisitor(), arg);
            }
            p.returntype_.accept(new ReturnTypeVisitor(), arg);
            p.throwtype_.accept(new ThrowTypeVisitor(), arg);
            for (org.syntax.stella.Absyn.Decl x: p.listdecl_) {
                x.accept(new DeclVisitor(), arg);
            }

            HashMap<String, Type> newContext = new HashMap<>(arg.context);
            AParamDecl paramDecl = null;
            if (p.listparamdecl_.size() != 0) {
                paramDecl = (AParamDecl)p.listparamdecl_.get(0);
                newContext.put(paramDecl.stellaident_, paramDecl.type_);
            }

            Type returnType = p.returntype_.accept(new ReturnType.Visitor<Type, Object>() {
                @Override
                public Type visit(NoReturnType p, Object arg) {
                    throw new TypeError("missing return type in declaration");
                }

                @Override
                public Type visit(SomeReturnType p, Object arg) {
                    return p.type_;
                }
            }, null);

            ListType argListType = new ListType();
            if (paramDecl != null)
                argListType.add(paramDecl.type_);
            arg.context.put(p.stellaident_, new TypeFun(argListType, returnType));
            newContext.put(p.stellaident_, new TypeFun(argListType, returnType));

            p.expr_.accept(new ExprVisitor(), new ContextAndExpectedType(newContext, returnType));
            return null;
        }
        public org.syntax.stella.Absyn.Type visit(org.syntax.stella.Absyn.DeclTypeAlias p, ContextAndExpectedType arg)
        { /* Code for DeclTypeAlias goes here */
            //p.stellaident_;
            p.type_.accept(new TypeVisitor(), arg);
            return null;
        }
    }
    public class LocalDeclVisitor implements org.syntax.stella.Absyn.LocalDecl.Visitor<org.syntax.stella.Absyn.Type, ContextAndExpectedType>
    {
        public Type visit(org.syntax.stella.Absyn.ALocalDecl p, ContextAndExpectedType arg)
        { /* Code for ALocalDecl goes here */
            p.decl_.accept(new DeclVisitor(), arg);
            return null;
        }
    }
    public class AnnotationVisitor implements org.syntax.stella.Absyn.Annotation.Visitor<org.syntax.stella.Absyn.Type, ContextAndExpectedType>
    {
        public Type visit(org.syntax.stella.Absyn.InlineAnnotation p, ContextAndExpectedType arg)
        { /* Code for InlineAnnotation goes here */
            return null;
        }
    }
    public class ParamDeclVisitor implements org.syntax.stella.Absyn.ParamDecl.Visitor<org.syntax.stella.Absyn.Type, ContextAndExpectedType>
    {
        public Type visit(org.syntax.stella.Absyn.AParamDecl p, ContextAndExpectedType arg)
        { /* Code for AParamDecl goes here */
            //p.stellaident_;
            p.type_.accept(new TypeVisitor(), arg);
            return null;
        }
    }
    public class ReturnTypeVisitor implements org.syntax.stella.Absyn.ReturnType.Visitor<org.syntax.stella.Absyn.Type, ContextAndExpectedType>
    {
        public Type visit(org.syntax.stella.Absyn.NoReturnType p, ContextAndExpectedType arg)
        { /* Code for NoReturnType goes here */
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.SomeReturnType p, ContextAndExpectedType arg)
        { /* Code for SomeReturnType goes here */
            p.type_.accept(new TypeVisitor(), arg);
            return null;
        }
    }
    public class ThrowTypeVisitor implements org.syntax.stella.Absyn.ThrowType.Visitor<org.syntax.stella.Absyn.Type, ContextAndExpectedType>
    {
        public Type visit(org.syntax.stella.Absyn.NoThrowType p, ContextAndExpectedType arg)
        { /* Code for NoThrowType goes here */
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.SomeThrowType p, ContextAndExpectedType arg)
        { /* Code for SomeThrowType goes here */
            for (org.syntax.stella.Absyn.Type x: p.listtype_) {
                x.accept(new TypeVisitor(), arg);
            }
            return null;
        }
    }
    public class TypeVisitor implements org.syntax.stella.Absyn.Type.Visitor<org.syntax.stella.Absyn.Type, ContextAndExpectedType>
    {
        public Type visit(org.syntax.stella.Absyn.TypeFun p, ContextAndExpectedType arg)
        { /* Code for TypeFun goes here */
            for (org.syntax.stella.Absyn.Type x: p.listtype_) {
                x.accept(new TypeVisitor(), arg);
            }
            p.type_.accept(new TypeVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.TypeRec p, ContextAndExpectedType arg)
        { /* Code for TypeRec goes here */
            //p.stellaident_;
            p.type_.accept(new TypeVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.TypeSum p, ContextAndExpectedType arg)
        { /* Code for TypeSum goes here */
            p.type_1.accept(new TypeVisitor(), arg);
            p.type_2.accept(new TypeVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.TypeTuple p, ContextAndExpectedType arg)
        { /* Code for TypeTuple goes here */
            for (org.syntax.stella.Absyn.Type x: p.listtype_) {
                x.accept(new TypeVisitor(), arg);
            }
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.TypeRecord p, ContextAndExpectedType arg)
        { /* Code for TypeRecord goes here */
            for (org.syntax.stella.Absyn.RecordFieldType x: p.listrecordfieldtype_) {
                x.accept(new RecordFieldTypeVisitor(), arg);
            }
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.TypeVariant p, ContextAndExpectedType arg)
        { /* Code for TypeVariant goes here */
            for (org.syntax.stella.Absyn.VariantFieldType x: p.listvariantfieldtype_) {
                x.accept(new VariantFieldTypeVisitor(), arg);
            }
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.TypeList p, ContextAndExpectedType arg)
        { /* Code for TypeList goes here */
            p.type_.accept(new TypeVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.TypeBool p, ContextAndExpectedType arg)
        { /* Code for TypeBool goes here */
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.TypeNat p, ContextAndExpectedType arg)
        { /* Code for TypeNat goes here */
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.TypeUnit p, ContextAndExpectedType arg)
        { /* Code for TypeUnit goes here */
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.TypeVar p, ContextAndExpectedType arg)
        { /* Code for TypeVar goes here */
            //p.stellaident_;
            return null;
        }
    }
    public class MatchCaseVisitor implements org.syntax.stella.Absyn.MatchCase.Visitor<org.syntax.stella.Absyn.Type, ContextAndExpectedType>
    {
        public Type visit(org.syntax.stella.Absyn.AMatchCase p, ContextAndExpectedType arg)
        { /* Code for AMatchCase goes here */
            p.pattern_.accept(new PatternVisitor(), arg);
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
    }
    public class OptionalTypingVisitor implements org.syntax.stella.Absyn.OptionalTyping.Visitor<org.syntax.stella.Absyn.Type, ContextAndExpectedType>
    {
        public Type visit(org.syntax.stella.Absyn.NoTyping p, ContextAndExpectedType arg)
        { /* Code for NoTyping goes here */
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.SomeTyping p, ContextAndExpectedType arg)
        { /* Code for SomeTyping goes here */
            p.type_.accept(new TypeVisitor(), arg);
            return null;
        }
    }
    public class PatternDataVisitor implements org.syntax.stella.Absyn.PatternData.Visitor<org.syntax.stella.Absyn.Type, ContextAndExpectedType>
    {
        public Type visit(org.syntax.stella.Absyn.NoPatternData p, ContextAndExpectedType arg)
        { /* Code for NoPatternData goes here */
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.SomePatternData p, ContextAndExpectedType arg)
        { /* Code for SomePatternData goes here */
            p.pattern_.accept(new PatternVisitor(), arg);
            return null;
        }
    }
    public class ExprDataVisitor implements org.syntax.stella.Absyn.ExprData.Visitor<org.syntax.stella.Absyn.Type, ContextAndExpectedType>
    {
        public Type visit(org.syntax.stella.Absyn.NoExprData p, ContextAndExpectedType arg)
        { /* Code for NoExprData goes here */
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.SomeExprData p, ContextAndExpectedType arg)
        { /* Code for SomeExprData goes here */
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
    }
    public class PatternVisitor implements org.syntax.stella.Absyn.Pattern.Visitor<org.syntax.stella.Absyn.Type, ContextAndExpectedType>
    {
        public Type visit(org.syntax.stella.Absyn.PatternVariant p, ContextAndExpectedType arg)
        { /* Code for PatternVariant goes here */
            //p.stellaident_;
            p.patterndata_.accept(new PatternDataVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.PatternInl p, ContextAndExpectedType arg)
        { /* Code for PatternInl goes here */
            p.pattern_.accept(new PatternVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.PatternInr p, ContextAndExpectedType arg)
        { /* Code for PatternInr goes here */
            p.pattern_.accept(new PatternVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.PatternTuple p, ContextAndExpectedType arg)
        { /* Code for PatternTuple goes here */
            System.out.println("IN PatternTuple");
            for (org.syntax.stella.Absyn.Pattern x: p.listpattern_) {
                x.accept(new PatternVisitor(), arg);
            }
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.PatternRecord p, ContextAndExpectedType arg)
        { /* Code for PatternRecord goes here */
            for (org.syntax.stella.Absyn.LabelledPattern x: p.listlabelledpattern_) {
                x.accept(new LabelledPatternVisitor(), arg);
            }
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.PatternList p, ContextAndExpectedType arg)
        { /* Code for PatternList goes here */
            for (org.syntax.stella.Absyn.Pattern x: p.listpattern_) {
                x.accept(new PatternVisitor(), arg);
            }
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.PatternCons p, ContextAndExpectedType arg)
        { /* Code for PatternCons goes here */
            p.pattern_1.accept(new PatternVisitor(), arg);
            p.pattern_2.accept(new PatternVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.PatternFalse p, ContextAndExpectedType arg)
        { /* Code for PatternFalse goes here */
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.PatternTrue p, ContextAndExpectedType arg)
        { /* Code for PatternTrue goes here */
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.PatternUnit p, ContextAndExpectedType arg)
        { /* Code for PatternUnit goes here */
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.PatternInt p, ContextAndExpectedType arg)
        { /* Code for PatternInt goes here */
            //p.integer_;
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.PatternSucc p, ContextAndExpectedType arg)
        { /* Code for PatternSucc goes here */
            p.pattern_.accept(new PatternVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.PatternVar p, ContextAndExpectedType arg)
        { /* Code for PatternVar goes here */
            //p.stellaident_;
            return null;
        }
    }
    public class LabelledPatternVisitor implements org.syntax.stella.Absyn.LabelledPattern.Visitor<org.syntax.stella.Absyn.Type, ContextAndExpectedType>
    {
        public Type visit(org.syntax.stella.Absyn.ALabelledPattern p, ContextAndExpectedType arg)
        { /* Code for ALabelledPattern goes here */
            //p.stellaident_;
            p.pattern_.accept(new PatternVisitor(), arg);
            return null;
        }
    }
    public class BindingVisitor implements org.syntax.stella.Absyn.Binding.Visitor<org.syntax.stella.Absyn.Type, ContextAndExpectedType>
    {
        public Type visit(org.syntax.stella.Absyn.ABinding p, ContextAndExpectedType arg)
        { /* Code for ABinding goes here */
            //p.stellaident_;
            Type type = p.expr_.accept(new ExprVisitor(), arg);
//            return null;
            if (type instanceof TypeRec)
                type = ((TypeRec) type).type_;
            return new TypeRec(p.stellaident_, type);
        }
    }
    public class ExprVisitor implements org.syntax.stella.Absyn.Expr.Visitor<org.syntax.stella.Absyn.Type, ContextAndExpectedType>
    {
        public Type visit(org.syntax.stella.Absyn.Sequence p, ContextAndExpectedType arg)
        { /* Code for Sequence goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.If p, ContextAndExpectedType arg)
        { /* Code for If goes here */
            System.out.println("Visiting if");
            p.expr_1.accept(new ExprVisitor(), new ContextAndExpectedType(arg.context, new TypeBool()));
            Type thenType = p.expr_2.accept(new ExprVisitor(), arg);
            p.expr_3.accept(new ExprVisitor(), new ContextAndExpectedType(arg.context, thenType));
            return compareTypes(p, thenType, arg.expectedType);
        }
        public Type visit(org.syntax.stella.Absyn.Let p, ContextAndExpectedType arg)
        { /* Code for Let goes here */
            for (org.syntax.stella.Absyn.PatternBinding x: p.listpatternbinding_) {
                x.accept(new PatternBindingVisitor(), arg);
            }
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.LetRec p, ContextAndExpectedType arg)
        { /* Code for LetRec goes here */
            for (org.syntax.stella.Absyn.PatternBinding x: p.listpatternbinding_) {
                x.accept(new PatternBindingVisitor(), arg);
            }
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.LessThan p, ContextAndExpectedType arg)
        { /* Code for LessThan goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.LessThanOrEqual p, ContextAndExpectedType arg)
        { /* Code for LessThanOrEqual goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.GreaterThan p, ContextAndExpectedType arg)
        { /* Code for GreaterThan goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.GreaterThanOrEqual p, ContextAndExpectedType arg)
        { /* Code for GreaterThanOrEqual goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.Equal p, ContextAndExpectedType arg)
        { /* Code for Equal goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.NotEqual p, ContextAndExpectedType arg)
        { /* Code for NotEqual goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.TypeAsc p, ContextAndExpectedType arg)
        { /* Code for TypeAsc goes here */
            p.expr_.accept(new ExprVisitor(), arg);
            p.type_.accept(new TypeVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.Abstraction p, ContextAndExpectedType arg)
        { /* Code for Abstraction goes here */
            HashMap newContext = new HashMap<>(arg.context);
            AParamDecl paramDecl = (AParamDecl)p.listparamdecl_.get(0);
            newContext.put(paramDecl.stellaident_, paramDecl.type_);

            Type bodyType = null;
            if (arg.expectedType != null) {
                if (arg.expectedType instanceof TypeFun) {
                    compareTypes(new Var(paramDecl.stellaident_), paramDecl.type_, ((TypeFun)arg.expectedType).listtype_.get(0));
                    bodyType = ((TypeFun)arg.expectedType).type_;
                } else {
                    throw new TypeError("unexpected lambda abstraction");
                }
            }
            bodyType = p.expr_.accept(new ExprVisitor(), new ContextAndExpectedType(newContext, bodyType));

            ListType argType = new ListType();
            argType.add(paramDecl.type_);
            return compareTypes(p, new TypeFun(argType, bodyType), arg.expectedType);
        }
        public Type visit(org.syntax.stella.Absyn.Variant p, ContextAndExpectedType arg)
        { /* Code for Variant goes here */
            //p.stellaident_;
            p.exprdata_.accept(new ExprDataVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.Match p, ContextAndExpectedType arg)
        { /* Code for Match goes here */
            p.expr_.accept(new ExprVisitor(), arg);
            for (org.syntax.stella.Absyn.MatchCase x: p.listmatchcase_) {
                x.accept(new MatchCaseVisitor(), arg);
            }
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.List p, ContextAndExpectedType arg)
        { /* Code for List goes here */
            Type prev = null;
            for (org.syntax.stella.Absyn.Expr x: p.listexpr_) {
                if (prev == null)
                    prev = x.accept(new ExprVisitor(), new ContextAndExpectedType(arg.context, null));
                else
                    prev = x.accept(new ExprVisitor(), new ContextAndExpectedType(arg.context, prev));
            }
            return compareTypes(p, new TypeList(prev), arg.expectedType);
        }
        public Type visit(org.syntax.stella.Absyn.Add p, ContextAndExpectedType arg)
        { /* Code for Add goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.Subtract p, ContextAndExpectedType arg)
        { /* Code for Subtract goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.LogicOr p, ContextAndExpectedType arg)
        { /* Code for LogicOr goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.Multiply p, ContextAndExpectedType arg)
        { /* Code for Multiply goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.Divide p, ContextAndExpectedType arg)
        { /* Code for Divide goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.LogicAnd p, ContextAndExpectedType arg)
        { /* Code for LogicAnd goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.Application p, ContextAndExpectedType arg)
        { /* Code for Application goes here */
            Type funType = p.expr_.accept(new ExprVisitor(), new ContextAndExpectedType(arg.context, null));
            if (funType instanceof TypeFun) {
                Type argType = ((TypeFun)funType).listtype_.get(0);
                Type retType = ((TypeFun)funType).type_;
                p.listexpr_.get(0).accept(new ExprVisitor(), new ContextAndExpectedType(arg.context, argType));
                return compareTypes(p, retType, arg.expectedType);
            } else {
                throw new TypeError("trying to apply an expression of a non-function type");
            }
        }
        public Type visit(org.syntax.stella.Absyn.DotRecord p, ContextAndExpectedType arg)
        { /* Code for DotRecord goes here */
            Type type = p.expr_.accept(new ExprVisitor(), new ContextAndExpectedType(arg.context, null));
            if (type instanceof TypeRecord) {
                ListRecordFieldType params = ((TypeRecord) type).listrecordfieldtype_;
                for (RecordFieldType i : params)
                    if (((ARecordFieldType) i).stellaident_.equals(p.stellaident_))
                        return compareTypes(p, new TypeRec(((ARecordFieldType) i).stellaident_, ((ARecordFieldType) i).type_), arg.expectedType);
                throw new TypeError("not found dot param from Record");
            }
            else
                throw new TypeError("trying to get dot param from not Record");
        }
        public Type visit(org.syntax.stella.Absyn.DotTuple p, ContextAndExpectedType arg)
        { /* Code for DotTuple goes here */
            Type type = p.expr_.accept(new ExprVisitor(), new ContextAndExpectedType(arg.context, null));
            if (type instanceof TypeTuple) {
                ListType params = ((TypeTuple) type).listtype_;
                return compareTypes(p, params.get(p.integer_ - 1), arg.expectedType);
            }
            else
                throw new TypeError("trying to get dot param from not Tuple");
        }
        public Type visit(org.syntax.stella.Absyn.Tuple p, ContextAndExpectedType arg)
        { /* Code for Tuple goes here */
            ListType params = new ListType();
            for (org.syntax.stella.Absyn.Expr x: p.listexpr_) {
                Type type = x.accept(new ExprVisitor(), new ContextAndExpectedType(arg.context, null));
                params.add(type);
            }
            return compareTypes(p, new TypeTuple(params), arg.expectedType);
        }
        public Type visit(org.syntax.stella.Absyn.Record p, ContextAndExpectedType arg)
        { /* Code for Record goes here */
            ListRecordFieldType params = new ListRecordFieldType();
            for (org.syntax.stella.Absyn.Binding x: p.listbinding_) {
                Type type = x.accept(new BindingVisitor(), new ContextAndExpectedType(arg.context, null));
                params.add(new ARecordFieldType(((TypeRec) type).stellaident_, ((TypeRec) type).type_));
            }
            return compareTypes(p, new TypeRecord(params), arg.expectedType);
        }
        public Type visit(org.syntax.stella.Absyn.ConsList p, ContextAndExpectedType arg)
        { /* Code for ConsList goes here */
            p.expr_1.accept(new ExprVisitor(), arg);
            p.expr_2.accept(new ExprVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.Head p, ContextAndExpectedType arg)
        { /* Code for Head goes here */
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.IsEmpty p, ContextAndExpectedType arg)
        { /* Code for IsEmpty goes here */
            System.out.println("Visiting isEmpty");
            Type type = p.expr_.accept(new ExprVisitor(), new ContextAndExpectedType(arg.context, null));
            if (type instanceof TypeList)
                return compareTypes(p, new TypeBool(), arg.expectedType);
            else
                throw new TypeError("trying to apply an isempty to not List");
        }
        public Type visit(org.syntax.stella.Absyn.Tail p, ContextAndExpectedType arg)
        { /* Code for Tail goes here */
            System.out.println("Visiting tail");
            Type type = p.expr_.accept(new ExprVisitor(), new ContextAndExpectedType(arg.context, null));
            if (type instanceof TypeList)
                return compareTypes(p, type , arg.expectedType);
            else
                throw new TypeError("trying to apply an tail to not List");
        }
        public Type visit(org.syntax.stella.Absyn.Inl p, ContextAndExpectedType arg)
        { /* Code for Inl goes here */
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.Inr p, ContextAndExpectedType arg)
        { /* Code for Inr goes here */
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.Succ p, ContextAndExpectedType arg)
        { /* Code for Succ goes here */
            System.out.println("Visiting succ");
            p.expr_.accept(new ExprVisitor(), new ContextAndExpectedType(arg.context, new TypeNat()));
            return compareTypes(p, new TypeNat(), arg.expectedType);
        }
        public Type visit(org.syntax.stella.Absyn.LogicNot p, ContextAndExpectedType arg)
        { /* Code for LogicNot goes here */
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.Pred p, ContextAndExpectedType arg)
        { /* Code for Pred goes here */
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.IsZero p, ContextAndExpectedType arg)
        { /* Code for IsZero goes here */
            p.expr_.accept(new ExprVisitor(), new ContextAndExpectedType(arg.context, new TypeNat()));
            return compareTypes(p, new TypeBool(), arg.expectedType);
        }
        public Type visit(org.syntax.stella.Absyn.Fix p, ContextAndExpectedType arg)
        { /* Code for Fix goes here */
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.NatRec p, ContextAndExpectedType arg)
        { /* Code for NatRec goes here */
            p.expr_1.accept(new ExprVisitor(), new ContextAndExpectedType(arg.context, new TypeNat()));
            Type returnType = p.expr_2.accept(new ExprVisitor(), arg);
            ListType arg1 = new ListType();
            arg1.add(new TypeNat());
            ListType arg2 = new ListType();
            arg2.add(returnType);
            Type stepType = new TypeFun(arg1, new TypeFun(arg2, returnType));
            p.expr_3.accept(new ExprVisitor(), new ContextAndExpectedType(arg.context, stepType));
            return compareTypes(p, returnType, arg.expectedType);
        }
        public Type visit(org.syntax.stella.Absyn.Fold p, ContextAndExpectedType arg)
        { /* Code for Fold goes here */
            p.type_.accept(new TypeVisitor(), arg);
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.Unfold p, ContextAndExpectedType arg)
        { /* Code for Unfold goes here */
            p.type_.accept(new TypeVisitor(), arg);
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.ConstTrue p, ContextAndExpectedType arg)
        { /* Code for ConstTrue goes here */
            return compareTypes(p, new TypeBool(), arg.expectedType);
        }
        public Type visit(org.syntax.stella.Absyn.ConstFalse p, ContextAndExpectedType arg)
        { /* Code for ConstFalse goes here */
            return compareTypes(p, new TypeBool(), arg.expectedType);
        }
        public Type visit(org.syntax.stella.Absyn.ConstUnit p, ContextAndExpectedType arg)
        { /* Code for ConstUnit goes here */
            return null;
        }
        public Type visit(org.syntax.stella.Absyn.ConstInt p, ContextAndExpectedType arg)
        { /* Code for ConstInt goes here */
            //p.integer_;
            return compareTypes(p, new TypeNat(), arg.expectedType);
        }
        public Type visit(org.syntax.stella.Absyn.Var p, ContextAndExpectedType arg)
        { /* Code for Var goes here */
            //p.stellaident_;
            Type varType = arg.context.get(p.stellaident_);
            if (varType == null) {
                throw new TypeError("undefined variable");
            } else {
                return compareTypes(p, varType, arg.expectedType);
            }
        }
    }
    public class PatternBindingVisitor implements org.syntax.stella.Absyn.PatternBinding.Visitor<org.syntax.stella.Absyn.Type, ContextAndExpectedType>
    {
        public Type visit(org.syntax.stella.Absyn.APatternBinding p, ContextAndExpectedType arg)
        { /* Code for APatternBinding goes here */
            p.pattern_.accept(new PatternVisitor(), arg);
            p.expr_.accept(new ExprVisitor(), arg);
            return null;
        }
    }
    public class VariantFieldTypeVisitor implements org.syntax.stella.Absyn.VariantFieldType.Visitor<org.syntax.stella.Absyn.Type, ContextAndExpectedType>
    {
        public Type visit(org.syntax.stella.Absyn.AVariantFieldType p, ContextAndExpectedType arg)
        { /* Code for AVariantFieldType goes here */
            //p.stellaident_;
            p.optionaltyping_.accept(new OptionalTypingVisitor(), arg);
            return null;
        }
    }
    public class RecordFieldTypeVisitor implements org.syntax.stella.Absyn.RecordFieldType.Visitor<org.syntax.stella.Absyn.Type, ContextAndExpectedType>
    {
        public Type visit(org.syntax.stella.Absyn.ARecordFieldType p, ContextAndExpectedType arg)
        { /* Code for ARecordFieldType goes here */
            //p.stellaident_;
            p.type_.accept(new TypeVisitor(), arg);
            return null;
        }
    }
    public class TypingVisitor implements org.syntax.stella.Absyn.Typing.Visitor<org.syntax.stella.Absyn.Type, ContextAndExpectedType>
    {
        public Type visit(org.syntax.stella.Absyn.ATyping p, ContextAndExpectedType arg)
        { /* Code for ATyping goes here */
            p.expr_.accept(new ExprVisitor(), arg);
            p.type_.accept(new TypeVisitor(), arg);
            return null;
        }
    }
}
