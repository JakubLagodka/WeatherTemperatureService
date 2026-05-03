package weather.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TemperatureClassifierTest {
    private final TemperatureClassifier classifier = new TemperatureClassifier();

    @Test
    void classifiesTemperatureRanges() {
        assertEquals(TemperatureCategory.FREEZING, classifier.classify(-0.1));
        assertEquals(TemperatureCategory.COLD, classifier.classify(0.0));
        assertEquals(TemperatureCategory.COLD, classifier.classify(9.9));
        assertEquals(TemperatureCategory.MILD, classifier.classify(10.0));
        assertEquals(TemperatureCategory.MILD, classifier.classify(19.9));
        assertEquals(TemperatureCategory.WARM, classifier.classify(20.0));
        assertEquals(TemperatureCategory.WARM, classifier.classify(30.0));
        assertEquals(TemperatureCategory.HOT, classifier.classify(30.1));
    }
}
