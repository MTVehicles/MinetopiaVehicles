package nl.mtvehicles.core.infrastructure.modules;

import lombok.Getter;
import lombok.Setter;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.utils.Metrics;

/**
 * Module configuring bStats metrics system
 */
public class MetricsModule {
    private static @Getter
    @Setter
    MetricsModule instance;

    public MetricsModule() {
        Metrics metrics = new Metrics(Main.instance, 5932);
        metrics.addCustomChart(new Metrics.SimplePie("used_language", () -> {
            return ConfigModule.secretSettings.getMessagesLanguage();
        }));
        metrics.addCustomChart(new Metrics.SimplePie("used_driveUp", () -> {
            String returns;
            switch (ConfigModule.defaultConfig.driveUpSlabs()){
                case SLABS:
                    returns = "slabs"; break;
                case BLOCKS:
                    returns = "blocks"; break;
                case BOTH: default:
                    returns = "both"; break;
            }
            return returns;
        }));
    }
}
