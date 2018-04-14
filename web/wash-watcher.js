setInterval(checkLaundryStatus, 5000);

function checkLaundryStatus() {
  var request = new XMLHttpRequest();
  request.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      document.getElementById("status").innerHTML = this.responseText;
    }
  };
  request.open("GET", "/variance", true);
  request.send();
}