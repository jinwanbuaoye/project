import tests.*;

import java.io.IOException;

public class RunTests {
    public static void main(String[] args) throws IOException, InterruptedException {
        LoginPage login = new LoginPage();

        login.loginPageRight();
        login.loginSuc();
        login.loginFail();

        ActivityPage activityPage = new ActivityPage();
        NewactivityPage newactivityPage = new NewactivityPage();
        PrizePage prizePage = new PrizePage();
        NewprizePage newprizePage = new NewprizePage();
        UserPage userPage = new UserPage();
        NewuserPage newuserPage = new NewuserPage();

        activityPage.ActivitLogin();

        newactivityPage.newActivityFail();
        prizePage.prizeSuc();
        newprizePage.prizeFail();
        userPage.userSuc();
        newuserPage.newUserFail();
        newuserPage.newUserSuc();

        activityPage.ActivityNoLogin();
    }
}
