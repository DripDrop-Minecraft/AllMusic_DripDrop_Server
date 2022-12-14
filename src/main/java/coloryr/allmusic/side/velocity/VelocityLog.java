package coloryr.allmusic.side.velocity;

import coloryr.allmusic.api.IMyLogger;
import org.slf4j.Logger;

public class VelocityLog implements IMyLogger {
    private final Logger logger;

    public VelocityLog(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void warning(String data) {
        logger.warn(data);
    }

    @Override
    public void info(String data) {
        logger.info(data);
    }
}
