package cn.edu.ruc;

import cn.edu.ruc.hola.fake.FakeDbUtils;
import org.junit.jupiter.api.Test;

public class FakeDbUtilsTest {

    @Test
    public void testForCreate() {
        new FakeDbUtils().createFakeDb();
    }

    @Test
    public void testForDelete() {
        new FakeDbUtils().deleteFakeDb();
    }

    @Test
    public void testForAddFakeData() {
        new FakeDbUtils().addFakeData();
    }
}
