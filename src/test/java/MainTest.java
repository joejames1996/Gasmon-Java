import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

public class MainTest
{
    @Test
    public void testAlwaysPass()
    {
        int i = 1;
        assertThat(i).isEqualTo(1);
    }
}
