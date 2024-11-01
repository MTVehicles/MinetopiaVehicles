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

### Auto-updater info for contributors:

As of 2.5.5, auto-updater has been moved to https://github.com/MTVehicles/auto-updater!

(The current auto-updater API will only be updated to 2.5.5. This means that in the future, if you're using an older version, you'll have to update to 2.5.5 first, and only then will newer versions be displayed – if available.)

---

</div>

## How to download?

The plugin works on all Spigot/Paper versions 1.12.2+, **except 1.14.x**!

### Be aware!


If your server minecraft version is not supported by the plugin, it won't load.

Versions 1.12.1 and lower are NOT, and will NOT be supported.<br>
(Frankly, 1.8 was released [a pretty long time ago](https://howoldisminecraft189.today/). And some people still use it, right? I don't get it.)


> ⚠ We support ALL LATEST PATCH VERSIONS of Spigot/Paper 1.12+, except 1.14.4.<br>
> For example 1.16.5 IS supported, but 1.16.3 is not (and similarly with all other versions).
>
> **Still confused?** [Click here](https://wiki.mtvehicles.eu/faq.html#_5-i-am-getting-an-internal-error-occurred-error) to get a list of compatible versions.

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

### ❗ We've got a new wiki! https://wiki.mtvehicles.eu/ ❗

**Discord is the best place to report discovered bugs!**<br>
<a href="https://discord.gg/vehicle" taget="_blank"><img src="https://discord.com/api/guilds/694809233819959308/widget.png?style=banner2"></a>

#### Other links:

<a href="https://mtvehicles.nl" taget="_blank"><img alt="Website" src="https://img.shields.io/badge/-Website-green"></a>
<a href="https://discord.gg/vehicle" taget="_blank"><img alt="Website" src="https://img.shields.io/badge/-Discord-blue"></a>
<a href="https://www.spigotmc.org/resources/80910/" taget="_blank"><img alt="Website" src="https://img.shields.io/badge/-Spigot-orange"></a>
<a href="https://bstats.org/plugin/bukkit/MTVehicles/5932" taget="_blank"><img alt="Website" src="https://img.shields.io/badge/-bStats-grey"></a>

> ⚠ **You will not receive any support regarding unsupported Minecraft versions or other than latest plugin versions.**

## Features

✔ Add custom vehicles.<br>
✔ Adjust vehicle data in game.<br>
✔ Reloading/Restarting the server is possible with vehicles in the folder.<br>
✔ Gasoline function customizable in config.<br>
✔ All current vehicles + helicopters.<br>
✔ Tanks which can shoot TNTs (must be enabled in config.yml)!<br>
✔ ANWB - Disable picking up vehicles if it is in the water.<br>
✔ Trunks per vehicle & customizable.<br>
✔ Vehicle edit function.<br>
✔ Adjustable speed per vehicle.<br>
✔ Vouchers for vehicles.<br>
✔ Vehicles can take damage and be broken (must be enabled in config.yml).<br>
✔ Integration with WorldGuard and Vault.<br>
✔ Active support/developers.<br>
✔ Open Source.

## **Commands & Permissions**

### Main commands

| Command             | Description                                       | permission                                       |
|---------------------|---------------------------------------------------|--------------------------------------------------|
| `/vehicle info`     | Get information about a vehicle                   | `None`, `mtvehicles.admin` to display admin info |
| `/vehicle help`     | Get a list of all the possible commands           | `None`, `mtvehicles.admin` to display admin info |
| `/vehicle version`  | Get info about the plugin *(use this on discord)* | `mtvehicles.admin`                               |
| `/vehicle language` | Change the plugin language                        | `mtvehicles.admin` or `mtvehicles.language`      |

### Vehicle specific commands
These commands can only be executed by the owner of the vehicle. **Hold a vehicle in your hand while executing these.**

> Note: You may use some of these commands while **sitting inside** a vehicle, as long as you're the vehicle's owner.

| Command                          | Description                                          | Permission |
|----------------------------------|------------------------------------------------------|------------|
| `/vehicle public`                | Let everyone sit in your car                         | `None`     |
| `/vehicle private`               | Only allow people who have access to sit in your car | `None`     |
| `/vehicle addrider %player%`     | Add a rider to your vehicle                          | `None`     |
| `/vehicle addmember %player%`    | Add a member to your vehicle                         | `None`     |
| `/vehicle removerider %player%`  | Remove a rider from your vehicle                     | `None`     |
| `/vehicle removemember %player%` | Remove a member from your vehicle                    | `None`     |
| `/vehicle trunk`                 | Open the trunk of your vehicle                       | `None`     |
| `/vehicle repair`                | Restore the health of a vehicle (vehicle damage must be enabled in the [config.yml]) | `mtvehicles.repair` |
| `/vehicle refill`                | Refill the vehicle's fuel                            | `mtvehicles.refill` |

### Moderation commands


| Command                                | Description                                                      | Permission               |
|----------------------------------------|------------------------------------------------------------------|--------------------------|
| `/vehicle edit`                        | Adjust vehicle settings                                          | `mtvehicles.edit`        |
| `/vehicle menu`                        | Open a menu containing all the vehicles                          | `mtvehicles.menu`        |
| `/vehicle givevoucher %player% %uuid%` | Give a voucher to a player                                       | `mtvehicles.givevoucher` |
| `/vehicle givecar %player% %uuid%`     | Give a vehicle to a player                                       | `mtvehicles.givecar`     |
| `/vehicle restore`                     | A menu to recover vehicles                                       | `mtvehicles.restore`     |
| `/vehicle benzine`                     | Open the menu with jerrycans for the fuel                        | `mtvehicles.benzine`     |
| `/vehicle givefuel %player% %amount%`  | Give a jerrycan to a specific player                             | `mtvehicles.givefuel`    |
| `/vehicle reload`                      | Reload the plugin's config files                                 | `mtvehicles.reload`      |
| `/vehicle setowner %player%`           | Set an owner of a vehicle                                        | `mtvehicles.setowner`    |
| `/vehicle delete`                      | Remove a vehicle in your hand from the data                      | `mtvehicles.delete`      |
| `/vehicle despawn %license-plate%`     | Despawn the vehicle from all worlds (doesn't remove from the DB) | `mtvehicles.despawn`     |
| `/vehicle update`                      | Update the plugin to the latest version                          | `mtvehicles.update`      |


### Other permissions

| Permission                        | Description                                                                                    |
|-----------------------------------|------------------------------------------------------------------------------------------------|
| `mtvehicles.ride`                 | Whether a player can ride others' vehicles                                                     |
| `mtvehicles.anwb`                 | Whether a player can pick up vehicles from water - useless unless ANWB turned on in config     |
| `mtvehicles.oppakken`             | Whether a player can pick up vehicles                                                          |
| `mtvehicles.filljerrycans`        | Whether a player can fill up a jerry can - can be turned on/off in config                      |
| `mtvehicles.filljerrycansforfree` | Whether a player can fill up a jerry can for free (Economy plugin + Vault necessary)           |
| `mtvehicles.limit.X`              | Number of vehicles obtainable from /vehicle menu (Change X to an integer, use * for unlimited) |

## **Softdependencies**

### WorldGuard

You can download <a href="https://dev.bukkit.org/projects/worldguard">WorldGuard</a> to get access to custom features with regions. (Do not forget that WorldGuard needs WorldEdit as its dependency.)

> ⚠ WorldGuard features are not supported on 1.12.2 as it is no longer maintained and uses older API version.

You can edit the behaviour **in the config** where you may also find information how to set up the custom flags.

> ⚠ WorldGuard features are disabled by default and have to be enabled in the config.

| Custom flags         | State | Description                                                                                   |
|----------------------|-------|-----------------------------------------------------------------------------------------------|
| `mtv-gasstation`     | ALLOW | This region is a gas station. (More information can be found in the [config.yml])             |
|                      | DENY  | You can NEVER fill up jerrycans here. No config option can change this.                       |
| `mtv-place`          | ALLOW | Enables players to place vehicles, **if region whitelist for placing is enabled.**            |
|                      | DENY  | Prevents players from placing vehicles, **if region blacklist for placing is enabled.**       |
| `mtv-enter`          | ALLOW | Enables players to enter vehicles, **if region whitelist for entering is enabled.**           |
|                      | DENY  | Prevents players from entering vehicles, **if region blacklist for entering is enabled.**     |
| `mtv-pickup`         | ALLOW | Enables players to pick up vehicles, **if region whitelist for picking up is enabled.**       |
|                      | DENY  | Prevents players from picking up vehicles, **if region blacklist for picking up is enabled.** |
|                      |       |                                                                                               |
| `mtv-use-car`        | DENY  | Prevents all players from using CARS in this region.                                          |
| `mtv-use-tank`       | DENY  | Prevents all players from using TANKS in this region.                                         |
| `mtv-use-hover`      | DENY  | Prevents all players from using HOVERS in this region.                                        |
| `mtv-use-helicopter` | DENY  | Prevents all players from using HELICOPTERS in this region.                                   |
| `mtv-use-airplane`   | DENY  | Prevents all players from using AIRPLANES in this region.                                     |

### PlaceholderAPI

MTVehicles offers special placeholders for you to use.

#### Global placeholders

| Placeholder                  | Description                                            |
|------------------------------|--------------------------------------------------------|
| `%mtv_fuel_pricePerLitre%`   | Price of fuel (used while filling jerrycans)           |

#### Per-player placeholders

| Placeholder                  | Description                                                   |
|------------------------------|---------------------------------------------------------------|
| `%mtv_vehicle_licensePlate%` | License plate of a vehicle a player **is sitting in**         |
| `%mtv_vehicle_name%`         | Name of a vehicle a player is sitting in                      |
| `%mtv_vehicle_type%`         | Type of a vehicle a player is sitting in                      |
| `%mtv_vehicle_fuel%`         | Remaining fuel of a vehicle a player is sitting in            |
| `%mtv_vehicle_speed%`        | Current speed of a vehicle a player is sitting in             |
| `%mtv_vehicle_maxspeed%`     | Maximum speed of a vehicle a player is sitting in             |
| `%mtv_vehicle_place%`        | Get information whether the player is a DRIVER or a PASSENGER |
| `%mtv_vehicle_seats%`        | Get the number of seats of a vehicle a player is sitting in   |
| `%mtv_vehicle_uuid%`         | Get the UUID of a vehicle a player is sitting in              |
| `%mtv_vehicle_owner%`        | Get the owner (username) of a vehicle a player is sitting in  |


### Vault

You can download <a href="https://www.spigotmc.org/resources/vault.34315/">Vault</a> to enable economy integration. (Of course, you must have an economy plugin that supports Vault.)

This includes:
* Filling up jerrycans at gas stations for a specified price (can be adjusted in config.yml)!
* Enabling vehicles' price (can be adjusted in vehicles.yml)
* Buying vehicles and vouchers (via `/mtv buycar` and `/mtv buyvoucher`)

#### Additional commands

| Command                       | Description                                             | Permission |
|-------------------------------|---------------------------------------------------------|-------|
| `/vehicle buycar %uuid%`      | Buy a vehicle (for the price specified in vehicles.yml) | `mtvehicles.buycar`    |
| `/vehicle buyvoucher %uuid%`  | Buy a voucher (which costs the same as a vehicle)       | `mtvehicles.buycar` |

### Skript

You can perform some basic tasks with the Skript API. See the [Skript page on MTVehicles Wiki](https://wiki.mtvehicles.eu/information/skript.html) for documentation.

<div align="right">
  <sub>README.md by <a href="https://github.com/Zettovec">Nikd0</a> & <a href="https://github.com/Jeffrey-H">Jeffrey-H</a></sub>
</div>

[config.yml]: https://github.com/GamerJoep/MinetopiaVehicles/blob/master/src/main/resources/config.yml
