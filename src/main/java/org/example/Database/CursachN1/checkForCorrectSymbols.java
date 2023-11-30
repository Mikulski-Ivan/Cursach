package org.example.Database.CursachN1;

public class checkForCorrectSymbols {

    //проверяет последний символ пароля и логина на корректность-----------------------------------------------------------------------------------------
    public static Boolean isCorrectSymbolsForPasswordAndLogin(String newValue) {
        return (newValue.charAt(newValue.length() - 1)<'0'||
                (newValue.charAt(newValue.length() - 1)>'9'&& newValue.charAt(newValue.length() - 1) < 'A') ||
                (newValue.charAt(newValue.length() - 1) > 'Z' && newValue.charAt(newValue.length() - 1) < 'a') ||
                newValue.charAt(newValue.length() - 1) > 'z');
    }

    //проверяет последний символ кода на корректность-----------------------------------------------------------------------------------------
    public static Boolean isCorrectSymbolsForCode(String newValue) {
        return (newValue.charAt(newValue.length() - 1) < '0' ||
                newValue.charAt(newValue.length() - 1) > '9' ||
                newValue.charAt(0) == '0');
    }

    //проверяет последний символ почты на корректность-----------------------------------------------------------------------------------------
    public static Boolean isCorrectSymbolsForMail(String newValue) {
        return (newValue.charAt(newValue.length() - 1) < '-' ||
                (newValue.charAt(newValue.length() - 1) > '.' && newValue.charAt(newValue.length() - 1) < '0') ||
                (newValue.charAt(newValue.length() - 1) > '9' && newValue.charAt(newValue.length() - 1) < '@') ||
                (newValue.charAt(newValue.length() - 1) > 'Z' && newValue.charAt(newValue.length() - 1)<'_') ||
                (newValue.charAt(newValue.length() - 1) > '_' && newValue.charAt(newValue.length() - 1)<'a') ||
                newValue.charAt(newValue.length() - 1)>'z');
    }
}
