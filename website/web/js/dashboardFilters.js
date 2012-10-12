var filters;
var filterDiv;

function addFilter(filterName) {
    $.get('/app/filter', function(data) {
        $(filterDiv).append(data);
    });
}