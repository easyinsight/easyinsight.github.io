var reportReady = false;

var filterBase = {};

function updateFilter(name, key, refreshFunction) {
    var optionMenu = document.getElementById(name);
    var chosenOption = optionMenu.options[optionMenu.selectedIndex];
    var keyedFilter = filterBase[key];
    if (keyedFilter == null) {
        keyedFilter = {};
        filterBase[key] = keyedFilter;
    }
    keyedFilter[name] = chosenOption.value;
    if (reportReady) {
        refreshFunction();
    }
}

function updateRangeFilter(key, refreshFunction) {
    var keyedFilter = filterBase[key];
    if (keyedFilter == null) {
        keyedFilter = {};
        filterBase[key] = keyedFilter;
    }
    keyedFilter[key + "start"] = document.getElementById(key + 'start').value;
    keyedFilter[key + "end"] = document.getElementById(key + 'end').value;
    if (reportReady) {
        refreshFunction();
    }
}

function updateRollingFilter(name, key, refreshFunction) {
    var optionMenu = document.getElementById(name);
    var chosenOption = optionMenu.value;
    var keyedFilter = filterBase[key];
    if (keyedFilter == null) {
        keyedFilter = {};
        filterBase[key] = keyedFilter;
    }
    if (chosenOption == '18') {
        keyedFilter[name + "direction"] = document.getElementById('customDirection' + name).value;
        keyedFilter[name + "value"] = document.getElementById('customValue' + name).value;
        keyedFilter[name + "interval"] = document.getElementById('customInterval' + name).value;
    }
    keyedFilter[name] = chosenOption;
    if (reportReady) {
        refreshFunction();
    }
}

function updateMultiMonth(name, key, refreshFunction) {
    var startName = name + "start";
    var endName = name + "end";
    var startMonth = $("#"+startName).val();
    var endMonth = $("#"+endName).val();
    var keyedFilter = filterBase[key];
    if (keyedFilter == null) {
        keyedFilter = {};
        filterBase[key] = keyedFilter;
    }
    keyedFilter[name + "start"] = startMonth;
    keyedFilter[name + "end"] = endMonth;
    if (reportReady) {
        refreshFunction();
    }
}

function updateMultiFilter(name, key, refreshFunction) {
    var keyedFilter = filterBase[key];
    if (keyedFilter == null) {
        keyedFilter = {};
        filterBase[key] = keyedFilter;
    }
    var selects = $("#"+name).val();
    keyedFilter[name] = selects;
    if (reportReady) {
        refreshFunction();
    }
}

function filterEnable(name, key, refreshFunction) {
    var keyedFilter = filterBase[key];
    if (keyedFilter == null) {
        keyedFilter = {};
        filterBase[key] = keyedFilter;
    }
    keyedFilter[name + "enabled"] = document.getElementById(name + 'enabled').checked;

    if (reportReady) {
        refreshFunction();
    }
}