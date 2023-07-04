package fileUpload;
public class FileUpload{
    String nom ;
    String Path;
    byte [] bytes;
    public FileUpload(){}
    public FileUpload(String nom, String path, byte[] bytes) {
        this.nom = nom;
        Path = path;
        this.bytes = bytes;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getPath() {
        return Path;
    }
    public void setPath(String path) {
        Path = path;
    }
    public byte[] getBytes() {
        return bytes;
    }
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
