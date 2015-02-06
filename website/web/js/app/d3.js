(function() {
    var d3Module = angular.module("angulard3", []);

    d3Module.directive("columnChart", [function() {
        return {
            restrict: "A",
            require: "ngModel",
            link: function(scope, element, attrs, ngModel) {

                scope.$watch(
                    function(){
                        return ngModel.$modelValue;
                    }, function(newValue, oldValue){
                        nv.addGraph({
                            generate: function() {
                                var chart = nv.models.discreteBarChart()
                                    .transitionDuration(350)  //how fast do you want the lines to transition?
                                    .tooltipContent(function (key, x, y, e, graph) {
                                        return '<b>' + x + '</b>' +
                                            '<p>' + y + '</p>'
                                    })
                                    .showYAxis(true)        //Show the y-axis
                                    .showXAxis(true)        //Show the x-axis
                                d3.select(element[0]).datum(newValue).call(chart);
                                return chart;
                            }
                        });
                    }, true);

            }
        }
    }])
}());