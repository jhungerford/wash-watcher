defmodule PhoenixServer.Repo.Migrations.CreateSensor do
  use Ecto.Migration

  def change do
    create table(:sensors) do
      add :timestamp, :bigint
      add :device, :string
      add :x, :float
      add :y, :float
      add :z, :float

      timestamps
    end

  end
end
