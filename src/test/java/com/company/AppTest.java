package com.company;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {

    @Test
    public void testMessage() {
        assertEquals(
            "Enterprise Java App Running Successfully",
            App.message()
        );
    }
}
