package com.easyinsight.util {

import mx.validators.ValidationResult;
import mx.validators.Validator;

public class AnalysisItemValidator extends Validator{
    public function AnalysisItemValidator() {
        super();
    }


    override protected function doValidation(value:Object):Array {
        var results:Array = [];
        if (value == null) {
            var validationResult:ValidationResult = new ValidationResult(true, null, "NoEntry", "You need to choose a valid entry.");
            results.push(validationResult);
        }
        return results;
    }
}
}