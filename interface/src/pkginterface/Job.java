package pkginterface;

import java.io.Serializable;

public class Job implements Serializable
{
    private String id;
    private String title;

    public Job(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }
}
