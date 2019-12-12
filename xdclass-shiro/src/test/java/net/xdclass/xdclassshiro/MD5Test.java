package net.xdclass.xdclassshiro;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.Test;

public class MD5Test {

    @Test
    public void test(){
        String hashName = "md5";

        String pwd = "123456";
        Object reuslt = new SimpleHash(hashName,pwd,null,2);
        System.out.println(reuslt);
    }
}
