package org.bongiorno.latency;


public class Latency {

    private final Throwable thrown;
    private Class<?> clazz;
    private String method;
    private long duration;

    public Latency(Class<?> clazz, String method, long duration, Throwable t) {
        this.clazz = clazz;
        this.method = method;
        this.duration = duration;
        this.thrown = t;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getMethod() {
        return method;
    }

    public long getDuration() {
        return duration;
    }

    public Throwable getThrown() {
        return thrown;
    }

    @Override
    public String toString() {
        String exception = thrown != null ? "," + thrown.getClass().getName() :"";
        return String.format("%s,%s,%d%s",clazz.getName(),method,duration, exception);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Latency)) return false;

        Latency latency = (Latency) o;

        if (duration != latency.duration) return false;
        if (thrown != null ? !thrown.equals(latency.thrown) : latency.thrown != null) return false;
        if (!clazz.equals(latency.clazz)) return false;
        return method.equals(latency.method);

    }

    @Override
    public int hashCode() {
        int result = thrown != null ? thrown.hashCode() : 0;
        result = 31 * result + clazz.hashCode();
        result = 31 * result + method.hashCode();
        result = 31 * result + (int) (duration ^ (duration >>> 32));
        return result;
    }
}
