package com.builtinmenlo.selfieclub.test;

import android.test.InstrumentationTestCase;

import com.builtinmenlo.selfieclub.util.Util;

/**
 * Created by Leonardo on 6/25/14.
 */
public class DeviceTest extends InstrumentationTestCase {
    public void testUDID() throws Exception{
        final String udid = Util.getUDID(getInstrumentation().getContext());
        assertNotNull(udid);
    }
}
