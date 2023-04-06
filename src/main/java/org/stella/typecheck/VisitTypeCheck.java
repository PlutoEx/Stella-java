// File generated by the BNF Converter (bnfc 2.9.4.1).

package org.stella.typecheck;

import com.ibm.icu.impl.Pair;
import org.syntax.stella.Absyn.*;

import java.util.Objects;
import java.util.Vector;

/*** Visitor Design Pattern Skeleton. ***/

/* This implements the common visitor design pattern.
   Tests show it to be slightly less efficient than the
   instanceof method, but easier to use.
   Replace the R and A parameters with the desired return
   and context types.*/

public class VisitTypeCheck {

    // Temp vars
    Type type_tmp = new TypeNat();
    String name_tmp = "";
    Vector<Pair<String, Type> > local = new Vector<>();

    // Const vars
    Vector<Pair<String, Type> > global = new Vector<>();
    Vector<String> checkpoints = new Vector<>();

    // Find function in global
    Type findFun(String name) {
        for (Pair<String, Type> i: global) {
            if (i.first.equals(name))
                return i.second;
        }
        System.out.println("Error: Not found '" + name + "'");
        System.exit(1);
        return null;
    }

    void enterScope() {
        checkpoints.add(global.lastElement().first);
    }

    void exitScope() {
        while (!global.lastElement().first.equals(checkpoints.lastElement())) {
            if (global.isEmpty()) {
                System.out.println("Something wrong: empty global after exit Scope");
                System.exit(1);
            }
            global.remove(global.size() - 1);
        }
        checkpoints.remove(checkpoints.size() - 1);
    }

    public class ProgramVisitor<R, A> implements org.syntax.stella.Absyn.Program.Visitor<R, A> {
        public R visit(org.syntax.stella.Absyn.AProgram p, A arg) { /* Code for AProgram goes here */
            p.languagedecl_.accept(new LanguageDeclVisitor<R, A>(), arg);
            for (org.syntax.stella.Absyn.Extension x : p.listextension_) {
                x.accept(new ExtensionVisitor<R, A>(), arg);
            }
            for (org.syntax.stella.Absyn.Decl x : p.listdecl_) {
                x.accept(new DeclVisitor<R, A>(), arg);
            }
            return null;
        }
    }

    public class LanguageDeclVisitor<R, A> implements org.syntax.stella.Absyn.LanguageDecl.Visitor<R, A> {
        public R visit(org.syntax.stella.Absyn.LanguageCore p, A arg) { /* Code for LanguageCore goes here */
            return null;
        }
    }

    public class ExtensionVisitor<R, A> implements org.syntax.stella.Absyn.Extension.Visitor<R, A> {
        public R visit(org.syntax.stella.Absyn.AnExtension p, A arg) { /* Code for AnExtension goes here */
            for (String x : p.listextensionname_) {
                //x;
            }
            return null;
        }
    }

    public class DeclVisitor<R, A> implements org.syntax.stella.Absyn.Decl.Visitor<R, A> {
        public R visit(org.syntax.stella.Absyn.DeclFun p, A arg) { /* Code for DeclFun goes here */
            for (org.syntax.stella.Absyn.Annotation x : p.listannotation_) {
                x.accept(new AnnotationVisitor<R, A>(), arg);
            }

            // Save function name
            String fun_name = p.stellaident_;

            // Save function parameters
            ListType fun_params_type = new ListType();
            for (org.syntax.stella.Absyn.ParamDecl x : p.listparamdecl_) {
                type_tmp = null;
                x.accept(new ParamDeclVisitor<R, A>(), arg);
                fun_params_type.add(type_tmp);
            }

            // Save return type
            type_tmp = null;
            p.returntype_.accept(new ReturnTypeVisitor<R, A>(), arg);
            Type fun_return = type_tmp;

            p.throwtype_.accept(new ThrowTypeVisitor<R, A>(), arg);
            for (org.syntax.stella.Absyn.Decl x : p.listdecl_) {
                x.accept(new DeclVisitor<R, A>(), arg);
            }

            // Save our function then save parameters as functions from local
            global.add(Pair.of(fun_name, new TypeFun(fun_params_type, fun_return)));
            global.addAll(local);
            local.clear();


            enterScope();
            p.expr_.accept(new ExprVisitor<R, A>(), arg);

//            for (Pair<String, TypeFun> i : global) {
//                System.out.println("Name: " + i.first + " Return: " + i.second.type_);
//                System.out.println("Params: " + i.second.listtype_);
//            }


            // Check return statement matching with actual
            if (!fun_return.equals(type_tmp)) {
                System.out.println("Error: Not equal return in function '" + fun_name + "': " + fun_return + " and " + type_tmp);
                System.exit(1);
            }
            exitScope();
            return null;
        }

        public R visit(org.syntax.stella.Absyn.DeclTypeAlias p, A arg) { /* Code for DeclTypeAlias goes here */
            //p.stellaident_;
            p.type_.accept(new TypeVisitor<R, A>(), arg);
            return null;
        }
    }

    public class LocalDeclVisitor<R, A> implements org.syntax.stella.Absyn.LocalDecl.Visitor<R, A> {
        public R visit(org.syntax.stella.Absyn.ALocalDecl p, A arg) { /* Code for ALocalDecl goes here */
            p.decl_.accept(new DeclVisitor<R, A>(), arg);
            return null;
        }
    }

    public class AnnotationVisitor<R, A> implements org.syntax.stella.Absyn.Annotation.Visitor<R, A> {
        public R visit(org.syntax.stella.Absyn.InlineAnnotation p, A arg) { /* Code for InlineAnnotation goes here */
            return null;
        }
    }

    public class ParamDeclVisitor<R, A> implements org.syntax.stella.Absyn.ParamDecl.Visitor<R, A> {
        public R visit(org.syntax.stella.Absyn.AParamDecl p, A arg) { /* Code for AParamDecl goes here */
            p.type_.accept(new TypeVisitor<R, A>(), arg);
            name_tmp = p.stellaident_;
            if(p.type_.getClass() == TypeFun.class)
                local.add(Pair.of(p.stellaident_, (TypeFun) p.type_));
            local.add(Pair.of(p.stellaident_, p.type_));
            return null;
        }
    }

    public class ReturnTypeVisitor<R, A> implements org.syntax.stella.Absyn.ReturnType.Visitor<R, A> {
        public R visit(org.syntax.stella.Absyn.NoReturnType p, A arg) { /* Code for NoReturnType goes here */
            return null;
        }

        public R visit(org.syntax.stella.Absyn.SomeReturnType p, A arg) { /* Code for SomeReturnType goes here */
            p.type_.accept(new TypeVisitor<R, A>(), arg);
            return null;
        }
    }

    public class ThrowTypeVisitor<R, A> implements org.syntax.stella.Absyn.ThrowType.Visitor<R, A> {
        public R visit(org.syntax.stella.Absyn.NoThrowType p, A arg) { /* Code for NoThrowType goes here */
            return null;
        }

        public R visit(org.syntax.stella.Absyn.SomeThrowType p, A arg) { /* Code for SomeThrowType goes here */
            for (org.syntax.stella.Absyn.Type x : p.listtype_) {
                x.accept(new TypeVisitor<R, A>(), arg);
            }
            return null;
        }
    }

    public class TypeVisitor<R, A> implements org.syntax.stella.Absyn.Type.Visitor<R, A> {
        public R visit(org.syntax.stella.Absyn.TypeFun p, A arg) { /* Code for TypeFun goes here */
            for (org.syntax.stella.Absyn.Type x : p.listtype_) {
                x.accept(new TypeVisitor<R, A>(), arg);
            }
            p.type_.accept(new TypeVisitor<R, A>(), arg);
            type_tmp = p;
            return null;
        }

        public R visit(org.syntax.stella.Absyn.TypeRec p, A arg) { /* Code for TypeRec goes here */
            //p.stellaident_;
            p.type_.accept(new TypeVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.TypeSum p, A arg) { /* Code for TypeSum goes here */
            p.type_1.accept(new TypeVisitor<R, A>(), arg);
            p.type_2.accept(new TypeVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.TypeTuple p, A arg) { /* Code for TypeTuple goes here */
            for (org.syntax.stella.Absyn.Type x : p.listtype_) {
                x.accept(new TypeVisitor<R, A>(), arg);
            }
            return null;
        }

        public R visit(org.syntax.stella.Absyn.TypeRecord p, A arg) { /* Code for TypeRecord goes here */
            for (org.syntax.stella.Absyn.RecordFieldType x : p.listrecordfieldtype_) {
                x.accept(new RecordFieldTypeVisitor<R, A>(), arg);
            }
            return null;
        }

        public R visit(org.syntax.stella.Absyn.TypeVariant p, A arg) { /* Code for TypeVariant goes here */
            for (org.syntax.stella.Absyn.VariantFieldType x : p.listvariantfieldtype_) {
                x.accept(new VariantFieldTypeVisitor<R, A>(), arg);
            }
            return null;
        }

        public R visit(org.syntax.stella.Absyn.TypeList p, A arg) { /* Code for TypeList goes here */
            p.type_.accept(new TypeVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.TypeBool p, A arg) { /* Code for TypeBool goes here */
            type_tmp = p;
            return null;
        }

        public R visit(org.syntax.stella.Absyn.TypeNat p, A arg) { /* Code for TypeNat goes here */
            type_tmp = p;
            return null;
        }

        public R visit(org.syntax.stella.Absyn.TypeUnit p, A arg) { /* Code for TypeUnit goes here */
            return null;
        }

        public R visit(org.syntax.stella.Absyn.TypeVar p, A arg) { /* Code for TypeVar goes here */
            //p.stellaident_;
            return null;
        }
    }

    public class MatchCaseVisitor<R, A> implements org.syntax.stella.Absyn.MatchCase.Visitor<R, A> {
        public R visit(org.syntax.stella.Absyn.AMatchCase p, A arg) { /* Code for AMatchCase goes here */
            p.pattern_.accept(new PatternVisitor<R, A>(), arg);
            p.expr_.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }
    }

    public class OptionalTypingVisitor<R, A> implements org.syntax.stella.Absyn.OptionalTyping.Visitor<R, A> {
        public R visit(org.syntax.stella.Absyn.NoTyping p, A arg) { /* Code for NoTyping goes here */
            return null;
        }

        public R visit(org.syntax.stella.Absyn.SomeTyping p, A arg) { /* Code for SomeTyping goes here */
            p.type_.accept(new TypeVisitor<R, A>(), arg);
            return null;
        }
    }

    public class PatternDataVisitor<R, A> implements org.syntax.stella.Absyn.PatternData.Visitor<R, A> {
        public R visit(org.syntax.stella.Absyn.NoPatternData p, A arg) { /* Code for NoPatternData goes here */
            return null;
        }

        public R visit(org.syntax.stella.Absyn.SomePatternData p, A arg) { /* Code for SomePatternData goes here */
            p.pattern_.accept(new PatternVisitor<R, A>(), arg);
            return null;
        }
    }

    public class ExprDataVisitor<R, A> implements org.syntax.stella.Absyn.ExprData.Visitor<R, A> {
        public R visit(org.syntax.stella.Absyn.NoExprData p, A arg) { /* Code for NoExprData goes here */
            return null;
        }

        public R visit(org.syntax.stella.Absyn.SomeExprData p, A arg) { /* Code for SomeExprData goes here */
            p.expr_.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }
    }

    public class PatternVisitor<R, A> implements org.syntax.stella.Absyn.Pattern.Visitor<R, A> {
        public R visit(org.syntax.stella.Absyn.PatternVariant p, A arg) { /* Code for PatternVariant goes here */
            //p.stellaident_;
            p.patterndata_.accept(new PatternDataVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.PatternInl p, A arg) { /* Code for PatternInl goes here */
            p.pattern_.accept(new PatternVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.PatternInr p, A arg) { /* Code for PatternInr goes here */
            p.pattern_.accept(new PatternVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.PatternTuple p, A arg) { /* Code for PatternTuple goes here */
            for (org.syntax.stella.Absyn.Pattern x : p.listpattern_) {
                x.accept(new PatternVisitor<R, A>(), arg);
            }
            return null;
        }

        public R visit(org.syntax.stella.Absyn.PatternRecord p, A arg) { /* Code for PatternRecord goes here */
            for (org.syntax.stella.Absyn.LabelledPattern x : p.listlabelledpattern_) {
                x.accept(new LabelledPatternVisitor<R, A>(), arg);
            }
            return null;
        }

        public R visit(org.syntax.stella.Absyn.PatternList p, A arg) { /* Code for PatternList goes here */
            for (org.syntax.stella.Absyn.Pattern x : p.listpattern_) {
                x.accept(new PatternVisitor<R, A>(), arg);
            }
            return null;
        }

        public R visit(org.syntax.stella.Absyn.PatternCons p, A arg) { /* Code for PatternCons goes here */
            p.pattern_1.accept(new PatternVisitor<R, A>(), arg);
            p.pattern_2.accept(new PatternVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.PatternFalse p, A arg) { /* Code for PatternFalse goes here */
            return null;
        }

        public R visit(org.syntax.stella.Absyn.PatternTrue p, A arg) { /* Code for PatternTrue goes here */
            return null;
        }

        public R visit(org.syntax.stella.Absyn.PatternUnit p, A arg) { /* Code for PatternUnit goes here */
            return null;
        }

        public R visit(org.syntax.stella.Absyn.PatternInt p, A arg) { /* Code for PatternInt goes here */
            //p.integer_;
            return null;
        }

        public R visit(org.syntax.stella.Absyn.PatternSucc p, A arg) { /* Code for PatternSucc goes here */
            p.pattern_.accept(new PatternVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.PatternVar p, A arg) { /* Code for PatternVar goes here */
            //p.stellaident_;
            return null;
        }
    }

    public class LabelledPatternVisitor<R, A> implements org.syntax.stella.Absyn.LabelledPattern.Visitor<R, A> {
        public R visit(org.syntax.stella.Absyn.ALabelledPattern p, A arg) { /* Code for ALabelledPattern goes here */
            //p.stellaident_;
            p.pattern_.accept(new PatternVisitor<R, A>(), arg);
            return null;
        }
    }

    public class BindingVisitor<R, A> implements org.syntax.stella.Absyn.Binding.Visitor<R, A> {
        public R visit(org.syntax.stella.Absyn.ABinding p, A arg) { /* Code for ABinding goes here */
            //p.stellaident_;
            p.expr_.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }
    }

    public class ExprVisitor<R, A> implements org.syntax.stella.Absyn.Expr.Visitor<R, A> {
        public R visit(org.syntax.stella.Absyn.Sequence p, A arg) { /* Code for Sequence goes here */
            p.expr_1.accept(new ExprVisitor<R, A>(), arg);
            p.expr_2.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.If p, A arg) { /* Code for If goes here */
            p.expr_1.accept(new ExprVisitor<R, A>(), arg);
            p.expr_2.accept(new ExprVisitor<R, A>(), arg);
            p.expr_3.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.Let p, A arg) { /* Code for Let goes here */
            for (org.syntax.stella.Absyn.PatternBinding x : p.listpatternbinding_) {
                x.accept(new PatternBindingVisitor<R, A>(), arg);
            }
            p.expr_.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.LetRec p, A arg) { /* Code for LetRec goes here */
            for (org.syntax.stella.Absyn.PatternBinding x : p.listpatternbinding_) {
                x.accept(new PatternBindingVisitor<R, A>(), arg);
            }
            p.expr_.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.LessThan p, A arg) { /* Code for LessThan goes here */
            p.expr_1.accept(new ExprVisitor<R, A>(), arg);
            p.expr_2.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.LessThanOrEqual p, A arg) { /* Code for LessThanOrEqual goes here */
            p.expr_1.accept(new ExprVisitor<R, A>(), arg);
            p.expr_2.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.GreaterThan p, A arg) { /* Code for GreaterThan goes here */
            p.expr_1.accept(new ExprVisitor<R, A>(), arg);
            p.expr_2.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.GreaterThanOrEqual p, A arg) { /* Code for GreaterThanOrEqual goes here */
            p.expr_1.accept(new ExprVisitor<R, A>(), arg);
            p.expr_2.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.Equal p, A arg) { /* Code for Equal goes here */
            p.expr_1.accept(new ExprVisitor<R, A>(), arg);
            p.expr_2.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.NotEqual p, A arg) { /* Code for NotEqual goes here */
            p.expr_1.accept(new ExprVisitor<R, A>(), arg);
            p.expr_2.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.TypeAsc p, A arg) { /* Code for TypeAsc goes here */
            p.expr_.accept(new ExprVisitor<R, A>(), arg);
            p.type_.accept(new TypeVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.Abstraction p, A arg) { /* Code for Abstraction goes here */
            ListType fun_param = new ListType();
            for (org.syntax.stella.Absyn.ParamDecl x : p.listparamdecl_) {
                x.accept(new ParamDeclVisitor<R, A>(), arg);
                fun_param.add(type_tmp);
                local.add(Pair.of(name_tmp, type_tmp));
            }
            enterScope();
            global.addAll(local);
            local.clear();
            p.expr_.accept(new ExprVisitor<R, A>(), arg);
            type_tmp = new TypeFun(fun_param, type_tmp);
            exitScope();
            return null;
        }

        public R visit(org.syntax.stella.Absyn.Variant p, A arg) { /* Code for Variant goes here */
            //p.stellaident_;
            p.exprdata_.accept(new ExprDataVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.Match p, A arg) { /* Code for Match goes here */
            p.expr_.accept(new ExprVisitor<R, A>(), arg);
            for (org.syntax.stella.Absyn.MatchCase x : p.listmatchcase_) {
                x.accept(new MatchCaseVisitor<R, A>(), arg);
            }
            return null;
        }

        public R visit(org.syntax.stella.Absyn.List p, A arg) { /* Code for List goes here */
            for (org.syntax.stella.Absyn.Expr x : p.listexpr_) {
                x.accept(new ExprVisitor<R, A>(), arg);
            }
            return null;
        }

        public R visit(org.syntax.stella.Absyn.Add p, A arg) { /* Code for Add goes here */
            p.expr_1.accept(new ExprVisitor<R, A>(), arg);
            p.expr_2.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.Subtract p, A arg) { /* Code for Subtract goes here */
            p.expr_1.accept(new ExprVisitor<R, A>(), arg);
            p.expr_2.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.LogicOr p, A arg) { /* Code for LogicOr goes here */
            p.expr_1.accept(new ExprVisitor<R, A>(), arg);
            p.expr_2.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.Multiply p, A arg) { /* Code for Multiply goes here */
            p.expr_1.accept(new ExprVisitor<R, A>(), arg);
            p.expr_2.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.Divide p, A arg) { /* Code for Divide goes here */
            p.expr_1.accept(new ExprVisitor<R, A>(), arg);
            p.expr_2.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.LogicAnd p, A arg) { /* Code for LogicAnd goes here */
            p.expr_1.accept(new ExprVisitor<R, A>(), arg);
            p.expr_2.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.Application p, A arg) { /* Code for Application goes here */
            p.expr_.accept(new ExprVisitor<R, A>(), arg);
            if (type_tmp.getClass() != TypeFun.class) {
                System.out.println("Error: Not a function '" + name_tmp + "'");
                System.exit(1);
            }
            TypeFun fun = (TypeFun) type_tmp;
            ListType fun_exprs = new ListType();
            String fun_name = name_tmp;
            for (org.syntax.stella.Absyn.Expr x : p.listexpr_) {
                x.accept(new ExprVisitor<R, A>(), arg);
                fun_exprs.add(type_tmp);
            }

            // Check matching expr and parameters
            if (fun_exprs.size() != fun.listtype_.size()) {
                System.out.println("Error: Not equal sizes of expr and parameters of function '" + fun_name + "'");
                System.exit(1);
            }
            for (int i = 0; i < fun_exprs.size(); i++) {
                if (!fun_exprs.get(i).equals(fun.listtype_.get(i))) {
                    System.out.println("! " + fun_exprs + " | " + fun.listtype_);
                    System.out.println("Error: Not matching expr and parameters of function '" + fun_name + "'");
                    System.exit(1);
                }
            }
            return null;
        }

        public R visit(org.syntax.stella.Absyn.DotRecord p, A arg) { /* Code for DotRecord goes here */
            p.expr_.accept(new ExprVisitor<R, A>(), arg);
            //p.stellaident_;
            return null;
        }

        public R visit(org.syntax.stella.Absyn.DotTuple p, A arg) { /* Code for DotTuple goes here */
            p.expr_.accept(new ExprVisitor<R, A>(), arg);
            //p.integer_;
            return null;
        }

        public R visit(org.syntax.stella.Absyn.Tuple p, A arg) { /* Code for Tuple goes here */
            for (org.syntax.stella.Absyn.Expr x : p.listexpr_) {
                x.accept(new ExprVisitor<R, A>(), arg);
            }
            return null;
        }

        public R visit(org.syntax.stella.Absyn.Record p, A arg) { /* Code for Record goes here */
            for (org.syntax.stella.Absyn.Binding x : p.listbinding_) {
                x.accept(new BindingVisitor<R, A>(), arg);
            }
            return null;
        }

        public R visit(org.syntax.stella.Absyn.ConsList p, A arg) { /* Code for ConsList goes here */
            p.expr_1.accept(new ExprVisitor<R, A>(), arg);
            p.expr_2.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.Head p, A arg) { /* Code for Head goes here */
            p.expr_.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.IsEmpty p, A arg) { /* Code for IsEmpty goes here */
            p.expr_.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.Tail p, A arg) { /* Code for Tail goes here */
            p.expr_.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.Inl p, A arg) { /* Code for Inl goes here */
            p.expr_.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.Inr p, A arg) { /* Code for Inr goes here */
            p.expr_.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.Succ p, A arg) { /* Code for Succ goes here */
            p.expr_.accept(new ExprVisitor<R, A>(), arg);
            name_tmp = "succ";
            return null;
        }

        public R visit(org.syntax.stella.Absyn.LogicNot p, A arg) { /* Code for LogicNot goes here */
            p.expr_.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.Pred p, A arg) { /* Code for Pred goes here */
            p.expr_.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.IsZero p, A arg) { /* Code for IsZero goes here */
            p.expr_.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.Fix p, A arg) { /* Code for Fix goes here */
            p.expr_.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.NatRec p, A arg) { /* Code for NatRec goes here */
            p.expr_1.accept(new ExprVisitor<R, A>(), arg);
            p.expr_2.accept(new ExprVisitor<R, A>(), arg);
            p.expr_3.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.Fold p, A arg) { /* Code for Fold goes here */
            p.type_.accept(new TypeVisitor<R, A>(), arg);
            p.expr_.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.Unfold p, A arg) { /* Code for Unfold goes here */
            p.type_.accept(new TypeVisitor<R, A>(), arg);
            p.expr_.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }

        public R visit(org.syntax.stella.Absyn.ConstTrue p, A arg) { /* Code for ConstTrue goes here */
            return null;
        }

        public R visit(org.syntax.stella.Absyn.ConstFalse p, A arg) { /* Code for ConstFalse goes here */
            return null;
        }

        public R visit(org.syntax.stella.Absyn.ConstUnit p, A arg) { /* Code for ConstUnit goes here */
            return null;
        }

        public R visit(org.syntax.stella.Absyn.ConstInt p, A arg) { /* Code for ConstInt goes here */
            //p.integer_;
            return null;
        }

        public R visit(org.syntax.stella.Absyn.Var p, A arg) { /* Code for Var goes here */
            name_tmp = p.stellaident_;
            type_tmp = findFun(p.stellaident_);
            return null;
        }
    }

    public class PatternBindingVisitor<R, A> implements org.syntax.stella.Absyn.PatternBinding.Visitor<R, A> {
        public R visit(org.syntax.stella.Absyn.APatternBinding p, A arg) { /* Code for APatternBinding goes here */
            p.pattern_.accept(new PatternVisitor<R, A>(), arg);
            p.expr_.accept(new ExprVisitor<R, A>(), arg);
            return null;
        }
    }

    public class VariantFieldTypeVisitor<R, A> implements org.syntax.stella.Absyn.VariantFieldType.Visitor<R, A> {
        public R visit(org.syntax.stella.Absyn.AVariantFieldType p, A arg) { /* Code for AVariantFieldType goes here */
            //p.stellaident_;
            p.optionaltyping_.accept(new OptionalTypingVisitor<R, A>(), arg);
            return null;
        }
    }

    public class RecordFieldTypeVisitor<R, A> implements org.syntax.stella.Absyn.RecordFieldType.Visitor<R, A> {
        public R visit(org.syntax.stella.Absyn.ARecordFieldType p, A arg) { /* Code for ARecordFieldType goes here */
            //p.stellaident_;
            p.type_.accept(new TypeVisitor<R, A>(), arg);
            return null;
        }
    }

    public class TypingVisitor<R, A> implements org.syntax.stella.Absyn.Typing.Visitor<R, A> {
        public R visit(org.syntax.stella.Absyn.ATyping p, A arg) { /* Code for ATyping goes here */
            p.expr_.accept(new ExprVisitor<R, A>(), arg);
            p.type_.accept(new TypeVisitor<R, A>(), arg);
            return null;
        }
    }
}
