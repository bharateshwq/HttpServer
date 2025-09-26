package com.bharateshprojects.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum HttpVersion {
    HTTP_1_1("HTTP/1.1", 1, 1);

    public final String LITERAL;
    public final int MINOR;
    public final int MAJOR;

    HttpVersion(String literal, int MINOR, int MAJOR) {
        this.LITERAL = literal;
        this.MINOR = MINOR;
        this.MAJOR = MAJOR;
    }

    private static final Pattern httpVersionRegexPatterm = Pattern.compile("^HTTP/(?<major>\\d).(?<minor>\\d+)");

    public static HttpVersion getBestCompatibleVersion(String literalVersion) throws BadHttpVersionException {
        Matcher matcher = httpVersionRegexPatterm.matcher(literalVersion);
        if (!matcher.find() || matcher.groupCount() != 2) {
            throw new BadHttpVersionException();
        }
        int major = Integer.parseInt(matcher.group("major"));
        int minor = Integer.parseInt(matcher.group("minor"));
        HttpVersion tempBestCompatible = null;
        for (HttpVersion version : HttpVersion.values()) {
            if (version.LITERAL.equals(literalVersion)) {
                return version;
            } else {
                if (version.MAJOR == major) {
                    if (version.MINOR < minor) {
                        tempBestCompatible = version;
                    }
                }
            }
        }
        return tempBestCompatible;
    }
}
