package com.project.application.util;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.springframework.stereotype.Component;

@Component
public class Fixtures {
    private static final Long RANDOMISATION_SEED = 8883999317134L;
    public static EasyRandom easyRandom = new EasyRandom(
            new EasyRandomParameters()
                    .seed(RANDOMISATION_SEED)
                    .collectionSizeRange(1, 5)
                    .stringLengthRange(1, 1)
                    .scanClasspathForConcreteTypes(true)
    );
}
