package weather.model;

public  class TemperatureClassifier {
    public static TemperatureCategory classify(double temperatureCelsius) {
        if (temperatureCelsius < 0.0) {
            return TemperatureCategory.FREEZING;
        }
        if (temperatureCelsius < 10.0) {
            return TemperatureCategory.COLD;
        }
        if (temperatureCelsius < 20.0) {
            return TemperatureCategory.MILD;
        }
        if (temperatureCelsius <= 30.0) {
            return TemperatureCategory.WARM;
        }
        return TemperatureCategory.HOT;
    }
}
