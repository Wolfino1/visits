package com.visitas.visitas.visitas.domain.utils.constants;

public class DomainConstants {
    public static final String START_DATE_NULL_MESSAGE = "Start date cannot be null";
    public static final String END_DATE_NULL_MESSAGE = "End date cannot be null";
    public static final String END_DATE_MUST_BE_AFTER_START_DATE ="End date must be after start date";
    public static final int VISIT_WEEKS_RANGE = 3;
    public static final String VISIT_START_IN_PAST            = "Start date cannot be in the past";
    public static final String VISIT_OUT_OF_ALLOWED_RANGE     = "You can only schedule visits within the next 3 weeks";
    public static final String VISIT_OVERLAP                  = "You already have another visit scheduled in that time range";
    public static final String VISIT_END_BEFORE_START         = "End date must be after start date";
    public static final String PAGE_NUMBER_INVALID = "Page number cannot be less than 0";
    public static final String PAGE_SIZE_INVALID = "Page size cannot be less than 0";
    public static final String HOUSE_DOES_NOT_EXIST        = "House does not exist";
    public static final String HOUSE_NOT_BELONG_TO_SELLER  = "You can only schedule visits for your own houses";
    public static final String VISIT_NOT_FOUND = "Visit not found";
    public static final String VISIT_SLOT_FULL = "Slots for this visits are full";
    public static final String VISIT_ALREADY_SCHEDULED_BY_USER = "You have already booked this visit";
    public static final int MAX_SCHEDULED_PER_SLOT = 2;
    public static final String VISIT_FULL = "There are 2 people booked for this visit already";
    public static final String INVALID_HOUSE_SELLER = "Unable to find this house seller";
    public static final String HOUSE_LOCATION_NOT_FOUND = "Unable to find this house location";
}
