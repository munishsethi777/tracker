package in.satyainfopages.geotrack.model;

public class IConstants {

    public static final String USER_LAST_PUSHED_LOC_SEQ = "user_last_pushed_loc_seq";

    //-------------------------- APIs
    public static final String DOMAIN_NAME = "http://www.envirotechlive.com/tracker/";
    public static final String API_URL = DOMAIN_NAME + "api/";

    public static final String REGISTRATION_QUERY = "mbl={0}&eml={1}&pss={2}&fnm={3}";
    public static final String REGISTRATION_URL = API_URL + "Registration.php?" + REGISTRATION_QUERY;

    public static final String LOGIN_QUERY = "seq={0}&pss={1}";
    public static final String LOGIN_URL = API_URL + "LoginUser.php?" + LOGIN_QUERY;

    public static final String CREATE_GROUP_QUERY = "name={0}&userseq={1}";
    public static final String CREATE_GROUP_URL = API_URL + "CreateGroup.php?" + CREATE_GROUP_QUERY;

    public static final String SET_TRACKING_QUERY = "data=";
    public static final String SET_TRACKING_URL = API_URL + "SetTracking.php?" + SET_TRACKING_QUERY;

    public static final String MY_CONTACTS_QUERY = "numbers=";
    public static final String MY_CONTACTS_URL = API_URL + "MyContacts.php?" + MY_CONTACTS_QUERY;

    public static final String GROUP_REQUEST_QUERY = "byuserseq={0}&tomobilenumber={1}&groupseq={2}";
    public static final String GROUP_REQUEST_URL = API_URL + "GroupRequest.php?" + GROUP_REQUEST_QUERY;

    public static final String RESPOND_GROUP_REQUEST_QUERY = "userseq={0}&groupseq={1}&response={2}";
    public static final String RESPOND_GROUP_REQUEST_URL = API_URL + "RespondGroupRequest.php?" + RESPOND_GROUP_REQUEST_QUERY;

    public static final String GET_TRACKING_GROUP_QUERY = "groupseq={0}";
    public static final String GET_TRACKING_GROUP_URL = API_URL + "GetTrackingForGroup.php?" + GET_TRACKING_GROUP_QUERY;


}
