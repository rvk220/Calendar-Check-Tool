package calendarchecktool;

import java.util.Scanner;

public class CalendarCheckTool {

    final static Scanner sc = new Scanner(System.in);

    static boolean isYearLeap(int year) {
        if (year % 4 != 0) {
            return false;
        } else {
            return (year % 100 != 0) ? true : year % 400 == 0;
        }
    }

    private static int getDaysInYear(int year) {
        return (isYearLeap(year)) ? 366 : 365;
    }

    private static int getDaysInMonth(boolean isYearLeap, int month1To12) {
        int[] days = {31, isYearLeap ? 29 : 28,
            31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        return days[month1To12 - 1];
    }

    static int getDayOfWeekJan1(int year) {
        // 0 = Monday, ... , 6 = Sunday.
        final int[] dW1Jan = {4, 6, 0, 1, 2, 4, 5, 6, 0, 2, 3,
            4, 5, 0, 1, 2, 3, 5, 6, 0, 1, 3, 4, 5, 6, 1, 2, 3};
        return dW1Jan[year % 28];

    }

    private static int checkYear(String input) {
        if (input.matches("190[1-9]|19[1-9]\\d|20\\d{2}|2100")) {
            return Integer.parseInt(input);
        } else {
            System.out.println("ERROR: INVALID YEAR NUMBER FORMAT OR THE ENTERED YEAR"
                    + " IS OUT OF RANGE (1901-2100). TRY AGAIN!!");
            return -1;
        }
    }

    private static boolean isYearOutputCorrect(int output) {
        if (output < 1901 || output > 2100) {
            System.out.println("ERROR: THE AUTO-GENERATED DATE"
                    + " IS OUT OF RANGE (1901-2100). TRY AGAIN!");
            return false;
        } else {
            return true;
        }
    }

    private static int checkMonth(String input) {
        if (input.matches("(0?[1-9])|(1[0-2])")) {
            return Integer.parseInt(input);
        } else {
            String[] monthPattern = {"(?i)jan(uary){0,1}", "(?i)feb(ruary){0,1}",
                "(?i)mar(ch){0,1}", "(?i)apr(il){0,1}", "(?i)may", "(?i)jun(e){0,1}",
                "(?i)jul(y){0,1}", "(?i)aug(ust){0,1}", "(?i)sep(tember){0,1}",
                "(?i)oct(ober){0,1}", "(?i)nov(ember){0,1}", "(?i)dec(ember){0,1}"};
            for (int i = 0; i < 12; i++) {
                if (input.matches(monthPattern[i])) {
                    return i + 1;
                }
            }
            System.out.println("ERROR: YOUR INPUT OF MONTH IS WRONG, TRY AGAIN!");
            return -1;
        }
    }

    private static int checkDayOfMonth(String input, int month1To12, boolean isYLeap) {
        if (input.matches("(0?)[1-9]|[12]\\d|3[01]")) {
            int temp = Integer.parseInt(input);
            if (temp < 29 || temp >= 29 && temp <= getDaysInMonth(isYLeap, month1To12)) {
                return temp;
            } else {
                System.out.println("ERROR: THE DAY OF MONTH NUMBER EXCEEDS "
                        + "QUANTITY OF DAYS IN THE CURRENT MONTH!");
                return -1;
            }
        } else {
            System.out.println("ERROR: THE DAY OF MONTH INPUT FORMAT IS WRONG "
                    + "OR THE NUMBER IS OUT OF RANGE!");
            return -1;
        }
    }

    private static String getMonthName(int month1To12) {
        String[] month = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};
        return month[month1To12 - 1];
    }

    static int[] enterFullDate(int numOfDate) {
        // numOfDate = 0 for a single date; 1 and 2 - for 1st and 2nd dates if 2 dates needed.
        // Only in the following line: {dd, mm, yyyy}; everywhere else - {yyyy, mm, dd}
        System.out.println("Enter a date " + (numOfDate == 0 ? "" : numOfDate + " ")
                + "in the forrmat 'day.month.year' or 'day month year' (e.g., "
                + "'15.02.2020', '15 feb 2020', '15 february 2020'.)");
        int year = -1;
        int month = -1;
        int dayOfMonth = -1;
        while (year == -1 || month == -1 || dayOfMonth == -1) {
            String[] date = sc.nextLine().split("\\.| ", 3);
            if (date.length == 3) {
                year = checkYear(date[2]);
                if (year != -1) {
                    month = checkMonth(date[1]);
                    if (month != -1) {
                        dayOfMonth = checkDayOfMonth(date[0], month, isYearLeap(year));
                    }
                }
            } else {
                System.out.println("ERROR: WRONG DATE FORMAT, TRY AGAIN!");
            }
        }
        System.out.println("You have entered  the date: "
                + dayOfMonth + " " + getMonthName(month) + " " + year + ".");
        return new int[]{year, month, dayOfMonth};
    }

    static int enterYearOnly() {
        int year = -1;
        while (year == -1) {
            year = checkYear(sc.nextLine());
        }
        System.out.println("You have entered  the year " + year + ".");
        return year;
    }

    static int getDayOfYear(boolean isYearLeap, int month1To12, int dayOfMonth) {
        int temp = 0;
        for (int i = 2; i <= month1To12; i++) {
            temp += getDaysInMonth(isYearLeap, i - 1);
        }
        return temp + dayOfMonth;
    }

    private static String getDateNameFromArray(int[] date) {
        //e.g., {2020 5 17} to "17 May 2020"
        String[] months = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};
        return date[2] + " " + months[date[1] - 1] + " " + date[0];
    }

    static int[] getDateFromDayOfYear(int dayOfYear, int year) {
        // dayOfYear between 1 and 365/366
        boolean isYLeap = isYearLeap(year);
        int month = 0;
        int daysOfPreviousMonths = 0;
        for (int i = 0; i < dayOfYear; i += getDaysInMonth(isYLeap, ++month)) {
            daysOfPreviousMonths = i;
        }
        int dayOfMonth = dayOfYear - daysOfPreviousMonths;
        return new int[]{year, month, dayOfMonth};
    }


    private static String getDayOfWeekName(int dayOfWeek0To6) {
        String[] name = {"Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday", "Sunday"};
        return name[dayOfWeek0To6];
    }
    
    static String getDayOfWeekName(int[] date) {
        String[] dWName = {"Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday", "Sunday"};
        int dayOfYear = getDayOfYear(isYearLeap(date[0]), date[1], date[2]);
        int dw1Jan = getDayOfWeekJan1(date[0]);
        return dWName[(dw1Jan + dayOfYear - 1) % 7];
    }

    private static String getSameYearsString(int year) {
        StringBuilder result = new StringBuilder();
        int dW1JanX = getDayOfWeekJan1(year);
        boolean isYLeap = isYearLeap(year);
        System.out.println("It is a " + (isYLeap ? "" : "non-") + "leap year "
                + "starting with " + getDayOfWeekName(dW1JanX) + ".");
        for (int i = 1901; i <= 2100; i++) {
            if (i != year && getDayOfWeekJan1(i) == dW1JanX && isYLeap == isYearLeap(i)) {
                result.append(i).append(", ");
            }
        }
        result.replace(result.length() - 2, result.length(), ".");
        result.insert(0, "Years with the same calandar as " + year + ": ");
        return result.toString();
    }

    private static boolean isDateOrderCorrect(int[] date1, int[] date2) {
        // {year, month, day)
        if (date2[0] < date1[0]) {
            return false;
        } else if (date2[0] == date1[0] && date2[1] < date1[1]) {
            return false;
        } else if (date2[0] == date1[0] && date2[1] == date1[1]) {
            return date1[2] <= date2[2];
        } else {
            return true;
        }
    }

    static int getDaysBetween2Dates(int[] date1, int[] date2) {
        boolean isOrderCorrect = isDateOrderCorrect(date1, date2);
        int y1 = (isOrderCorrect) ? date1[0] : date2[0];
        int m1 = (isOrderCorrect) ? date1[1] : date2[1];
        int d1 = (isOrderCorrect) ? date1[2] : date2[2];
        int y2 = (isOrderCorrect) ? date2[0] : date1[0];
        int m2 = (isOrderCorrect) ? date2[1] : date1[1];
        int d2 = (isOrderCorrect) ? date2[2] : date1[2];
        int sign = (isOrderCorrect) ? 1 : -1;
        boolean isLeap1 = isYearLeap(y1);
        if (y1 == y2) {
            return (getDayOfYear(isLeap1, m2, d2) - getDayOfYear(isLeap1, m1, d1)) * sign;
        } else {
            int startDays = (isLeap1 ? 366 : 365) - getDayOfYear(isLeap1, m1, d1);
            int lastDays = getDayOfYear(isYearLeap(y2), m2, d2);
            int middleDays = 0;
            for (int i = y1 + 1; i < y2; i++) {
                middleDays += getDaysInYear(i);
            }
            return (startDays + middleDays + lastDays) * sign;
        }
    }

    static int[] getDatePlusMinusXDays(int[] date, int changeDays) {
        int dayOfYear1 = getDayOfYear(isYearLeap(date[0]), date[1], date[2]);
        int year2 = date[0];
        int dayOfYear2 = dayOfYear1 + changeDays;
        if (changeDays > 0) {
            while (dayOfYear2 > 366 || (dayOfYear2 == 366 && getDaysInYear(year2) == 365)) {
                dayOfYear2 -= getDaysInYear(year2++);
            }
        } else {
            while (dayOfYear2 <= 0) {
                dayOfYear2 += getDaysInYear(--year2);
            }
        }
        return getDateFromDayOfYear(dayOfYear2, year2);
    }

    public static void cycleMode1YearInfo() {
        System.out.println("You have just chosen the mode 1 - check of calendar duplicates "
                + "(printing out a list of years having the same calendars as the current "
                + "year).  \n First of all, enter a year (number between 1901 and 2100).");
        System.out.println(getSameYearsString(enterYearOnly()));
    }

    public static void cycleMode2DayOfWeekFromDate() {
        System.out.println("You have just chosen the mode 2 - checking a "
                + "day of week for a specific date.");
        int[] date = enterFullDate(0);
        System.out.println("It is " + getDayOfWeekName(date) + ".");
    }

    public static void cycleMode3DaysBetween2Dates() {
        System.out.println("You have just chosen the mode 3 - defining quantity"
                + " of days between two dates.");
        int[] date1 = enterFullDate(1);
        int[] date2 = enterFullDate(2);
        int days = getDaysBetween2Dates(date1, date2);
        System.out.println("The quantity of days between " + date1[2] + " "
                + getMonthName(date1[1]) + " " + date1[0] + " and " + date2[2] + " "
                + getMonthName(date2[1]) + " " + date2[0] + " equals " + days + " days.");
    }

    public static void cycleMode4DatePlusXDays() {
        System.out.println("You have just chosen the mode 4 - defining a date "
                + "before/after X days comparing to another date.");
        boolean resultOutOfRange = true;
        while (resultOutOfRange) {
            int[] date1 = enterFullDate(0);
            System.out.println("Now enter a quantity of days (with '-' if you want to "
                    + "subtract them and without '-' if you need to add them to the date).");
            int days = 0;
            while (days == 0) {
                String input = sc.nextLine();
                days = (input.matches("(-?)\\d{1,5}")) ? Integer.parseInt(input) : 0;
                if (days == 0) {
                    System.out.println("ERROR: WRONG INPUT FORMAT OR NUMBER OF DAYS EQUALS ZERO!");
                }
                System.out.println("You have entered the number of days " + days + ".");
            }
            int[] date2 = getDatePlusMinusXDays(date1, days);
            resultOutOfRange = !isYearOutputCorrect(date2[0]);
            if (!resultOutOfRange) {
                System.out.println(Math.abs(days) + " days " + ((days > 0) ? "after" : "before")
                        + " " + date1[2] + " " + getMonthName(date1[1]) + " " + date1[0]
                        + ", is " + date2[2] + " " + getMonthName(date2[1]) + " " + date2[0] + ".");
            }
        }
    }
    
    public static void main(String[] args) {
        boolean again = true;
        System.out.println("Welcome to Calendar Check Tool!");
        while(again) {
            System.out.println("\nPlease choose your action by entering a digit:\n"
                    + "1 - check of calendar duplicates (printing out a list of "
                    + "years having the same calendars as the current year);\n"
                    + "2 - checking a day of week for a specific date;\n"
                    + "3 - defining quantity of days between two dates; \n"
                    + "4 - defining a date before/after X days comparing to another date;\n"
                    + "? - any other symbol(s) - to exit the tool.");
            String input = sc.nextLine();
            if (input.equals("1")) {
                cycleMode1YearInfo();
            } else if  (input.equals("2")) {
                cycleMode2DayOfWeekFromDate();
            } else if  (input.equals("3")) {
                cycleMode3DaysBetween2Dates();
            } else if (input.equals("4")) {
                cycleMode4DatePlusXDays();
            } else {
                System.out.println("You have just terminated work of Calendar "
                        + "Check Tool. Thanks for using!");
                again = false;
            }             
        }
        sc.close();
    }
}
