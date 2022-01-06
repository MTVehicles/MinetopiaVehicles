<h1 align="center">MinetopiaVehicles</h1>
<p align="center"><em>A realistic vehicles plugin for your Minecraft server!</em></p>

<div align="center">

<a href="https://www.spigotmc.org/resources/80910/"><img alt="Spiget Version" src="https://img.shields.io/spiget/version/80910?label=version"></a>
<a href="https://github.com/Zettovec/MinetopiaVehicles/releases"><img alt="GitHub release (latest by date including pre-releases)" src="https://img.shields.io/github/v/release/Zettovec/MinetopiaVehicles?include_prereleases&label=pre-release"></a>
<a href="https://www.spigotmc.org/resources/80910/"><img alt="Spiget Version" src="https://img.shields.io/spiget/downloads/80910"></a>
<a href="https://www.spigotmc.org/resources/80910/"><img alt="Spiget Stars" src="https://img.shields.io/spiget/stars/80910?color=yellow"></a>
---  

### ❗USE `/VEHICLE LANGUAGE` TO CHANGE THE LANGUAGE❗</h3>
Messages in the plugin can be displayed in multiple languages. (Default: English) You can find them <a href="https://github.com/GamerJoep/MinetopiaVehicles/tree/master/src/main/resources/messages">here</a>.

---

</div>

## How to download?

The plugin works on all spigot versions from 1.12.2 - 1.18.1, **except 1.14.x**!

### Be aware!


If your server minecraft version is not supported by the plugin, it won't load.

Versions 1.12.1 and lower are not, and will not be supported.<br>
(Did you know that 1.8 was released in 2014? And some people still use it! Whoa.)


> ⚠ We support ALL LATEST PATCH VERSIONS of Spigot/Paper 1.12+, except 1.14.4.<br>
> For example 1.16.5 IS supported, but 1.16.3 is not (and similarly with all other versions).

### 1. Download the latest version
- Latest stable releases are always published on <a href="https://www.spigotmc.org/resources/80910/">Spigot.org</a>.

Put the downloaded plugin into your server's `plugins/` folder.

### 2. Download the resource pack
- **Download the resource pack.** Link can be found on our <a href="https://discord.gg/vehicle">Discord server</a> (We've created a&nbsp;<a href="https://www.youtube.com/watch?v=rSXNd-6rhQk">YouTube tutorial</a>).

You may set it as a server resource pack, or everyone would have to download the resource pack individually. (For instance, take a look <a href="https://apexminecrafthosting.com/how-to-upload-a-resource-pack/" taget="_blank">here</a>.)

> ⚠ The vehicles WON'T look properly without the resource pack.

### 3. Restart the server
Restart the server. No explanation needed right?

### 4. Enjoy!
**Et voilà, it's working!** Don't forget to set your language using `/vehicle language`.<br>

If this did not work don't hesitate to contact the support on our Discord server.

## Contact us!

**Discord is the best place to report discovered bugs!**<br>
<a href="https://discord.gg/vehicle" taget="_blank"><img src="https://discord.com/api/guilds/694809233819959308/widget.png?style=banner2"></a>

#### Other links:

<a href="https://mtvehicles.nl" taget="_blank"><img alt="Website" src="https://img.shields.io/badge/-Website-green"></a>
<a href="https://discord.gg/vehicle" taget="_blank"><img alt="Website" src="https://img.shields.io/badge/-Discord-blue"></a>
<a href="https://www.spigotmc.org/resources/80910/" taget="_blank"><img alt="Website" src="https://img.shields.io/badge/-Spigot-orange"></a>
<a href="https://bstats.org/plugin/bukkit/MTVehicles/5932" taget="_blank"><img alt="Website" src="https://img.shields.io/badge/-bStats-grey"></a>

> ⚠ **You will not receive any support regarding unsupported Minecraft versions or other than latest plugin versions.**

## Functionalities

✔ Add custom vehicles.<br>
✔ Adjust vehicle data in game.<br>
✔ Reloading/Restarting the server is possible with vehicles in the folder.<br>
✔ Gasoline function customizable in config.<br>
✔ All current vehicles + helicopters.<br>
✔ ANWB if your car is in the water.<br>
✔ Trunks per vehicle & customizable.<br>
✔ Vehicle edit function.<br>
✔ Adjustable speed per vehicle.<br>
✔ Vouchers for vehicles.<br>
✔ Integration with WorldGuard and Vault.<br>
✔ Active support/developers.<br>
✔ Open Source.

## **Commands & Permissions**

### Main commands

| Command           | Description                                       | permission                                       |
|-------------------|---------------------------------------------------|--------------------------------------------------|
| /vehicle info     | Get information about a vehicle                   | `None`, `mtvehicles.admin` to display admin info |
| /vehicle help     | Get a list of all the possible commands           | `None`, `mtvehicles.admin` to display admin info |
| /vehicle version  | Get info about the plugin _(use this on discord)_ | `mtvehicles.admin`                               |
| /vehicle language | Change the plugin language                        | `mtvehicles.language`                            |

### Vehicle specific commands
These commands can only be executed by the owner of the vehicle

| Command               | Description                                          | Permission |
|-----------------------|------------------------------------------------------|------------|
| /vehicle public       | Let everyone sit in your car                         | `None`     |
| /vehicle private      | Only allow people who have access to sit in your car | `None`     |
| /vehicle addrider     | Add a rider to your vehicle                          | `None`     |
| /vehicle addmember    | Add a member to your vehicle                         | `None`     |
| /vehicle removerider  | Remove a rider from your vehicle                     | `None`     |
| /vehicle removemember | Remove a member from your vehicle                    | `None`     |

### Moderation commands

| Command              | Description                                 | Permission               |
|----------------------|---------------------------------------------|--------------------------|
| /vehicle edit        | Adjust vehicle settings                     | `mtvehicles.edit`        |
| /vehicle menu        | Open a menu containing all the vehicles     | `mtvehicles.menu`        |
| /vehicle givevoucher | Give a voucher to a player                  | `mtvehicles.givevoucher` |
| /vehicle givecar     | Give a vehicle to a player                  | `mtvehicles.givecar`     |
| /vehicle restore     | A menu to recover vehicles                  | `mtvehicles.restore`     |
| /vehicle benzine     | Open the menu with jerrycans for the fuel   | `mtvehicles.benzine`     |
| /vehicle reload      | Reload the plugin's config files            | `mtvehicles.reload`      |
| /vehicle setowner    | Set an owner of a vehicle                   | `mtvehicles.setowner`    |
| /vehicle delete      | Remove a vehicle in your hand from the data | `mtvehicles.delete`      |
| /vehicle update      | Update the plugin to the latest version     | `mtvehicles.update`      |

### Other permissions

| Permission                        | Description                                                                                |
|-----------------------------------|--------------------------------------------------------------------------------------------|
| `mtvehicles.ride`                 | Whether a player can ride others' vehicles                                                 |
| `mtvehicles.anwb`                 | Whether a player can pick up vehicles from water - useless unless ANWB turned on in config |
| `mtvehicles.oppakken`             | Whether a player can pick up vehicles                                                      |
| `mtvehicles.filljerrycans`        | Whether a player can fill up a jerry can - can be turned on/off in config                  |
| `mtvehicles.filljerrycansforfree` | Whether a player can fill up a jerry can for free (Economy plugin + Vault necessary)       |

## **Softdependencies**

### WorldGuard

You can download <a href="https://dev.bukkit.org/projects/worldguard">WorldGuard</a> to get access to custom features with regions. (Do not forget that WorldGuard needs WorldEdit as its dependency.)

> ⚠ WorldGuard features are not supported on 1.12.2 as it is no longer maintained and uses older API version.

You can edit the behaviour **in the config** where you may also find information how to set up the custom flags.

> ⚠ WorldGuard features are disabled by default and have to be enabled in the config.

| Custom flags     | State | Description                                                                                    |
|------------------|-------|------------------------------------------------------------------------------------------------|
| `mtv-gasstation` | ALLOW | This region is a gas station. (More information can be found in the [config.yml])              |
|                  | DENY  | You can NEVER fill up jerrycans here. No config option can change this.                        |
| `mtv-place`      | ALLOW | Enables players to place vehicles, **if region whitelist for placing is enabled.**             |
|                  | DENY  | Prevents players from placing vehicles, **if region blacklist for placing is enabled.**        |
| `mtv-enter`      | ALLOW | Enables players to enter vehicles, **if region whitelist for entering is enabled.**            |
|                  | DENY  | Prevents players from entering vehicles, **if region blacklist for entering is enabled.**      |
| `mtv-pickup`     | ALLOW | Enables players to pick up vehicles, **if region whitelist for picking up is enabled.**        |
|                  | DENY  | Prevents players from picking up vehicles, **if region blacklist for picking up is enabled.**  |

### Vault

You can download <a href="https://www.spigotmc.org/resources/vault.34315/">Vault</a> to enable economy integration. (Of course, you must have an economy plugin that supports Vault.)

Options concerning Vault and prices are to be found in the config.yml.

<div align="right">
  <sub>README.md by <a href="https://github.com/Zettovec">Nikd0</a> & <a href="https://github.com/Jeffrey-H">Jeffrey-H</a></sub>
</div>

[config.yml]: https://github.com/GamerJoep/MinetopiaVehicles/blob/master/src/main/resources/config.yml
