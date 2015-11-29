defmodule PhoenixServer.SensorTest do
  use PhoenixServer.ModelCase

  alias PhoenixServer.Sensor

  @valid_attrs %{device: "some content", timestamp: 42, x: "120.5", y: "120.5", z: "120.5"}
  @invalid_attrs %{}

  test "changeset with valid attributes" do
    changeset = Sensor.changeset(%Sensor{}, @valid_attrs)
    assert changeset.valid?
  end

  test "changeset with invalid attributes" do
    changeset = Sensor.changeset(%Sensor{}, @invalid_attrs)
    refute changeset.valid?
  end
end
