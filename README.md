<h1 align="center">MinetopiaVehicles</h1>
<p align="center"><em>A realistic vehicles plugin for your Minecraft server!</em></p>

<div align="center">

<a href="https://github.com/MTVehicles/MinetopiaVehicles/releases"><img alt="GitHub release (latest by date including pre-releases)" src="https://img.shields.io/github/v/release/MTVehicles/MinetopiaVehicles?include_prereleases&label=latest"></a>
<a href="https://www.spigotmc.org/resources/80910/"><img alt="Spiget Version" src="https://img.shields.io/spiget/version/80910?label=latest-aternos"></a>
<a href="https://www.spigotmc.org/resources/80910/"><img alt="Spiget Version" src="https://img.shields.io/spiget/downloads/80910"></a>
<a href="https://www.spigotmc.org/resources/80910/"><img alt="Spiget Stars" src="https://img.shields.io/spiget/stars/80910?color=yellow"></a>
---  

### ❗USE `/VEHICLE LANGUAGE` TO CHANGE THE LANGUAGE❗</h3>
Messages in the plugin can be displayed in multiple languages. (Default: English) You can find them <a href="https://github.com/GamerJoep/MinetopiaVehicles/tree/master/src/main/resources/messages">here</a>.

---

### We support all major Paper versions from 1.12.2!

</div>

✅ For the list of compatible versions, **[see wiki](https://wiki.mtvehicles.eu/faq.html#_5-i-am-getting-an-internal-error-occurred-error)**. <br>
✅ We support **latest patch versions** – e.g. 1.16.5 is supported, but 1.16.1 is not. <br>
✅ We only support **Spigot/Paper**. Other forks such as Purpur and Leaf are not supported. <br>
❌ We don't support 1.14.x due to compatibility issues. <br>
❌ 1.12.1 and lower is NOT a will NOT be supported!

---

## Useful links

### See our new [wiki](https://wiki.mtvehicles.eu) or contact us via [Discord](https://discord.gg/vehicle) for support!

<a href="https://discord.gg/vehicle" taget="_blank"><img src="https://discord.com/api/guilds/694809233819959308/widget.png?style=banner2"></a>

- 🌍 **Website:** https://mtvehicles.eu/ <br>
- 💡 **Wiki:** https://wiki.mtvehicles.eu <br>
- 🕹️ **Spigot:** https://www.spigotmc.org/resources/80910/ <br>
- 🚙 **Resource pack:** https://mtvehicles.nl/resourcepack/ <br>
- ⤴️ **Auto-Updater:** https://github.com/MTVehicles/auto-updater (Since 2.5.5 – if you're using an older version, you have to update to 2.5.5 and only then can you update to newer versions.)

> ⚠️ **You will not receive any support regarding unsupported Minecraft versions or other than latest plugin versions!**

---

## How to set up the plugin?

### 1. Download the latest version
- Latest stable releases are always published on <a href="https://www.spigotmc.org/resources/80910/">Spigot.org</a>.

Put the downloaded plugin into your server's `plugins/` folder.

### 2. Download the resource pack
- **Download the resource pack.** Use the link https://mtvehicles.nl/resourcepack/! (We've also created a&nbsp;<a href="https://www.youtube.com/watch?v=rSXNd-6rhQk">YouTube tutorial</a>).

You may set it as a server resource pack, or everyone would have to download the resource pack individually. (For instance, take a look <a href="https://apexminecrafthosting.com/how-to-upload-a-resource-pack/" taget="_blank">here</a>.)

> ⚠ The vehicles WON'T look properly without the resource pack.

### 3. Restart the server
Restart the server. No explanation needed, right? Right?

### 4. Enjoy!
**Et voilà, it's working!** Don't forget to set your language using `/vehicle language`.<br>

If this did not work don't hesitate to contact the support on our Discord server.

---

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
| `/vehicle give %player% %vehicle% [--voucher:true]` | Give a vehicle/voucher to a player (uses tab completer) | `mtvehicles.givevoucher` or `mtvehicles.givecar` |
| `/vehicle restore`                     | A menu to recover vehicles                                       | `mtvehicles.restore`     |
| `/vehicle benzine`                     | Open the menu with jerrycans for the fuel                        | `mtvehicles.benzine`     |
| `/vehicle givefuel %player% %amount%`  | Give a jerrycan to a specific player                             | `mtvehicles.givefuel`    |
| `/vehicle reload`                      | Reload the plugin's config files                                 | `mtvehicles.reload`      |
| `/vehicle setowner %player%`           | Set an owner of a vehicle                                        | `mtvehicles.setowner`    |
| `/vehicle delete`                      | Remove a vehicle in your hand from the data                      | `mtvehicles.delete`      |
| `/vehicle despawn %license-plate%`     | Despawn the vehicle from all worlds (doesn't remove from the DB) | `mtvehicles.despawn`     |
| `/vehicle update`                      | Update the plugin to the latest version                          | `mtvehicles.update`      |
| ~~`/vehicle givevoucher %player% %uuid%`~~ | Give a voucher to a player (Use /vehicle give)               | `mtvehicles.givevoucher` |
| ~~`/vehicle givecar %player% %uuid%`~~     | Give a vehicle to a player (Use /vehicle give)               | `mtvehicles.givecar`     |


### Other permissions

| Permission                        | Description                                                                                        |
|-----------------------------------|----------------------------------------------------------------------------------------------------|
| `mtvehicles.ride`                 | Whether a player can ride others' vehicles                                                         |
| `mtvehicles.anwb`                 | Whether a player can pick up vehicles from water - useless unless ANWB turned on in config         |
| `mtvehicles.oppakken`             | Whether a player can pick up vehicles                                                              |
| `mtvehicles.filljerrycans`        | Whether a player can fill up a jerry can - can be turned on/off in config                          |
| `mtvehicles.filljerrycansforfree` | Whether a player can fill up a jerry can for free (Economy plugin + Vault necessary)               |
| `mtvehicles.limit.X`              | Number of vehicles obtainable from /vehicle menu (Change X to an integer, e.g. `mtvehicles.limit.6`) |
| `mtvehicles.nolimit`              | No limit for vehicles obtainable from /vehicle menu (in case of issues)                            |

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
* Buying vehicles and vouchers (with `/mtv buy` – or /mtv buycar and /mtv buyvoucher, if you wish to use UUID)

#### Additional commands

| Command                       | Description                                             | Permission |
|-------------------------------|---------------------------------------------------------|-------|
| `/vehicle buy %vehicle% [--voucher:true]` | Buy a vehicle/voucher (uses tab completer)  | `mtvehicles.buyvoucher` or `mtvehicles.buycar` |
| ~~`/vehicle buycar %uuid%`~~      | Buy a vehicle (for the price specified in vehicles.yml) | `mtvehicles.buycar`    |
| ~~`/vehicle buyvoucher %uuid%`~~  | Buy a voucher (which costs the same as a vehicle)       | `mtvehicles.buyvoucher` |

### Skript

You can perform some basic tasks with the Skript API. See the [Skript page on MTVehicles Wiki](https://wiki.mtvehicles.eu/information/skript.html) for documentation.

<div align="right">
  <sub>README.md by <a href="https://github.com/Zettovec">Nikd0</a> & <a href="https://github.com/Jeffrey-H">Jeffrey-H</a></sub>
</div>

[config.yml]: https://github.com/GamerJoep/MinetopiaVehicles/blob/master/src/main/resources/config.yml
