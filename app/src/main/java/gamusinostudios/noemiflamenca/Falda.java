package gamusinostudios.noemiflamenca;

/**
 * Created by dani on 02/02/2018.
 */

public class Falda {

    private String Id, Color, Descripcion, Url;

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

    @Override
    public String toString() {
        return "Falda{" +
                "Id='" + Id + '\'' +
                ", Color='" + Color + '\'' +
                ", Descripcion='" + Descripcion + '\'' +
                ", Url='" + Url + '\'' +
                '}';
    }
}
