package com.frontier.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author fendou
 */
@SpringBootApplication
public class DemoApplication {
//    private static Logger logger = LoggerFactory.getLogger(DemoApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

//    public static void main(String[] args) {
//
//
//        try {
//            // start
//            JobConfig.getInstance().set();
//            JobConfig.getInstance().initJobExecutor();
//
//            // Blocks until interrupted
//            while (true) {
//                try {
//                    TimeUnit.HOURS.sleep(1);
//                } catch (InterruptedException e) {
//                    break;
//                }
//            }
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//        } finally {
//            // destroy
//            JobConfig.getInstance().destroyJobExecutor();
//        }
//
//    }

}
