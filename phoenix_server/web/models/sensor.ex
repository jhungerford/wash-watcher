defmodule PhoenixServer.Sensor do
  use PhoenixServer.Web, :model

  schema "sensors" do
    field :timestamp, :integer
    field :device, :string
    field :x, :float
    field :y, :float
    field :z, :float

    timestamps
  end

  @required_fields ~w(timestamp device x y z)
  @optional_fields ~w()

  @doc """
  Creates a changeset based on the `model` and `params`.

  If no params are provided, an invalid changeset is returned
  with no validation performed.
  """
  def changeset(model, params \\ :empty) do
    model
    |> cast(params, @required_fields, @optional_fields)
  end
end
