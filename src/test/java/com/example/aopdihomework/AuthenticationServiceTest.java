package com.example.aopdihomework;

import com.example.aopdihomework.aop.adapter.*;
import com.example.aopdihomework.aop.adapter.impl.FailedCounterAdapterImpl;
import com.example.aopdihomework.aop.adapter.impl.FailedTooManyTimesException;
import com.example.aopdihomework.aop.service.AuthenticationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import java.security.NoSuchAlgorithmException;

import static org.mockito.Mockito.doThrow;

public class AuthenticationServiceTest {

    private AuthenticationService authenticationService;
    private FailedCounterAdapter failedCounter;
    private HashAdapter hash;
    private NotificationAdapter notification;
    private LoggerAdapter logger;
    private OptAdopter otp;
    private ProfilerRepo profileRepo;

    @Before
    public void setUp()
    {
        failedCounter = Mockito.mock(FailedCounterAdapterImpl.class);
        hash = Mockito.mock(HashAdapter.class);
        notification = Mockito.mock(NotificationAdapter.class);
        logger = Mockito.mock(LoggerAdapter.class);
        otp = Mockito.mock(OptAdopter.class);
        profileRepo = Mockito.mock(ProfilerRepo.class);

        authenticationService = new AuthenticationService(hash, profileRepo, failedCounter, otp, logger, notification);

    }

    @Test
    public void is_valid() {
        givenAccountIsLocked("Ryan", false);
        givenPasswordFromRepo("Ryan", "hashed password");
        givenHashedResult("hello", "hashed password");
        givenCurrentOtp("Ryan", "123_456_joey_hello_world");

        boolean isValid = false;
        try {
            isValid = authenticationService.isValid("Ryan",
                    "abc123",
                    "123_456_joey_hello_world");
        } catch (FailedTooManyTimesException e) {
            e.printStackTrace();
        }
        shouldBeValid(isValid);
    }

    @Test
    public void should_reset_failed_count_when_valid()
    {
        whenValid("joey");
        shouldResetFailedCount("joey");
    }

    @Test
    public void Invalid() throws FailedTooManyTimesException {
        givenAccountIsLocked("joey", false);
        givenPasswordFromRepo("joey", "hashed password");
        givenHashedResult("hello", "wrong password");
        givenCurrentOtp("joey", "123_456_joey_hello_world");

        boolean isValid = authenticationService.isValid("Ryan",
                "abc123",
                "123_456_joey_hello_world");
        shouldBeInvalid(isValid);
    }

    @Test
    public void should_add_failed_count_when_invalid()
    {
        whenInvalid("joey");
        shouldAddFailedCount("joey");
    }

    @Test
    public void should_notify_user_when_invalid()
    {
        whenInvalid("joey");
        shouldNotifyUser("joey");
    }

    @Test
    public void should_log_current_failed_count_when_invalid()
    {
        givenCurrentFailedCount(3);
        whenInvalid("joey");
        ShouldLog("times:3.");
    }

    @Test(expected = FailedTooManyTimesException.class)
    public void account_is_locked() throws FailedTooManyTimesException, NoSuchAlgorithmException {
        givenAccountIsLocked("joey", true);
        Mockito.when(authenticationService.isValid("joey", "123", "opt123")).thenThrow(FailedTooManyTimesException.class).thenReturn(true);
    }

    private void shouldBeInvalid(boolean isValid)
    {
        Assert.assertFalse(isValid);
    }

    private void shouldBeValid(boolean isValid)
    {
        Assert.assertTrue(isValid);
    }

    private void ShouldLog(String containContent)
    {
        Mockito.verify(logger, Mockito.times(1)).log(containContent);
    }

    private void givenCurrentFailedCount(int failedCount)
    {
        Mockito.when(failedCounter.getFailedCounter("Ryan")).thenReturn(failedCount);
    }

    private void shouldNotifyUser(String account)
    {
        Mockito.verify(notification, Mockito.times(1)).sendNotification(account, "account: " + account + " failed to login.");

    }

    private void shouldAddFailedCount(String account)
    {
        Mockito.verify(failedCounter, Mockito.times(1)).addFailedCounter(account);
    }

    private void whenInvalid(String account) {
        givenAccountIsLocked("joey", false);
        givenPasswordFromRepo(account, "hashed password");
        givenHashedResult("hello", "wrong password");
        givenCurrentOtp(account, "123_456_joey_hello_world");

        try {
            authenticationService.isValid(account,
                    "hello",
                    "123_456_joey_hello_world");
        } catch (FailedTooManyTimesException e) {
            e.printStackTrace();
        }
    }

    private void shouldResetFailedCount(String account)
    {
        Mockito.verify(failedCounter, Mockito.times(1)).resetFailedCounter(account);
    }

    private void whenValid(String account)
    {
        givenAccountIsLocked("joey", false);
        givenPasswordFromRepo(account, "hashed password");
        givenHashedResult("hello", "hashed password");
        givenCurrentOtp(account, "123_456_joey_hello_world");

        try {
            authenticationService.isValid(account,
                    "hello",
                    "123_456_joey_hello_world");
        } catch (FailedTooManyTimesException e) {
            e.printStackTrace();
        }
    }

//    private void ShouldThrow(TestDelegate action)
//{
//    Assert.assertThrows();
//}

    private void givenCurrentOtp(String account, String otp)
    {
        Mockito.when(this.otp.getOtp(account)).thenReturn(otp);
    }

    private void givenHashedResult(String input, String hashedResult)
    {
        Mockito.when( hash.getHash(input)).thenReturn(hashedResult);
    }

    private void givenPasswordFromRepo(String account, String password)
    {
        Mockito.when(profileRepo.getPassword(account)).thenReturn(password);
    }

    private void givenAccountIsLocked(String account, boolean isLocked)
    {
        Mockito.when(failedCounter.isLocked(account)).thenReturn(isLocked);
    }
}
