package nl.mtvehicles.core.infrastructure.models;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PaperUtils {

  public final static boolean isRunningPaper;

  static {
    isRunningPaper = Bukkit.getServer().getVersion().toLowerCase().contains("paper");
  }

}
