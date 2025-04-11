package dev.fire.utils.ext;

public class OSUtil {
    private static final String OS = System.getProperty("os.name").toLowerCase();
    public static boolean isWindows() {
        return OS.contains("win");
    }
    public static boolean isMac() {
        return OS.contains("mac");
    }
    public static boolean isUnix() {
        return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
    }
    public static boolean isSolaris() {
        return OS.contains("sunos");
    }
    public static OperatingSystem getOS() {
        if (isWindows()) {
            return OperatingSystem.WINDOWS;
        } else if (isMac()) {
            return OperatingSystem.MAC;
        } else if (isUnix()) {
            return OperatingSystem.UNIX;
        } else if (isSolaris()) {
            return OperatingSystem.SOLARIS;
        } else {
            return OperatingSystem.OTHER;
        }
    }
}