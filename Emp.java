package table;
import annotation.*;

@TableName(nom_table = "Employe")
public class Emp {
    @FieldName(columnName = "id_emp")
    String id;

    @FieldName(columnName = "name_emp")
    String name;

    String notin;
}