package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import nl.mtvehicles.core.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class VehicleUpdate extends MTVehicleSubCommand {

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!isPlayer) return false;

        if (!checkPermission("mtvehicles.update")) return true;

        Player p = (Player) sender;

        File dest = new File("plugins");
        URL file;
        try {
            download(file = new URL("https://minetopiavehicles.nl/api/MTVehicles.jar"), dest);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        return true;
    }


    public void download(URL file, File dest) {
        try {
            InputStream is = file.openStream();
            File finaldest = new File(dest + "/" + file.getFile().replace("/api/MTVehicles.jar", "/"+Main.fol().replace("plugins", "")));
           // File finaldest = new File(dest + "/" + file.getFile());
            finaldest.getParentFile().mkdirs();
            finaldest.createNewFile();
            System.out.println("Voor de laatste stap moeten we even de server herladen!");

            OutputStream os = new FileOutputStream(finaldest);
            byte data[] = new byte[1024];
            int count;
            while ((count = is.read(data, 0, 1024)) != -1) {
                os.write(data, 0, count);
            }
            os.flush();
            is.close();
            os.close();
            Main.instance.getServer().reload();
            sendMessage(Main.messagesConfig.getMessage("updateSucces"));


        } catch (Exception ec) {
            sendMessage(Main.messagesConfig.getMessage("updateFailed"));
            ec.printStackTrace();
        }
    }
}