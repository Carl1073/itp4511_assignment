package ict.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.List;
import java.lang.reflect.Method;

public class DropdownTag extends SimpleTagSupport {
    private List<?> items;
    private String name;    // Used for the value attribute (e.g., serviceId)
    private String option;  // Used for the display text (e.g., serviceName)

    public void setItems(List<?> items) { this.items = items; }
    public void setName(String name) { this.name = name; }
    public void setOption(String option) { this.option = option; }

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        if (items == null) return;

        try {
            for (Object item : items) {
                // Construct getter names (e.g., "get" + "ServiceId")
                String nameGetter = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
                String optionGetter = "get" + option.substring(0, 1).toUpperCase() + option.substring(1);

                Method getNameMethod = item.getClass().getMethod(nameGetter);
                Method getOptionMethod = item.getClass().getMethod(optionGetter);

                Object valueAttr = getNameMethod.invoke(item);
                Object labelAttr = getOptionMethod.invoke(item);

                out.println("<option value=\"" + valueAttr + "\">" + labelAttr + "</option>");
            }
        } catch (Exception e) {
            throw new JspException("Error in DropdownTag", e);
        }
    }
}