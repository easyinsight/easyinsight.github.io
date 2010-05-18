package com.easyinsight.analysis {
import mx.utils.ObjectUtil;

public class SortFunctionFactory {
    public function SortFunctionFactory() {
    }
    
    public static function createSortFunction(myHeader:AnalysisItem, sortDescending:Boolean):Function {
            return function(obj1:Object, obj2:Object, fields:Array = null):int {

                var value1:Value = obj1[myHeader.qualifiedName()];
                var value2:Value = obj2[myHeader.qualifiedName()];

                if (value1 == null) {
                    value1 = new EmptyValue();
                }
                if (value2 == null) {
                    value2 = new EmptyValue();
                }

                if (value1.summary) {
                    if (sortDescending) {
                        return -1;
                    } else {
                        return 1;
                    }
                }

                if (value2.summary) {
                    if (sortDescending) {
                        return 1;
                    } else {
                        return -1;
                    }
                }

                if (value1.toSortValue().type() == Value.EMPTY && value2.toSortValue().type() == Value.EMPTY) {
                    return 0;
                }
                if (value1.type() == Value.EMPTY) {

                    if (sortDescending) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
                if (value2.type == Value.EMPTY || value2.toSortValue().type() == Value.EMPTY) {
                    if (sortDescending) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
                if (value1.toSortValue().type() == Value.NUMBER && value2.toSortValue().type() == Value.NUMBER) {
                    var number1:Number = value1.toSortValue().toNumber();
                    if (isNaN(number1) || !isFinite(number1)) {
                        if (sortDescending) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }

                    var number2:Number = value2.toSortValue().toNumber();
                    if (isNaN(number2) || !isFinite(number2)) {
                        if (sortDescending) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                    if (number1 < number2) {
                        return -1;
                    } else if (number1 > number2) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
                if (value1.toSortValue().type() == Value.STRING && value2.toSortValue().type() == Value.STRING) {
                    var string1:String = value1.toSortValue().toString();
                    var string2:String = value2.toSortValue().toString();
                    return ObjectUtil.stringCompare(string1, string2, true);
                }
                if (value1.toSortValue().type() == Value.DATE && value2.toSortValue().type() == Value.DATE) {
                    var date1:Date = value1.toSortValue().toDate();
                    var date2:Date = value2.toSortValue().toDate();
                    return ObjectUtil.dateCompare(date1, date2);
                }
                var strCopy1:String = String(value1.toSortValue().getValue());
                var strCopy2:String = String(value2.toSortValue().getValue());
                return ObjectUtil.stringCompare(strCopy1, strCopy2, true);
                //return Number(value1.getValue()) - Number(value2.getValue());
            };
        }
}
}