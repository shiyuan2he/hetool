package cn.hsy.me.hetool.core.util;

import org.junit.Test;

import java.util.stream.LongStream;

import static org.junit.Assert.*;

/**
 * @author heshiyuan
 */
public class MathUtilTest {

    @Test
    public void main() {
        LongStream.range(0L, 1000L).forEach(i -> {
            int mod = Math.floorMod(i, 100);
            if(mod < 10){
                System.out.println(i + "    " + mod);
            }
        });
    }
}