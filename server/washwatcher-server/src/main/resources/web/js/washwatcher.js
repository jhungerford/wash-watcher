$(function() {
    var everySeconds = 1;
    var totalSeconds = 30;

    var everyMS = (everySeconds*1000);
    var numReadings = totalSeconds / everySeconds;

    var now = new Date().getTime();
    var toMS = Math.floor(now / (everyMS)) * everyMS;
    var fromMS = toMS - (numReadings + 1) * everyMS;

    var ctx = document.getElementById("magnitudeChart").getContext("2d");

    var emptyMagnitudes = new Array(numReadings + 1).map(Number.prototype.valueOf, 0);
    var emptyLabels = Array.apply(null, new Array(numReadings + 1)).map(function(_, i) {
        return formatTimestamp(toMS - (numReadings - i) * everyMS);
    });

    var chartData = {
        labels: emptyLabels,
        datasets: [{
            label: "Magnitudes",
            fillColor: "rgba(151,187,205,0.2)",
            strokeColor: "rgba(151,187,205,1)",
            pointColor: "rgba(151,187,205,1)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(151,187,205,1)",
            data: emptyMagnitudes
        }]
    };

    var options = {
        animation: false
    };

    var lineChart = new Chart(ctx).Line(chartData, options);

    function formatTimestamp(timestamp) {
        return new Date(timestamp).toLocaleTimeString();
    }

    function refreshData() {
        var url = "/api/v1/sensor/magnitudes?every_ms=" + everyMS + "&from_ms=" + fromMS + '&to_ms=' + toMS;
        $.get(url).done(function(data) {
            data.forEach(function (reading) {
                lineChart.addData([reading['magnitude']], formatTimestamp(reading['when']));
                lineChart.removeData();
            }, this);

            fromMS = toMS;
            toMS = toMS + everyMS;
        });
    }

    window.setInterval(refreshData, everyMS);
    refreshData();
});
