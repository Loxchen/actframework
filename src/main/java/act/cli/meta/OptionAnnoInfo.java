package act.cli.meta;

import org.osgl.util.C;
import org.osgl.util.E;
import org.osgl.util.S;

/**
 * Store the option annotation meta info. There are two mutually exclusive
 * option annotations:
 * <ul>
 *     <li>{@link act.cli.Optional}</li>
 *     <li>{@link act.cli.Required}</li>
 * </ul>
 */
public class OptionAnnoInfo {
    private int index;
    /*
     * e.g -o
     */
    private String lead1;
    /*
     * e.g. --option
     */
    private String lead2;
    private String defVal;
    private String group;
    private String help;
    private boolean required;
    private String param;

    public OptionAnnoInfo(int index, boolean optional) {
        this.index = index;
        this.required = !optional;
    }

    public int index() {
        return index;
    }

    public OptionAnnoInfo value(String[] value) {
        E.illegalArgumentIf(null == value || value.length == 0);
        lead1 = value[0];
        if (value.length > 1) {
            lead2 = value[1];
        } else {
            String[] sa = lead1.split("[,;]+");
            if (sa.length > 2) {
                throw E.unexpected("Option cannot have more than two leads");
            }
            if (sa.length > 1) {
                lead1 = sa[0];
                lead2 = sa[1];
            }
        }
        return this;
    }

    public String lead1() {
        return lead1;
    }

    public String lead2() {
        return lead2;
    }

    public String leads() {
        if (null == lead1 && null == lead2) {
            return "";
        }
        if (null == lead1) {
            return lead2;
        } else if (null == lead2) {
            return lead1;
        }
        return S.join(",", lead1, lead2);
    }

    private void setLeadsIfNotSet(String paramName) {
        if (null == lead1) {
            lead1 = "-" + paramName.charAt(0);
            lead2 = "--" + paramName;
        }
    }

    public OptionAnnoInfo required(boolean required) {
        this.required = required;
        return this;
    }

    public boolean required() {
        return required;
    }

    public OptionAnnoInfo defVal(String defVal) {
        this.defVal = defVal;
        return this;
    }

    public String defVal() {
        return defVal;
    }

    public OptionAnnoInfo group(String group) {
        this.group = group;
        return this;
    }

    public String group() {
        return group;
    }

    public OptionAnnoInfo paramName(String name) {
        param = name;
        setLeadsIfNotSet(name);
        return this;
    }

    public OptionAnnoInfo help(String helpMessage) {
        help = helpMessage;
        return this;
    }

    public String help() {
        StringBuilder sb = S.builder();
        sb.append("\n\t");
        if (S.notEmpty(lead1)) {
            sb.append(lead1);
            if (S.notEmpty(lead2)) {
                sb.append(", ").append(lead2);
            }
        } else if (S.notEmpty(lead2)) {
            sb.append(lead2);
        }
        String s = help;
        if (null == s) {
            s = param;
        }
        sb.append("\t").append(s);
        return sb.toString();
    }
}