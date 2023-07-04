os :
tena tsy mety mihitsy vao tsy windows de tsy mahazo ampiasa IDE toy ny netbeans sy IntelliJ

import :

import annotation.*;
import modelview.*;


Annotation :
@TableName(nom_table = "Departement")
@FieldName(columnName = "id_dept")
@MethodUrl(url = "Dept-lister") => tsy maintsy mireturn Model View raha hanao anio

exemple utilisation ModelView
setView() : le jsp tiana hipoitra --> tena tonga tsy mandeha mihitsy vao tsy atao .jsp ary le .jsp tena tsy maintsy misy
addItem(string cle, Object Valeur) 
tsy maintsy atao anaty try catch
@MethodUrl(url = "Dept-lister")
    public ModelView  deptList() throws Exception {
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
            throw e;
        }
    return mv;
       
   }

. tena tonga dia tsy mandeha vao tsy misu getter su setter

. mbola tsy mahazaka date aloha atreto