/**
 * Created by Josh0207 on 15/11/2016.
 */

public class campaign {
    private String title;
    private String image;
    private String desc;
    private int status;

    public campaign(){

    }

    public campaign(String title, String image, String desc, int status) {
        this.title = title;
        this.image = image;
        this.desc = desc;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
