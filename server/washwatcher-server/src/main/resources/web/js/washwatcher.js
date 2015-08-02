$(function() {
  $.get("/api/v1/sensor/magnitudes?everySeconds=5")
    .done(function(data) {
      var labels = data.map(function(reading) {
        return reading['when'];
      });

      var magnitudes = data.map(function(reading) {
        return reading['magnitude'];
      });

      var chartData = {
        labels: labels,
        datasets: [{
          label: "Magnitudes",
          fillColor: "rgba(151,187,205,0.2)",
          strokeColor: "rgba(151,187,205,1)",
          pointColor: "rgba(151,187,205,1)",
          pointStrokeColor: "#fff",
          pointHighlightFill: "#fff",
          pointHighlightStroke: "rgba(151,187,205,1)",
          data: magnitudes
        }]
      };

      var options = {};
      var ctx = document.getElementById("magnitudeChart").getContext("2d");
      var chart = new Chart(ctx);
      chart.Line(chartData, options);
    });
});