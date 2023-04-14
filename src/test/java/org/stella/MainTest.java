package org.stella;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.stella.typecheck.VisitTypeCheck;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;

class MainTest {

    @ParameterizedTest(name = "{index} Typechecking well-typed program {0}")
    @ValueSource(strings = {
            "tests/core/well-typed/abstract-function.stella",
            "tests/core/well-typed/added-test-1.stella",
            "tests/core/well-typed/added-test-2.stella",
            "tests/core/well-typed/apply-increase.stella",
            "tests/core/well-typed/applying-actual-function-3.stella",
            "tests/core/well-typed/bool-to-nat.stella",
            "tests/core/well-typed/cubes.stella",
            "tests/core/well-typed/decrement_twice.stella",
            "tests/core/well-typed/double-application.stella",
            "tests/core/well-typed/factorial.stella",
            "tests/core/well-typed/good-if.stella",
            "tests/core/well-typed/good-if-2.stella",
            "tests/core/well-typed/good-succ-1.stella",
            "tests/core/well-typed/good-succ-2.stella",
            "tests/core/well-typed/higher-order-1.stella",
            "tests/core/well-typed/higher-order-2.stella",
            "tests/core/well-typed/increment-triple.stella",
            "tests/core/well-typed/increment_twice.stella",
            "tests/core/well-typed/inner-if.stella",
            "tests/core/well-typed/logical-operators.stella",
            "tests/core/well-typed/many-if.stella",
            "tests/core/well-typed/my-good-if.stella",
            "tests/core/well-typed/my-good-succ.stella",
            "tests/core/well-typed/my-test.stella",
            "tests/core/well-typed/my-test-2.stella",
            "tests/core/well-typed/my-well-typed-1.stella",
            "tests/core/well-typed/my-well-typed-2.stella",
            "tests/core/well-typed/nat-to-bool.stella",
            "tests/core/well-typed/negate.stella",
            "tests/core/well-typed/nested.stella",
            "tests/core/well-typed/shadowed-variable-2.stella",
            "tests/core/well-typed/simple-if.stella",
            "tests/core/well-typed/simple-succ.stella",
            "tests/core/well-typed/simple-types.stella",
            "tests/core/well-typed/squares.stella",
            "tests/core/well-typed/succ-with-func.stella"})
    public void testWellTyped(String filepath) throws IOException, Exception {
        String[] args = new String[0];
        final InputStream original = System.in;
        final FileInputStream fips = new FileInputStream(new File(filepath));
        System.setIn(fips);
        Main.main(args);
        System.setIn(original);
    }

    @ParameterizedTest(name = "{index} Typechecking ill-typed program {0}")
    @ValueSource(strings = {
            "tests/core/ill-typed/added-test-1.stella",
            "tests/core/ill-typed/added-test-2.stella",
            "tests/core/ill-typed/application-param-type.stella",
            "tests/core/ill-typed/applying-non-function-1.stella",
            "tests/core/ill-typed/applying-non-function-2.stella",
            "tests/core/ill-typed/applying-non-function-3.stella",
            "tests/core/ill-typed/argument-type-mismatch-1.stella",
            "tests/core/ill-typed/argument-type-mismatch-2.stella",
            "tests/core/ill-typed/argument-type-mismatch-3.stella",
            "tests/core/ill-typed/argument-type-mismatch-4.stella",
            "tests/core/ill-typed/bad-abstraction.stella",
            "tests/core/ill-typed/bad-add-1.stella",
            "tests/core/ill-typed/bad-cmp-1.stella",
            "tests/core/ill-typed/bad-cmp-2.stella",
            "tests/core/ill-typed/bad-cmp-3.stella",
            "tests/core/ill-typed/bad-cmp-4.stella",
            "tests/core/ill-typed/bad-cmp-5.stella",
            "tests/core/ill-typed/bad-cmp-6.stella",
            "tests/core/ill-typed/bad-factorial.stella",
            "tests/core/ill-typed/bad-factorial-1.stella",
            "tests/core/ill-typed/bad-factorial-2.stella",
            "tests/core/ill-typed/bad-function-call.stella",
            "tests/core/ill-typed/bad-higher-order-1.stella",
            "tests/core/ill-typed/bad-if-1.stella",
            "tests/core/ill-typed/bad-if-2.stella",
            "tests/core/ill-typed/bad-if-3.stella",
            "tests/core/ill-typed/bad-if-4.stella",
            "tests/core/ill-typed/bad-if-and-undefined-variable-1.stella",
            "tests/core/ill-typed/bad-iszero.stella",
            "tests/core/ill-typed/bad-logic-and-1.stella",
            "tests/core/ill-typed/bad-logic-not-1.stella",
            "tests/core/ill-typed/bad-logic-or-1.stella",
            "tests/core/ill-typed/bad-multiply-1.stella",
            "tests/core/ill-typed/bad-nat-1.stella",
            "tests/core/ill-typed/bad-nat-2.stella",
            "tests/core/ill-typed/bad-nat-rec-1.stella",
            "tests/core/ill-typed/bad-nat-rec-2.stella",
            "tests/core/ill-typed/bad-return-type.stella",
            "tests/core/ill-typed/bad-squares-1.stella",
            "tests/core/ill-typed/bad-squares-2.stella",
            "tests/core/ill-typed/bad-succ-1.stella",
            "tests/core/ill-typed/bad-succ-2.stella",
            "tests/core/ill-typed/bad-succ-3.stella",
            "tests/core/ill-typed/function-mismatch.stella",
            "tests/core/ill-typed/invalid-nat.stella",
            "tests/core/ill-typed/invalid-not_.stella",
            "tests/core/ill-typed/my-factorial.stella",
            "tests/core/ill-typed/my-ill-test-2.stella",
            "tests/core/ill-typed/my-ill-typed-1.stella",
            "tests/core/ill-typed/my-ill-typed-2.stella",
            "tests/core/ill-typed/my-mismatch.stella",
            "tests/core/ill-typed/nat__rec-parameters.stella",
            "tests/core/ill-typed/shadowed-variable-1.stella",
            "tests/core/ill-typed/shadowed-variable-2.stella",
            "tests/core/ill-typed/undefined-variable-1.stella",
            "tests/core/ill-typed/undefined-variable-2.stella",
            "tests/core/ill-typed/undefined-variable-3.stella",
    })
    public void testIllTyped(String filepath) throws IOException, Exception {
        String[] args = new String[0];
        final InputStream original = System.in;
        final FileInputStream fips = new FileInputStream(new File(filepath));
        System.setIn(fips);

        boolean typecheckerFailed = false;
        try {
            Main.main(args); // TODO: check that if it fail then there is a type error actually, and not a problem with implementation
        } catch (VisitTypeCheck.TypeError e) {
            System.out.println("Type Error: " + e.getMessage());
            typecheckerFailed = true;
        }
        if (!typecheckerFailed) {
            throw new Exception("expected the typechecker to fail!");
        }
        // System.setIn(original); // dead code
    }
}