package table;
import annotation.*;
import modelview.*;


@TableName(nom_table = "Departement")
public class Dept {
    @FieldName(columnName = "id_dept")
    String id;

    @FieldName(columnName = "name_dept")
    String name;

    String notin;
    

    public Dept() {
    }

    public Dept(String id, String name, String notin) {
        this.id = id;
        this.name = name;
        this.notin = notin;
    }

    @MethodUrl(url = "Dept-list")
    public ModelView  deptList() {
        ModelView mv = new ModelView();
        try {
            mv.setView("Dept.jsp");
            Dept[] list = new Dept[3];
            for (int i = 0; i < list.length; i++) {
                list[i] = new Dept("DD", "dept", "no");
            }
            mv.addItem("list", list);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        
    return mv;
       
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

    public String getId() {
        return id;
    }


}
