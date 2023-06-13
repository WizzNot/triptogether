package com.example.project.reg;

public class Valid {
    public static String isValid(String password) {//функция проверки защищенности предлогаемого пользователем пароля
        int min = 5;
        int max = 16;
        int digit = 0;
        int special = 0;
        int upCount = 0;
        int loCount = 0;
        String otv = "";
        if (password.length() >= min && password.length() <= max) {
            for (int i = 0; i < password.length(); i++) {
                char c = password.charAt(i);
                if (Character.isUpperCase(c)) {
                    upCount++;
                }
                if (Character.isLowerCase(c)) {
                    loCount++;
                }
                if (Character.isDigit(c)) {
                    digit++;
                }
                if (c >= 33 && c <= 46 || c == 64) {
                    special++;
                }
            }
            if (special >= 1 && loCount >= 1 && upCount >= 1 && digit >= 1) {
                return "Подходящий пароль";
            }
            if (loCount == 0) {
                otv += "• Вам нужен по крайней мере один символ нижнего регистра" + "\n";
            }
            if (upCount == 0) {
                otv += "• Вам нужен по крайней мере один символ верхнего регистра" + "\n";
            }
            if (digit == 0) {
                otv += "• Вам нужна хотя бы одна цифра" + "\n";
            }
            if (special == 0) {
                otv += "• Вам нужен по крайней мере один особый символ" + "\n";
            }
        }
        else if (password.length() < min) {
            otv += "• Пароль должен содержать не менее " + min + " символов" + "\n";
        }
        else if (password.length() > max) {
            otv += "• Проль слишком большой, максимальная длина: " + max + " символов" + "\n";
        }
        return otv;
    }
}
