package com.price.pegging.Utilitty;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PasswordPattern {
    public boolean patternCheck(String password)
    {
        final String PASSWORD_PATTERN = ".{8,}";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
