defmodule PhoenixServer.SensorControllerTest do
  use PhoenixServer.ConnCase

  alias PhoenixServer.Sensor
  @valid_attrs %{device: "some content", timestamp: 42, x: "120.5", y: "120.5", z: "120.5"}
  @invalid_attrs %{}

  setup do
    conn = conn() |> put_req_header("accept", "application/json")
    {:ok, conn: conn}
  end

  test "lists all entries on index", %{conn: conn} do
    conn = get conn, sensor_path(conn, :index)
    assert json_response(conn, 200)["data"] == []
  end

  test "shows chosen resource", %{conn: conn} do
    sensor = Repo.insert! %Sensor{}
    conn = get conn, sensor_path(conn, :show, sensor)
    assert json_response(conn, 200)["data"] == %{"id" => sensor.id,
      "timestamp" => sensor.timestamp,
      "device" => sensor.device,
      "x" => sensor.x,
      "y" => sensor.y,
      "z" => sensor.z}
  end

  test "does not show resource and instead throw error when id is nonexistent", %{conn: conn} do
    assert_raise Ecto.NoResultsError, fn ->
      get conn, sensor_path(conn, :show, -1)
    end
  end

  test "creates and renders resource when data is valid", %{conn: conn} do
    conn = post conn, sensor_path(conn, :create), sensor: @valid_attrs
    assert json_response(conn, 201)["data"]["id"]
    assert Repo.get_by(Sensor, @valid_attrs)
  end

  test "does not create resource and renders errors when data is invalid", %{conn: conn} do
    conn = post conn, sensor_path(conn, :create), sensor: @invalid_attrs
    assert json_response(conn, 422)["errors"] != %{}
  end

  test "updates and renders chosen resource when data is valid", %{conn: conn} do
    sensor = Repo.insert! %Sensor{}
    conn = put conn, sensor_path(conn, :update, sensor), sensor: @valid_attrs
    assert json_response(conn, 200)["data"]["id"]
    assert Repo.get_by(Sensor, @valid_attrs)
  end

  test "does not update chosen resource and renders errors when data is invalid", %{conn: conn} do
    sensor = Repo.insert! %Sensor{}
    conn = put conn, sensor_path(conn, :update, sensor), sensor: @invalid_attrs
    assert json_response(conn, 422)["errors"] != %{}
  end

  test "deletes chosen resource", %{conn: conn} do
    sensor = Repo.insert! %Sensor{}
    conn = delete conn, sensor_path(conn, :delete, sensor)
    assert response(conn, 204)
    refute Repo.get(Sensor, sensor.id)
  end
end
