defmodule PhoenixServer.SensorServer do
  use GenServer

  def start_link do
    GenServer.start_link(__MODULE__, [], [name: :sensor_server])
  end

  def handle_call(:random, _sender, status) do
    # data = Stream.repeatedly( fn -> :random.uniform end )
    #   |> Enum.take(3)
    # timestamp = :os.system_time(:milli_seconds)
    #
    # timestamp_data = [timestamp] ++ Enum.to_list(data)
    {:reply, [1, 2, 3, 4], status}
  end

end
