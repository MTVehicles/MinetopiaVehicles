package nl.mtvehicles.core.infrastructure.modules;

import lombok.Getter;
import lombok.Setter;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.dataconfig.Metrics;

public class MetricsModule {
    private static @Getter
    @Setter
    MetricsModule instance;

    public MetricsModule() {
        Metrics metrics = new Metrics(Main.instance, 5932);
        metrics.addCustomChart(new Metrics.SimplePie("used_language", () -> {
            return ConfigModule.defaultConfig.getConfig().getString("messagesLanguage");
        }));
    }
}
