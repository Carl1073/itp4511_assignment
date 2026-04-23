package ict.tag;
// import the library

import java.io.IOException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class HelloTag extends SimpleTagSupport {

    private String name;

    // Setter for the attribute
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void doTag() {

        try {

            JspWriter out = getJspContext().getOut();
            if (name != null) {
                getJspContext().getOut().write("Hello, " + name + "!");
            } else {
                getJspContext().getOut().write("Hello, Guest!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
