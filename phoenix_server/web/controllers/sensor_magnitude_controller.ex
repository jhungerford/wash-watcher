defmodule PhoenixServer.SensorMagnitudeController do
  use PhoenixServer.Web, :controller

  def since(conn, %{"timestamp" => timestamp}) do
    query = from s in PhoenixServer.Sensor,
      where: s.timestamp > ^timestamp,
      select: s

    sensors = Repo.all(query)

    num = length(sensors)
    value = case num do
      0 -> 0
      n -> List.foldl(sensors, 0, fn (sensor, acc) ->
        diff = gravityDifference(magnitude(sensor))

        if diff > acc, do: diff, else: acc
      end)
    end

    json conn, %{num: num, value: value}
  end

  def gravityDifference(magnitude) do
    abs(magnitude - 9.8)
  end

  def magnitude(sensor) do
    :math.sqrt(:math.pow(sensor.x, 2) + :math.pow(sensor.y, 2) + :math.pow(sensor.z, 2))
  end
end
