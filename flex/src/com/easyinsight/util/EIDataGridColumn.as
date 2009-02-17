package com.easyinsight.util {
import mx.controls.dataGridClasses.DataGridColumn;
public class EIDataGridColumn extends DataGridColumn {

    private var headerFactory:EICustomHeaderFactory = new EICustomHeaderFactory();

    public function EIDataGridColumn() {
        super();
        this.headerRenderer = headerFactory; 
    }
}
}