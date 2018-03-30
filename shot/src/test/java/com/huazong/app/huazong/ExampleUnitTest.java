package com.huazong.app.huazong;

import com.huazong.app.huazong.entity.Friend;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

        Friend f = new Friend();
        f.setUserId(1);
        Friend f2 = new Friend();
        f2.setUserId(1);
        System.out.print(f.equals(f2));

        //assertEquals(4, 2 + 2);
    }
}