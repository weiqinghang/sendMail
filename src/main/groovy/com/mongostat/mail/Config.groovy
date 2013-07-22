package com.mongostat.mail

/**
 * Created with IntelliJ IDEA.
 * User: jleo
 * Date: 12-10-16
 * Time: 下午3:16
 * To change this template use File | Settings | File Templates.
 */
class Config {
    public static def config;
    static {
        def userDir = System.getProperty("user.dir")
        println("config dir is " + userDir);
        def configFile = new File(userDir, "config.groovy")
        config = new ConfigSlurper().parse(configFile.text);
    }
}
