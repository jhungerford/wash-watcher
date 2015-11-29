defmodule PhoenixServer.SensorView do
  use PhoenixServer.Web, :view

  def render("index.json", %{sensors: sensors}) do
    %{data: render_many(sensors, PhoenixServer.SensorView, "sensor.json")}
  end

  def render("show.json", %{sensor: sensor}) do
    %{data: render_one(sensor, PhoenixServer.SensorView, "sensor.json")}
  end

  def render("sensor.json", %{sensor: sensor}) do
    %{id: sensor.id,
      timestamp: sensor.timestamp,
      device: sensor.device,
      x: sensor.x,
      y: sensor.y,
      z: sensor.z}
  end
end
