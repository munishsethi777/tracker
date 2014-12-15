package in.satyainfopages.geotrack.model;

public class IConstants {

//	public static final String USER_MOBILE = "user_moblie";
//	public static final String USER_SEQ = "user_seq";
//    public static final String USER_EMAIL = "user_seq";
//    public static final String USER_FULL_NAME = "user_seq";
//    public static final String USER_GROUP_INFO = "user_groups";


//	public static final String USER_SUBMITTERS_KEY = "user_submitters";
//	public static final String USER_SUBMITTERS_NAME_KEY = "user_submitters_name";
//	public static final String USER_ISSUBMITTER_KEY = "user_issubmitter";
//	public static final String USER_ISVIEWER_KEY = "user_isviewer";
//	public static final String USER_PUSH_SERVICE_KEY = "user_push_service";
//	public static final String USER_PULL_SERVICE_KEY = "user_pull_service";
//	public static final String USER_LAST_PUSHED_LOC_SEQ = "user_last_pushed_loc_seq";
//	public static final String USER_LAST_PULL_DATE = "user_last_pull_date";
//	public static final String USER_PUSH_CALL_GAP = "user_push_call_gap";
//	public static final String USER_PULL_CALL_GAP = "user_pull_call_gap";


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

}
