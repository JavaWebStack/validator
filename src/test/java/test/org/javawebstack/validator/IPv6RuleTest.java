package test.org.javawebstack.validator;

import org.javawebstack.abstractdata.AbstractMapper;
import org.javawebstack.validator.Rule;
import org.javawebstack.validator.ValidationContext;
import org.javawebstack.validator.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IPv6RuleTest {

    @Test
    public void testSimpleIPV4Rules() {
        Validator validator = Validator.getValidator(TestObject1.class);
        TestObject1 test = new TestObject1();

        // FIX IT
        //test.ip = "ThisIsNotAnIP";
        //assertFalse(validator.validate(new ValidationContext(), new AbstractMapper().toAbstract(test)).isValid());
        test.ip = "::1/128";
        assertTrue(validator.validate(new ValidationContext(), new AbstractMapper().toAbstract(test)).isValid());
        test.ip = "0:0:0:0:0:0:0:0";
        assertTrue(validator.validate(new ValidationContext(), new AbstractMapper().toAbstract(test)).isValid());
    }


    private static class TestObject1 {
        @Rule("ipv6")
        String ip;
    }

}
