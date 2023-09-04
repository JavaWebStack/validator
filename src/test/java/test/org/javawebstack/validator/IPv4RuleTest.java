package test.org.javawebstack.validator;

import org.javawebstack.abstractdata.mapper.Mapper;
import org.javawebstack.validator.Rule;
import org.javawebstack.validator.ValidationContext;
import org.javawebstack.validator.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IPv4RuleTest {

    @Test
    public void testSimpleIPV4Rules() {
        Validator validator = Validator.getValidator(TestObject1.class);
        TestObject1 test = new TestObject1();

        test.ip = "ThisIsNotAnIP";
        assertFalse(validator.validate(new ValidationContext(), new Mapper().map(test)).isValid());
        test.ip = "0.0.0.0";
        assertTrue(validator.validate(new ValidationContext(), new Mapper().map(test)).isValid());
        test.ip = "255.255.255.255";
        assertTrue(validator.validate(new ValidationContext(), new Mapper().map(test)).isValid());
        test.ip = "256.455.275.295";
        assertFalse(validator.validate(new ValidationContext(), new Mapper().map(test)).isValid());
    }


    private static class TestObject1 {
        @Rule("ipv4")
        String ip;
    }

}
