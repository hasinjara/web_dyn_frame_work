package utilitaire;

public class Utilitaire {
    

    public String getUrl(String url) {
        String[] split = url.split("\\/");
        String last_split = split[split.length -1];
        String[] split_var = last_split.split("\\?"); 
        return split_var[0];
    }
}
