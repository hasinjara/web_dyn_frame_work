package utilitaire;

public class Utilitaire {
    

    public String getUrl(String url) {
        int ind = url.indexOf("/");
        url = url.substring(ind);
        ind = url.indexOf("/");
        url = url.substring(ind);
        ind = url.indexOf("/");
        url = url.substring(ind);
        return url.substring(ind);
    }
}
