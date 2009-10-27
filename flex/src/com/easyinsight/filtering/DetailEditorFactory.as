package com.easyinsight.filtering {
public class DetailEditorFactory {
    public function DetailEditorFactory() {
    }

    public static function getDetailEditorClass(filter:FilterDefinition):Class {
        if (filter is FilterValueDefinition) {
            return MultiValueFilterWindow;
        } else if (filter is FilterPatternDefinition) {
            return PatternFilterWindow;
        } else {
            return null;
        }
    }
}
}