package com.easyinsight.calculations;

import com.easyinsight.calculations.generated.CalculationsParser;
import org.antlr.runtime.*;

/**
 * Created by Alan on 12/13/13.
 */
public class CalculationParser extends CalculationsParser {
    public CalculationParser(TokenStream input) {
        super(input);
    }

    public CalculationParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

    @Override
    protected Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow) throws RecognitionException {
        throw new RecognitionException();
    }
}
