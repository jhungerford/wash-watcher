defmodule PhoenixServer.PageController do
  use PhoenixServer.Web, :controller

  def index(conn, _params) do
    render conn, "index.html"
  end
end
