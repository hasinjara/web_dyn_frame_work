package table;

import annotation.*;
import modelview.*;
import fileUpload.*;


@TableName(nom_table = "Departement")
@Scope(value = "singleton")
public class Dept {
    @FieldName(columnName = "id_dept")
    String id;

    @FieldName(columnName = "name_dept")
    String name;

    String notin;
    FileUpload File;
    
    
    public FileUpload getFile() {
        return File;
    }
    public void setFile(FileUpload file) {
        this.File = file;
    }
    public String getNamePath(){
        return File.getPath() ;
    }
    

    public Dept() {
    }

    public Dept(String id, String name, String notin) {
        this.id = id;
        this.name = name;
        this.notin = notin;
    }

    @Auth(profil = "profil")
    @MethodUrl(url = "Dept-lister")
    public ModelView  deptList() throws Exception {
        ModelView mv = new ModelView();
        try {
            mv.setView("Dept.jsp");
            Dept[] list = new Dept[3];
            for (int i = 0; i < list.length; i++) {
                list[i]=new Dept("DD","dept","no");
            }
            mv.addItem("list", list);
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    return mv;
       
    }

    @Session()
    @MethodUrl(url = "Dept-add")
    public ModelView  form() throws Exception {
        ModelView mv = new ModelView();
        try {
            mv.setView("Form.jsp");
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    return mv;
    }

    @MethodUrl(url = "Dept-save")
    public ModelView  save() throws Exception {
        ModelView mv = new ModelView();
        try {
            mv.setView("save.jsp");
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    return mv;
    }

    @MethodUrl(url = "Dept-login")
    public ModelView  login() throws Exception {
        ModelView mv = new ModelView();
        try {
            mv.addSession("profil", "profil");
            mv.addSession("connected", true);
            mv.setView("login.jsp");
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    return mv;
    }

    
    @MethodUrl(url = "Dept-Id")
    public ModelView  FindById(@ParamName("nom") String nom, @ParamName("numero")int numero) throws Exception {
        ModelView mv = new ModelView();
        try {
            mv.setView("Form.jsp");
            System.out.println("nom "+ nom + " num " + numero);
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
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
