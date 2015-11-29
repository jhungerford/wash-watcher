$(function() {
  var data = {
    labels: [],
    datasets: [
        {
            label: "My Second dataset",
            fillColor: "rgba(151,187,205,0.2)",
            strokeColor: "rgba(151,187,205,1)",
            pointColor: "rgba(151,187,205,1)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(151,187,205,1)",
            data: []
        }
    ]
  };

  var options = {
    animation: false
  };

  var ctx = document.getElementById("magnitude-chart-canvas").getContext("2d");

  var lineChart = new Chart(ctx).Line(data, options);

  var lastFetch = new Date().getTime()
  var numInGraph = 0

  window.setInterval(function() {
    startFetch = new Date().getTime()
    $.get("/api/magnitudes/since/" + lastFetch)
      .done(function(data) {
        console.log(data);
        lineChart.addData([data.value], "");

        if (numInGraph >= 15) {
          lineChart.removeData();
        } else {
          numInGraph ++;
        }

        lastFetch = startFetch;
      });
  }, 1000);
});
