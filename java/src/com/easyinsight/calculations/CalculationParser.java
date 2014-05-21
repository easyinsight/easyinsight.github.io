package com.easyinsight.calculations;

import com.easyinsight.calculations.generated.CalculationsParser;
import com.easyinsight.logging.LogClass;
import org.antlr.runtime.*;

/**
 * Created by Alan on 12/13/13.
 */
public class CalculationParser extends CalculationsParser {
    private boolean throwing;
    private String fullString;
    public CalculationParser(TokenStream input) {
        super(input);
        fullString =  null;
        throwing = false;
    }

    public CalculationParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
        throwing = false;
    }

    public CalculationParser(CommonTokenStream tokes, String s, boolean throwing) {
        super(tokes);
        this.fullString = s;
        this.throwing = throwing;
    }

    @Override
    protected Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow) throws RecognitionException {
        if(throwing) {
            throw new RecognitionException();
        }
        return super.recoverFromMismatchedToken(input, ttype, follow);
//        throw new RecognitionException();
    }
}
