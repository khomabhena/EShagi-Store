package tech.astromobile.merchant;

public final class DBContract {

    public DBContract() {
    }

    static abstract class DeliveryArea {
        static final String TABLE_NAME = "table_twelve";
        static final String ID = "_id";

        static final String TIMESTAMP = "local_uid";
        static final String IS_SEEN = "col_1";
        static final String IS_MALE = "col_2";

        static final String KEY = "col_3";
        static final String AREA = "col_4";
        static final String DESCRIPTION = "col_5";
        static final String CHARGE = "col_6";
        static final String IS_AVAILABLE = "col_7";
        static final String IS_CLUB = "col_8";
        static final String POSITION = "col_9";
        static final String MEDICAL_AID = "col_10";
        static final String EMAIL = "col_11";
        static final String SUFFIX = "col_12";
        static final String NO = "col_13";
        static final String ADDRESS = "col_14";
        static final String EMPLOYER = "col_15";
        static final String PHONE = "col_16";
        static final String SPECIMEN_TYPE = "col_17";
        static final String MEDICAL_LINK = "col_18";
        static final String FORM_LINK = "col_19";
        static final String PATIENT_DOB = "col_20";
    }

    static abstract class Meals {
        static final String TABLE_NAME = "table_thirteen";
        static final String ID = "_id";
        static final String TYPE = "local_uid";
        static final String KEY = "col_1";
        static final String NAME = "col_2";
        static final String LINK = "col_3";
        static final String PRICE = "col_4";
        static final String DESCRIPTION = "col_6";
        static final String IS_AVAILABLE = "col_5";
        static final String TIMESTAMP = "col_7";
        static final String TAKEAWAY_CHARGE = "col_8";
        static final String LIMIT = "col_9";
        static final String SIZE = "col_10";
        static final String IS_HOME_DELIVERY = "col_11";
        static final String CATEGORY_KEY = "col_12";
        static final String COl_13 = "col_13";
        static final String COl_14 = "col_14";
        static final String COl_15 = "col_15";
        static final String COl_16 = "col_16";
        static final String COl_17 = "col_17";
        static final String COl_18 = "col_18";
        static final String COl_19 = "col_19";
        static final String COl_20 = "col_20";
    }

    static abstract class OrderedMeals {
        static final String TABLE_NAME = "table_ordered_meals";
        static final String ID = "_id";
        static final String TYPE = "local_uid";
        static final String KEY = "col_1";
        static final String ORDER_KEY = "col_9";
        static final String NAME = "col_2";
        static final String LINK = "col_3";
        static final String PRICE = "col_4";
        static final String DISCOUNT = "col_6";
        static final String IS_AVAILABLE = "col_5";
        static final String TIMESTAMP = "col_7";
        static final String TAKEAWAY_CHARGE = "col_8";
        static final String LIMIT = "col_10";
        static final String SIZE = "col_11";
        static final String DESCRIPTION = "col_12";
        static final String IS_HOME_DELIVERY = "col_13";
        static final String CATEGORY_KEY = "col_14";
        static final String COl_15 = "col_15";
        static final String COl_16 = "col_16";
        static final String COl_17 = "col_17";
        static final String COl_18 = "col_18";
        static final String COl_19 = "col_19";
        static final String COl_20 = "col_20";
    }

    static abstract class Orders {
        static final String TABLE_NAME = "table_questions";
        static final String ID = "_id";
        static final String UID = "user_uid";
        static final String ORDER_CODE = "local_uid";
        static final String KEY = "col_1";
        static final String NAME = "col_2";
        static final String SURNAME = "col_3";
        static final String CONFIRMATION_CODE = "col_4";
        static final String ADDRESS = "col_5";
        static final String ORDER_CHARGE = "col_6";
        static final String DELIVERY_CHARGE = "col_7";
        static final String PROCESSING_CHARGE = "col_8";
        static final String SERVICE_CHARGE = "col_9";
        static final String TIMESTAMP = "col_10";
        static final String DELIVER_BEFORE = "col_11";
        static final String DELIVERED_ON = "col_12";
        static final String IS_PAID = "col_13";
        static final String IS_PREPARING = "col_14";
        static final String IS_CLUB = "col_15";
        static final String IS_DONE = "col_16";
        static final String IS_DELIVERED = "col_17";
        static final String IS_TAKEAWAY = "col_18";
        static final String TAKEAWAY_CHARGE = "col_19";
        static final String DELIVERY_START = "col_20";
        static final String NOTES = "col_21";
        static final String DELIVERY_AREA_KEY = "col_22";
    }


    static abstract class DeliveryTime {
        static final String TABLE_NAME = "table_responses_prep";
        static final String ID = "_id";
        static final String LOCAL_UID = "local_uid";
        static final String KEY = "col_1";
        static final String START_HOUR = "col_2";
        static final String END_HOUR = "col_3";
        static final String MINUTE = "col_4";
        static final String EXTRA_CHARGE = "col_5";
        static final String IS_AVAILABLE = "col_6";
        static final String NAME = "col_7";
        static final String POSITION = "col_8";
        static final String COl_9 = "col_9";
        static final String COl_10 = "col_10";
        static final String COl_11 = "col_11";
        static final String COl_12 = "col_12";
        static final String COl_13 = "col_13";
        static final String COl_14 = "col_14";
        static final String COl_15 = "col_15";
        static final String COl_16 = "col_16";
        static final String COl_17 = "col_17";
        static final String COl_18 = "col_18";
        static final String COl_19 = "col_19";
        static final String COl_20 = "col_20";
    }

    static abstract class Categories {
        static final String TABLE_NAME = "table_actions_prep";
        static final String ID = "_id";
        static final String LOCAL_UID = "local_uid";
        static final String KEY = "col_1";
        static final String PARENT_KEY = "col_2";
        static final String NAME = "col_3";
        static final String POSITION = "col_4";
        static final String ENABLED = "col_5";
        static final String TIMESTAMP = "col_6";
        static final String IS_AVAILABLE = "col_7";
        static final String COl_8 = "col_8";
        static final String COl_9 = "col_9";
        static final String COl_10 = "col_10";
        static final String COl_11 = "col_11";
        static final String COl_12 = "col_12";
        static final String COl_13 = "col_13";
        static final String COl_14 = "col_14";
        static final String COl_15 = "col_15";
        static final String COl_16 = "col_16";
        static final String COl_17 = "col_17";
        static final String COl_18 = "col_18";
        static final String COl_19 = "col_19";
        static final String COl_20 = "col_20";
    }

    public static abstract class BankingDetails {
        public   static final String TABLE_NAME = "table_responses_prepl";
        public   static final String ID = "_id";
        public   static final String LOCAL_UID = "local_uid";
        public   static final String KEY = "col_1";
        public   static final String CLIENT_UID = "col_2";
        public   static final String BANK_NAME = "col_3";
        public   static final String ACCOUNT_NAME = "col_4";
        public   static final String BRANCH_NAME = "col_5";
        public  static final String ACCOUNT_NO = "col_6";
        public  static final String BRANCH_CODE = "col_7";
        public  static final String ACCOUNT_TYPE = "col_8";
        public  static final String TIMESTAMP = "col_9";
        public  static final String IS_UPLOADED = "col_10";
        public   static final String NAT_ID = "col_11";
        public    static final String COl_12 = "col_12";
        public   static final String COl_13 = "col_13";
        public   static final String COl_14 = "col_14";
        public   static final String COl_15 = "col_15";
        public   static final String COl_16 = "col_16";
        public   static final String COl_17 = "col_17";
        public  static final String COl_18 = "col_18";
        public  static final String COl_19 = "col_19";
        public   static final String COl_20 = "col_20";
    }

    public static abstract class Client {
        public   static final String TABLE_NAME = "table_thirteenlo";
        public  static final String ID = "_id";
        public   static final String UID = "local_uid";
        public   static final String AGENT_UID = "col_1";
        public  static final String IS_MALE = "col_2";
        public  static final String TITLE = "col_3";
        public   static final String NAME = "col_4";
        public   static final String DOB = "col_6";
        public  static final String NAT_ID = "col_5";
        public static final String COUNTRY_OF_BIRTH_UID = "col_7";
        public  static final String IS_ZIM_RESIDENT = "col_8";
        public  static final String SURNAME = "col_9";
        public  static final String MAIDEN_SURNAME = "col_10";
        public   static final String NUMBER_OF_DEPENDANTS = "col_11";
        public static final String EMAIL = "col_12";
        public  static final String SALARY = "col_13";
        public static final String ECOCASH_NUMBER = "col_14";
        public  static final String HOME_TELEPHONE = "col_15";
        public  static final String MARITAL_STATUS = "col_16";
        public  static final String PASS_PHOTO_PATH = "col_17";
        public static final String PAYSLIP_PATH = "col_18";
        public  static final String NAT_ID_PATH = "col_19";
        public  static final String IS_UPLOADED = "col_20";
        public  static final String MOBILE_NUMBER = "col_21_number";
    }

    static abstract class Keywords {
        static final String TABLE_NAME = "table_chats";
        static final String ID = "_id";
        static final String LOCAL_UID = "local_uid";
        static final String KEY = "col_1";
        static final String NAME = "col_3";
        static final String TYPE = "col_2";
        static final String ENABLED = "col_4";
        static final String QUESTION = "col_5";
        static final String TIMESTAMP = "col_6";
        static final String LANGUAGE = "col_7";
        static final String COl_8 = "col_8";
        static final String COl_9 = "col_9";
        static final String COl_10 = "col_10";
        static final String COl_11 = "col_11";
        static final String COl_12 = "col_12";
        static final String COl_13 = "col_13";
        static final String COl_14 = "col_14";
        static final String COl_15 = "col_15";
        static final String COl_16 = "col_16";
        static final String COl_17 = "col_17";
        static final String COl_18 = "col_18";
        static final String COl_19 = "col_19";
        static final String COl_20 = "col_20";
    }

    static abstract class Actions1 {
        static final String TABLE_NAME = "table_actions_prep_one";
        static final String ID = "_id";
        static final String LOCAL_UID = "local_uid";
        static final String KEY = "col_1";
        static final String PARENT_KEY = "col_2";
        static final String NAME = "col_3";
        static final String ACTION = "col_4";
        static final String ENABLED = "col_5";
        static final String TIMESTAMP = "col_6";
        static final String LANGUAGE = "col_7";
        static final String COl_8 = "col_8";
        static final String COl_9 = "col_9";
        static final String COl_10 = "col_10";
        static final String COl_11 = "col_11";
        static final String COl_12 = "col_12";
        static final String COl_13 = "col_13";
        static final String COl_14 = "col_14";
        static final String COl_15 = "col_15";
        static final String COl_16 = "col_16";
        static final String COl_17 = "col_17";
        static final String COl_18 = "col_18";
        static final String COl_19 = "col_19";
        static final String COl_20 = "col_20";
    }

    static abstract class Actions2 {
        static final String TABLE_NAME = "table_actions_prep_two";
        static final String ID = "_id";
        static final String LOCAL_UID = "local_uid";
        static final String KEY = "col_1";
        static final String PARENT_KEY = "col_2";
        static final String NAME = "col_3";
        static final String ACTION = "col_4";
        static final String ENABLED = "col_5";
        static final String TIMESTAMP = "col_6";
        static final String LANGUAGE = "col_7";
        static final String COl_8 = "col_8";
        static final String COl_9 = "col_9";
        static final String COl_10 = "col_10";
        static final String COl_11 = "col_11";
        static final String COl_12 = "col_12";
        static final String COl_13 = "col_13";
        static final String COl_14 = "col_14";
        static final String COl_15 = "col_15";
        static final String COl_16 = "col_16";
        static final String COl_17 = "col_17";
        static final String COl_18 = "col_18";
        static final String COl_19 = "col_19";
        static final String COl_20 = "col_20";
    }

    static abstract class Actions3 {
        static final String TABLE_NAME = "table_actions_prep_three";
        static final String ID = "_id";
        static final String LOCAL_UID = "local_uid";
        static final String KEY = "col_1";
        static final String PARENT_KEY = "col_2";
        static final String NAME = "col_3";
        static final String ACTION = "col_4";
        static final String ENABLED = "col_5";
        static final String TIMESTAMP = "col_6";
        static final String LANGUAGE = "col_7";
        static final String COl_8 = "col_8";
        static final String COl_9 = "col_9";
        static final String COl_10 = "col_10";
        static final String COl_11 = "col_11";
        static final String COl_12 = "col_12";
        static final String COl_13 = "col_13";
        static final String COl_14 = "col_14";
        static final String COl_15 = "col_15";
        static final String COl_16 = "col_16";
        static final String COl_17 = "col_17";
        static final String COl_18 = "col_18";
        static final String COl_19 = "col_19";
        static final String COl_20 = "col_20";
    }

    static abstract class Actions4 {
        static final String TABLE_NAME = "table_actions_prep_four";
        static final String ID = "_id";
        static final String LOCAL_UID = "local_uid";
        static final String KEY = "col_1";
        static final String PARENT_KEY = "col_2";
        static final String NAME = "col_3";
        static final String ACTION = "col_4";
        static final String ENABLED = "col_5";
        static final String TIMESTAMP = "col_6";
        static final String LANGUAGE = "col_7";
        static final String COl_8 = "col_8";
        static final String COl_9 = "col_9";
        static final String COl_10 = "col_10";
        static final String COl_11 = "col_11";
        static final String COl_12 = "col_12";
        static final String COl_13 = "col_13";
        static final String COl_14 = "col_14";
        static final String COl_15 = "col_15";
        static final String COl_16 = "col_16";
        static final String COl_17 = "col_17";
        static final String COl_18 = "col_18";
        static final String COl_19 = "col_19";
        static final String COl_20 = "col_20";
    }

    static abstract class Actions5 {
        static final String TABLE_NAME = "table_actions_prep_five";
        static final String ID = "_id";
        static final String LOCAL_UID = "local_uid";
        static final String KEY = "col_1";
        static final String PARENT_KEY = "col_2";
        static final String NAME = "col_3";
        static final String ACTION = "col_4";
        static final String ENABLED = "col_5";
        static final String TIMESTAMP = "col_6";
        static final String LANGUAGE = "col_7";
        static final String COl_8 = "col_8";
        static final String COl_9 = "col_9";
        static final String COl_10 = "col_10";
        static final String COl_11 = "col_11";
        static final String COl_12 = "col_12";
        static final String COl_13 = "col_13";
        static final String COl_14 = "col_14";
        static final String COl_15 = "col_15";
        static final String COl_16 = "col_16";
        static final String COl_17 = "col_17";
        static final String COl_18 = "col_18";
        static final String COl_19 = "col_19";
        static final String COl_20 = "col_20";
    }

}