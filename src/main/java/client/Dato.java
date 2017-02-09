package client;


/**
 * Created by sgerman on 17/01/2017.
 */
public class Dato {

    private final long id;
    private final String content;

    public Dato() {
        id = 0;
        content = "";

    }
    public Dato(long id, String content) {
        this.id = id;
        this.content = content;
    }

    @Override
    public String toString() {
        return "{id:" + id + ", content:" + content + "}";
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
