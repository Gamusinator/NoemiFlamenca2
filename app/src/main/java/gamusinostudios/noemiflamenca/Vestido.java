package gamusinostudios.noemiflamenca;

/*
 * Created by Dani on 31/01/2018.
 */

public class Vestido {
    private int Id;
    private String Color, Descripcion, Url;

    public int getId() {
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

    public void setId(int id) {
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

    @Override
    public String toString() {
        return "Vestido{" +
                "Id=" + Id +
                ", Color='" + Color + '\'' +
                ", Descripcion='" + Descripcion + '\'' +
                ", Url='" + Url + '\'' +
                '}';
    }
}
