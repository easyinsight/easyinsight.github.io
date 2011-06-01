package com.easyinsight.analysis {

import mx.controls.advancedDataGridClasses.AdvancedDataGridColumn;
import mx.utils.ObjectUtil;

public class SortFunctionFactory {
    public function SortFunctionFactory() {
    }
    
    public static function createSortFunction(myHeader:AnalysisItem, sortDescending:Boolean, column:AdvancedDataGridColumn = null):Function {
            return function(obj1:Object, obj2:Object, fields:Array = null):int {
                var value1:Value;
                var value2:Value;
                var valueObj1:Object = obj1[myHeader.qualifiedName()];
                var valueObj2:Object = obj2[myHeader.qualifiedName()];
                if (valueObj1 is Value) {
                    value1 = valueObj1 as Value;
                } else if (valueObj1 is Number) {
                    value1 = new NumericValue();
                    NumericValue(value1).value = valueObj1 as Number;
                } else if (valueObj1 is String) {
                    value1 = new StringValue();
                    StringValue(value1).value = valueObj1 as String;
                } else if (valueObj1 is Date) {
                    value1 = new DateValue();
                    DateValue(value1).cachedDate = valueObj1 as Date;
                }
                if (valueObj2 is Value) {
                    value2 = valueObj2 as Value;
                } else if (valueObj2 is Number) {
                    value2 = new NumericValue();
                    NumericValue(value2).value = valueObj2 as Number;
                } else if (valueObj2 is String) {
                    value2 = new StringValue();
                    StringValue(value2).value = valueObj2 as String;
                } else if (valueObj2 is Date) {
                    value2 = new DateValue();
                    DateValue(value2).cachedDate = valueObj2 as Date;
                }

                if (value1 == null) {
                    value1 = new EmptyValue();
                }
                if (value2 == null) {
                    value2 = new EmptyValue();
                }

                if (value1.summary) {
                    if ((column != null && column.sortDescending) || sortDescending) {
                        return -1;
                    } else {
                        return 1;
                    }
                }

                if (value2.summary) {
                    if ((column != null && column.sortDescending) || sortDescending) {
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
                if (value2.type() == Value.EMPTY || value2.toSortValue().type() == Value.EMPTY) {
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