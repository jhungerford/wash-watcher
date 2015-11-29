defmodule PhoenixServer.SensorMagnitudeController do
  use PhoenixServer.Web, :controller

  def since(conn, %{"timestamp" => timestamp}) do
    query = from s in PhoenixServer.Sensor,
      where: s.timestamp > ^timestamp,
      select: s

    sensors = Repo.all(query)

    num = length(sensors)
    avg = List.foldl(sensors, 0, fn (sensor, acc) -> acc + magnitude(sensor) end) / num

    json conn, %{num: num, avg: avg}
  end

  def magnitude(sensor) do
    :math.sqrt(:math.pow(sensor.x, 2) + :math.pow(sensor.y, 2) + :math.pow(sensor.z, 2))
  end
end
