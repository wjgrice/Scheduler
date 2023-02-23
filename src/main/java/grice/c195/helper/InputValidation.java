package grice.c195.helper;

import javafx.scene.control.TextField;

public class InputValidation {

        public static boolean isValidTime(TextField field) {
            String time = field.getText();
            if (time.length() != 5) {
                return true;
            }
            if (time.charAt(2) != ':') {
                return true;
            }
            try {
                int hour = Integer.parseInt(time.substring(0, 2));
                int minute = Integer.parseInt(time.substring(3, 5));
                if (hour < 0 || hour > 23) {
                    return true;
                }
                if (minute < 0 || minute > 59) {
                    return true;
                }
            } catch (NumberFormatException e) {
                return true;
            }
            return false;
        }
}
