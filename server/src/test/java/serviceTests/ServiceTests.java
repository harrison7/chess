package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.*;
import service.*;

public class ServiceTests {
    static final ClearService clearService = new ClearService(new MemoryUserDAO(),
            new MemoryAuthDAO(), new MemoryGameDAO());

    @BeforeEach
    void clearSuccess() throws DataAccessException {
        clearService.clear();
    }


}
