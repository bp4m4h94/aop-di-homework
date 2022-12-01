package com.example.aopdihomework;

import com.example.aopdihomework.aop.adapter.FailedCounterAdapter;
import com.example.aopdihomework.aop.adapter.HashAdapter;
import com.example.aopdihomework.aop.adapter.OptAdopter;
import com.example.aopdihomework.aop.adapter.ProfilerRepo;
import com.example.aopdihomework.aop.adapter.impl.FailedTooManyTimesException;
import com.example.aopdihomework.aop.adapter.impl.LoggerAdapter;
import com.example.aopdihomework.aop.service.AuthenticationService;
import com.sun.nio.sctp.Notification;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.security.NoSuchAlgorithmException;

import static org.mockito.Mockito.doThrow;

public class AuthenticationServiceTest {

    private AuthenticationService _authenticationService;
    private FailedCounterAdapter _failedCounter;
    private HashAdapter _hash;

    private Notification notification;
    private LoggerAdapter _logger;
//        private INotification _notification;
    private OptAdopter _otp;
    private ProfilerRepo _profileRepo;

    @Before
    public void setUp()
    {
//        _failedCounter = Substitute.For<IFailedCounter>();
//        _hash = Substitute.For<IHash>();
//        _logger = Substitute.For<ILogger>();
//        _notification = Substitute.For<INotification>();
//        _otp = Substitute.For<IOtp>();
//        _profileRepo = Substitute.For<IProfileRepo>();
//        _authenticationService = new AuthenticationService(_failedCounter, _hash, _logger, _notification, _otp, _profileRepo);
    }

    @Test
    public void is_valid() {
        givenAccountIsLocked("Ryan", false);
        givenPasswordFromRepo("Ryan", "hashed password");
        givenHashedResult("hello", "hashed password");
        givenCurrentOtp("Ryan", "123_456_joey_hello_world");

        boolean isValid = false;
        try {
            isValid = _authenticationService.isValid("Ryan",
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

        boolean isValid = _authenticationService.isValid("Ryan",
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
        doThrow(FailedTooManyTimesException.class).when(_authenticationService.isValid("joey", "hello", "123_456_joey_hello_world"));
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
        Mockito.verify(_logger, Mockito.times(1)).log();
//        _logger.Received(1).LogInfo(Arg.Is<String>(s => s.Contains(containContent)));
    }

    private void givenCurrentFailedCount(int failedCount)
    {
        Mockito.when( _failedCounter.getFailedCounter("Ryan")).thenReturn(failedCount);
    }

    private void shouldNotifyUser(String account)
    {
        Mockito.verify(notification, Mockito.times(1)).notify(account, "account: " + account + " failed to login.");

    }

    private void shouldAddFailedCount(String account)
    {
        Mockito.verify(_failedCounter, Mockito.times(1)).addFailedCounter(account);

//        _failedCounter.Received(1).Add(account);
    }

    private void whenInvalid(String account) {
        givenAccountIsLocked("joey", false);
        givenPasswordFromRepo(account, "hashed password");
        givenHashedResult("hello", "wrong password");
        givenCurrentOtp(account, "123_456_joey_hello_world");

        try {
            _authenticationService.isValid(account,
                    "hello",
                    "123_456_joey_hello_world");
        } catch (FailedTooManyTimesException e) {
            e.printStackTrace();
        }
    }

    private void shouldResetFailedCount(String account)
    {
        Mockito.verify(_failedCounter, Mockito.times(1)).resetFailedCounter(account);

//        _failedCounter.Received(1).Reset(account);
    }

    private void whenValid(String account)
    {
        givenAccountIsLocked("joey", false);
        givenPasswordFromRepo(account, "hashed password");
        givenHashedResult("hello", "hashed password");
        givenCurrentOtp(account, "123_456_joey_hello_world");

        try {
            _authenticationService.isValid(account,
                    "hello",
                    "123_456_joey_hello_world");
        } catch (FailedTooManyTimesException e) {
            e.printStackTrace();
        }
    }

//    private void ShouldThrow<TException>(TestDelegate action) where TException : Exception
//{
//    Assert.Throws<TException>(action);
//}

    private void givenCurrentOtp(String account, String otp)
    {
        Mockito.when(_otp.getOtp(account)).thenReturn(otp);
    }

    private void givenHashedResult(String input, String hashedResult)
    {
        Mockito.when( _hash.getHash(input)).thenReturn(hashedResult);
    }

    private void givenPasswordFromRepo(String account, String password)
    {
        Mockito.when(_profileRepo.getPassword(account)).thenReturn(password);
    }

    private void givenAccountIsLocked(String account, boolean isLocked)
    {
        Mockito.when(_failedCounter.isLocked(account)).thenReturn(isLocked);
    }
}
