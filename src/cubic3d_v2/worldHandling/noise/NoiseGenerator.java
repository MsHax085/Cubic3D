package cubic3d_v2.worldHandling.noise;

public abstract class NoiseGenerator {
    
    protected final int perm[] = new int[512];
    protected double offsetX;
    protected double offsetY;
    protected double offsetZ;

    public static int floor(double x) {
        return x >= 0 ? (int) x : (int) x - 1;
    }

    protected static double fade(double x) {
        return x * x * x * (x * (x * 6 - 15) + 10);
    }

    protected static double lerp(double x, double y, double z) {
        return y + x * (z - y);
    }

    protected static double grad(int hash, double x, double y, double z) {
        hash &= 15;
        double u = hash < 8 ? x : y;
        double v = hash < 4 ? y : hash == 12 || hash == 14 ? x : z;
        return ((hash & 1) == 0 ? u : -u) + ((hash & 2) == 0 ? v : -v);
    }

    public double noise(double x) {
        return noise(x, 0, 0);
    }

    public double noise(double x, double y) {
        return noise(x, y, 0);
    }

    public abstract double noise(double x, double y, double z);

    public double noise(double x, int octaves, double frequency, double amplitude) {
        return noise(x, 0, 0, octaves, frequency, amplitude);
    }

    public double noise(double x, int octaves, double frequency, double amplitude, boolean normalized) {
        return noise(x, 0, 0, octaves, frequency, amplitude, normalized);
    }

    public double noise(double x, double y, int octaves, double frequency, double amplitude) {
        return noise(x, y, 0, octaves, frequency, amplitude);
    }

    public double noise(double x, double y, int octaves, double frequency, double amplitude, boolean normalized) {
        return noise(x, y, 0, octaves, frequency, amplitude, normalized);
    }

    public double noise(double x, double y, double z, int octaves, double frequency, double amplitude) {
        return noise(x, y, z, octaves, frequency, amplitude, false);
    }

    public double noise(double x, double y, double z, int octaves, double frequency, double amplitude, boolean normalized) {
        double result = 0;
        double amp = 1;
        double freq = 1;
        double max = 0;

        for (int i = 0; i < octaves; i++) {
            result += noise(x * freq, y * freq, z * freq) * amp;
            max += amp;
            freq *= frequency;
            amp *= amplitude;
        }

        if (normalized) {
            result /= max;
        }

        return result;
    }
}