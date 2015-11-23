$(function() {
  var data = {
    labels: ["January", "February", "March", "April", "May", "June", "July"],
    datasets: [
        {
            label: "My First dataset",
            fillColor: "rgba(220,220,220,0.2)",
            strokeColor: "rgba(220,220,220,1)",
            pointColor: "rgba(220,220,220,1)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(220,220,220,1)",
            data: [65, 59, 80, 81, 56, 55, 40]
        },
        {
            label: "My Second dataset",
            fillColor: "rgba(151,187,205,0.2)",
            strokeColor: "rgba(151,187,205,1)",
            pointColor: "rgba(151,187,205,1)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(151,187,205,1)",
            data: [28, 48, 40, 19, 86, 27, 90]
        }
    ]
  };

  var options = {
    animation: false
  };

  var ctx = document.getElementById("magnitude-chart-canvas").getContext("2d");

  var lineChart = new Chart(ctx).Line(data, options);

  window.setInterval(function() {
    lineChart.addData([parseInt(Math.random()*40 + 20), parseInt(Math.random()*40 + 20)], "month");
    lineChart.removeData();
  }, 1000);

  // $.get("/api/v1/sensor/magnitudes?everySeconds=5")
  //   .done(function(data) {
  //     var labels = data.map(function(reading) {
  //       return reading['when'];
  //     });
  //
  //     var magnitudes = data.map(function(reading) {
  //       return reading['magnitude'];
  //     });
  //
  //     var chartData = {
  //       labels: labels,
  //       datasets: [{
  //         label: "Magnitudes",
  //         fillColor: "rgba(151,187,205,0.2)",
  //         strokeColor: "rgba(151,187,205,1)",
  //         pointColor: "rgba(151,187,205,1)",
  //         pointStrokeColor: "#fff",
  //         pointHighlightFill: "#fff",
  //         pointHighlightStroke: "rgba(151,187,205,1)",
  //         data: magnitudes
  //       }]
  //     };
  //
  //     var options = {
  //       animation: false
  //     };
  //     var ctx = document.getElementById("magnitude-chart-canvas").getContext("2d");
  //     var chart = new Chart(ctx);
  //     chart.Line(chartData, options);
  //   });
});
