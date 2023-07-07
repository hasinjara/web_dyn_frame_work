package modelview;

import java.util.Vector;
import java.util.Map;
import java.util.Objects;
import java.util.HashMap;
import java.lang.ClassLoader;

import com.google.gson.Gson;

public class ModelView {
    String view;
    HashMap <String, Object> data;
    HashMap <String, Object> session;
    boolean isJson;


    public boolean isJson() {
        return isJson;
    }

    public void setJson(boolean isJson) {
        this.isJson = isJson;
    }

    public HashMap<String, Object> getSession() {
        return session;
    }

    public void setSession(HashMap<String, Object> session) {
        this.session = session;
    }

    public ModelView() {
        setData();
        setSession();
        setJson(false);
    }

    public ModelView(String view) {
        try {
            setView(view);
            setData();
            setSession();
            setJson(false);
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
            setJson(false);
        } catch (Exception e) {
            // TODO: handle exception
            // throw e;
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private ModelView(String view, HashMap <String, Object> data, HashMap <String, Object> session) {
        try {
            setView(view);
            setData(data);
            setSession(session);
            setJson(false);
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

    public void setSession() {
        this.session = new HashMap<String, Object>();
        // System.out.println("lsdhfawefhaieufWELI WGGLF UIG DDss");
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

    public void addSession(String key, Object value) {
        this.session.put(key, value);
    }

    public String getDataJson() {
        String json = "";
        Gson gson = new Gson();
        json = gson.toJson(this.getData());
        return json;
    }


    

}