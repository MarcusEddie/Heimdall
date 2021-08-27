package org.iman.Heimdallr.barrier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RulerTests {

    @Test
    public void testIsNullSuccessGivenANullString() {
        String param = null;
        String rs = Ruler.isNull(param);
        Assertions.assertNull(rs);
    }
    
}
