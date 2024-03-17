package com.swtp4.backend;

import com.swtp4.backend.services.UniqueNumberService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UniqueNumberServiceTest {

    private final UniqueNumberService service = new UniqueNumberService();

    @Test
    void whenGenerateUniqueNumber_thenUUIDIsNotNull() {
        String uniqueNumber = service.generateUniqueNumber();
        assertNotNull(uniqueNumber, "Die generierte UUID sollte nicht null sein");
    }

    @Test
    void whenGenerateUniqueNumber_thenUUIDIsValidFormat() {
        String uniqueNumber = service.generateUniqueNumber();
        assertTrue(uniqueNumber.matches("^[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}$"),
                "Die generierte UUID entspricht nicht dem erwarteten Format");
    }
}
