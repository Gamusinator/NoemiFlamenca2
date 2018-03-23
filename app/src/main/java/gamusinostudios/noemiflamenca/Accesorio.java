package gamusinostudios.noemiflamenca;

/**
 * Created by dani on 02/02/2018.
 */

public class Accesorio {

    private String Id, Color, Descripcion, Url, Precio;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }

    @Override
    public String toString() {
        return "Accesorio{" +
                "Id='" + Id + '\'' +
                ", Color='" + Color + '\'' +
                ", Descripcion='" + Descripcion + '\'' +
                ", Url='" + Url + '\'' +
                ", Precio='" + Precio + '\'' +
                '}';
    }
}
