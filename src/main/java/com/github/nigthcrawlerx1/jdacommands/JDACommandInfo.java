package com.github.nigthcrawlerx1.jdacommands;

public class JDACommandInfo {

    public static final String VERSION_MAJOR = "@versionMajor@";
    public static final String VERSION_MINOR = "@versionMinor@";
    public static final String VERSION_REVISION = "@versionRevision@";
    public static final String VERSION_BUILD = "@versionBuild@";
    public static final String VERSION = VERSION_MAJOR.startsWith("@") ? "dev" : String.format("%s.%s.%s_%s", VERSION_MAJOR, VERSION_MINOR, VERSION_REVISION, VERSION_BUILD);

}
