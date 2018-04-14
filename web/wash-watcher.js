setInterval(checkLaundryStatus, 5000);
let done = true;

function isDone(variance) {
  return variance > 1000000;
}

function setDone() {
  document.getElementById("status").innerHTML = "Done!";
  document.getElementById("body").classList.add("done");
  document.getElementById("body").classList.remove("running");
}

function setRunning() {
  document.getElementById("status").innerHTML = "Running...";
  document.getElementById("body").classList.add("running");
  document.getElementById("body").classList.remove("done");
}

function checkLaundryStatus() {
  const request = new XMLHttpRequest();
  request.onreadystatechange = function() {
    if (this.readyState === 4 && this.status === 200) {
      const variance = Number(this.responseText);
      const newDone = isDone(variance);

      // Only update the page if the status changed
      if (newDone !== done) {
        done = newDone;
        if (newDone) {
          // Switched to running
          setDone();
        } else {
          // Switched to done
          setRunning();
        }
      }
    }
  };
  request.open("GET", "/variance", true);
  request.send();
}