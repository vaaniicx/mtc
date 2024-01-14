package at.if22b208.mtc.server.http;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Request {

    private Method method;
    private String urlContent;
    private String pathname;
    private List<String> pathParts;
    private String params;
    private Header header = new Header();
    private String body;

    public String getServiceRoute() {
        if (this.pathParts == null ||
                this.pathParts.isEmpty()) {
            return null;
        }

        return '/' + this.pathParts.get(0);
    }

    public void setUrlContent(String urlContent) {
        this.urlContent = urlContent;
        boolean hasParams = urlContent.contains("?");

        if (hasParams) {
            String[] pathParts = urlContent.split("\\?");
            this.setPathname(pathParts[0]);
            this.setParams(pathParts[1]);
        } else {
            this.setPathname(urlContent);
            this.setParams(null);
        }
    }

    public void setPathname(String pathname) {
        this.pathname = pathname;
        String[] parts = pathname.split("/");

        this.pathParts = new ArrayList<>();
        for (String part : parts) {
            if (part != null && !part.isEmpty()) {
                this.pathParts.add(part);
            }
        }
    }

    public String getRoot() {
        return this.getPathParts().get(0) == null ? "" : this.getPathParts().get(0);
    }

    public String getBody() {
        return this.body == null ? "" : this.body.toLowerCase();
    }
}
