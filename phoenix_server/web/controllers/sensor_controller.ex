defmodule PhoenixServer.SensorController do
  use PhoenixServer.Web, :controller

  def random(conn, _params) do
    data = Stream.repeatedly( fn -> :random.uniform end )
      |> Enum.take(3)
    timestamp = :os.system_time(:milli_seconds)

    json conn, %{status: "success", timestamp: timestamp, data: data}
  end
end
