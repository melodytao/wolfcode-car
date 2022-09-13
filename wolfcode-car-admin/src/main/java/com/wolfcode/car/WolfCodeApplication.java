package com.wolfcode.car;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author ruoyi
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class WolfCodeApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(WolfCodeApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  e店邦O2O平台启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
               " __          __   _  __  _____          _      \n" +
                " \\ \\        / /  | |/ _|/ ____|        | |     \n" +
                "  \\ \\  /\\  / /__ | | |_| |     ___   __| | ___ \n" +
                "   \\ \\/  \\/ / _ \\| |  _| |    / _ \\ / _` |/ _ \\\n" +
                "    \\  /\\  / (_) | | | | |___| (_) | (_| |  __/\n" +
                "     \\/  \\/ \\___/|_|_|  \\_____\\___/ \\__,_|\\___|\n" +
                "                                               ");
    }
}
