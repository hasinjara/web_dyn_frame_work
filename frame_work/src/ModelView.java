package modelview;

public class ModelView {
    private String view;

    private void setView(String view) throws Exception {
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

    public ModelView() {
        
    }

    public ModelView(String view) {
        try {
            setView(view);
        } catch (Exception e) {
            // TODO: handle exception
            // throw e;
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        
    }
}