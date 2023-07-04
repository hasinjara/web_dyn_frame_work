package modelview;

import java.util.Vector;
import java.util.Map;
import java.util.Objects;
import java.util.HashMap;
import java.lang.ClassLoader;

public class ModelView {
    String view;
    HashMap <String, Object> data;


    public ModelView() {
        setData();
    }

    public ModelView(String view) {
        try {
            setView(view);
            setData();
        } catch (Exception e) {
            // TODO: handle exception
            // throw e;
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private ModelView(String view, HashMap <String, Object> data) {
        try {
            setView(view);
            setData(data);
        } catch (Exception e) {
            // TODO: handle exception
            // throw e;
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public HashMap getData() {
        return data;
    }

    public void setData(HashMap <String, Object> data) {
        this.data = data;
    }

    public void setData() {
        this.data = new HashMap<String, Object>();
    }

    public void setView(String view) throws Exception {
        if (view.compareToIgnoreCase("") == 0 || view == null ) {
            throw new Exception("Invalid view name");
        }
        else {
            this.view = view;
        }
    }

    public String getView() {
        return this.view;
    }

    public void addItem(String key, Object value) {
        this.data.put(key, value);
    }

    


}