package gamusinostudios.noemiflamenca;

/*
 * Created by Dani on 31/01/2018.
 */

public class Vestido {

    private String Id, Color, Descripcion, Url, Precio;

    public String getId() {
        return Id;
    }

    public String getColor() {
        return Color;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public String getUrl() {
        return Url;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setColor(String color) {
        Color = color;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }

    @Override
    public String toString() {
        return "Vestido{" +
                "Id='" + Id + '\'' +
                ", Color='" + Color + '\'' +
                ", Descripcion='" + Descripcion + '\'' +
                ", Url='" + Url + '\'' +
                ", Precio='" + Precio + '\'' +
                '}';
    }
}
