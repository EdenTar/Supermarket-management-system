package Test;

import Backend.Logic.Controllers.TransportEmployee.UserController;
import Backend.Logic.Starters.Starter;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserControllerTest {
    public static UserController userController;
    @BeforeClass
    public static void setUp() throws Exception {
        Starter starter=Starter.getInstance();
        userController=starter.getUserController();
    }
    @Test
    public void login() {
        Assert.assertFalse(userController.isLoggedIn(3, "DRIVER"));
        try {
            userController.login(3,"3","DRIVER");
            Assert.assertTrue(userController.isLoggedIn(3, "DRIVER"));
            userController.logout(3);
        } catch (Exception e) {
            Assert.fail();
        }

    }

    @Test
    public void logout() {
        try {
            userController.login(3,"3","DRIVER");
            Assert.assertTrue(userController.isLoggedIn(3, "DRIVER"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        userController.logout(3);
        Assert.assertFalse(userController.isLoggedIn(3,"DRIVER"));
    }
}