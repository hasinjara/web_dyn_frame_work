package table;
import annotation.*;

@TableName(nom_table = "Departement")
public class Dept {
    @FieldName(columnName = "id_dept")
    String id;

    @FieldName(columnName = "name_dept")
    String name;

    String notin;

    @MethodUrl(url = "Dept-getId")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotin() {
        return notin;
    }

    public void setNotin(String notin) {
        this.notin = notin;
    }


}
