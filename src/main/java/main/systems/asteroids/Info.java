package main.systems.asteroids;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class Info {
    public static double getUsageCpu() {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        return operatingSystemMXBean.getSystemLoadAverage();
    }
}
