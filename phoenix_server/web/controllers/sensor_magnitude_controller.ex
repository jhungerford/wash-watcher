defmodule PhoenixServer.SensorMagnitudeController do
  use PhoenixServer.Web, :controller

  def since(conn, %{"timestamp" => timestamp}) do
    query = from s in PhoenixServer.Sensor,
      where: s.timestamp > ^timestamp,
      select: s
    sensors = Repo.all(query)

    json conn, %{num: length(sensors)}
  end
end
