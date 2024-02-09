package ca.event.solosphere.core.constants;

public class RegexTemplate {

    public static final String NOT_EMPTY = "^(?=\\s*\\S).*$";

    //Minimum 2 character length
    public static final String USERNAME = "^.{2,50}$";

    public static final String EMAIL = "[a-zA-Z0-9_\\.\\+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-\\.]+";

    public static final String PASSWORD_PATTERN = "^.{6,}$";

    public static final String NAME_PATTERN = "^.{3,}$";

    public static final String ADDRESS_PATTERN = "^(?s).{20,}$";

    //public static final String PASSWORD_PATTERN_SPECIAL = "((?=.*[\\d\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\-\\_\\+\\=\\{\\}\\[\\]\\;{\\:\\<\\>\\,\\.\\?\\/\\~])(?=.*[A-Z]).*.{8,})";
    public static final String PASSWORD_PATTERN_SPECIAL = "((?=.*[\\d])(?=.*[A-Z]).*.{8,50})";

    public static final String INDIAN_PHONE_NO = "^([0]|\\+91)?[789]\\d{9}$";

    public static final String MOBILE_NO = "^[0-9]([0-9]{9}$)";

    public static final String PINCODE = "^([0-9]{6})?$";

    public static final String ONLY_DIGITS = "[^0-9]";

    public static final String VALID_NAME="^[a-zA-Z][a-zA-Z\\s]{2,50}$";
}
