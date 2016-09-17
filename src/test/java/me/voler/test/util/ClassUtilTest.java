package me.voler.test.util;

import me.voler.admin.util.db.ClassUtil;
import org.junit.Assert;
import org.junit.Test;

public class ClassUtilTest {

    @Test
    public void testCamel2Underline() {
        ClassUtil classUtil = new ClassUtil();
        Assert.assertEquals(classUtil.camel2Underline("User"), "user");
        Assert.assertEquals(classUtil.camel2Underline("UserInfo"), "user_info");
        Assert.assertEquals(classUtil.camel2Underline("UserInfoV3"), "user_info_v3");
        Assert.assertEquals(classUtil.camel2Underline("UserInfoDAO"), "user_info_dao");
    }

}
