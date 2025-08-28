package org.mbg.common.base;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: LinhLH
 **/
@Component
@Slf4j
public class ApplicationShutdownPoint {
    @PreDestroy
    public void onShutdown() {

        _log.info("<<<<<<<<<< @PreDestroy method called in ApplicationShutdownPoint. " +
                "Application is attempting to shut down gracefully. >>>>>>>>>>");

         try {
             Thread.sleep(1000); // Wait for 1 second
         } catch (InterruptedException e) {
             Thread.currentThread().interrupt();
         }
    }
}
