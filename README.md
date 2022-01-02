<h1 align="center">MinetopiaVehicles</h1>
<p align="center"><em>A realistic vehicles plugin for your Minecraft server!</em></p>

<div align="center">

<a href="https://www.spigotmc.org/resources/80910/"><img alt="Spiget Version" src="https://img.shields.io/spiget/version/80910?label=version"></a>
<a href="https://github.com/Zettovec/MinetopiaVehicles/releases"><img alt="GitHub release (latest by date including pre-releases)" src="https://img.shields.io/github/v/release/Zettovec/MinetopiaVehicles?include_prereleases&label=pre-release"></a>
<a href="https://www.spigotmc.org/resources/80910/"><img alt="Spiget Version" src="https://img.shields.io/spiget/downloads/80910"></a>
<a href="https://www.spigotmc.org/resources/80910/"><img alt="Spiget Stars" src="https://img.shields.io/spiget/stars/80910?color=yellow"></a>
---  

### ❗FOR ENGLISH, DO /VEHICLES LANGUAGE❗</h3>
This plugin's main language is dutch, to switch execute the command `/vehicle language`

---

</div>

## How to download?

The plugin works on all spigot versions from 1.12.2 - 1.18.1, **except 1.14.x**!

### Be aware!


If your server minecraft version is not supported by the plugin, it won't load.

Versions 1.12.1 and lower are not supported, and will not be supported.<br>
(Did you know that 1.8 was released in 2014? And some people still use it! Whoa.)


```
We support ALL LATEST PATCH VERSIONS of Spigot/Paper 1.12+, excluding 1.14.4.
For example 1.16.5 IS supported, but 1.16.3 is not (and similarly with all other versions).
```

### 1. Download the latest version
- Latest stable releases are always published on <a href="https://www.spigotmc.org/resources/80910/">Spigot.org</a>.

Put the downloaded plugin into your server's `plugins/` folder.

### 2. Download the resource pack
- **Download the resource pack.** Link can be found on our <a href="https://discord.gg/vehicle">Discord server</a> (
  We've created a&nbsp;<a href="https://www.youtube.com/watch?v=rSXNd-6rhQk">YouTube tutorial</a>).

You may set it as a server resource pack, or everyone would have to download the resource pack individually.

> ⚠ The vehicles WON'T look properly without the resource pack.

### 3. Restart the server
Restart the server. No explanation needed right?

### 4. Enjoy!
**Et voilà, it's working!** Don't forget to set your language using `/vehicle language`.<br>

If this did not work don't hesitate to contact the support on our Discord server.

## Contact us!

**Discord is the best place to report discovered bugs!**
<a href="https://www.youtube.com/channel/UCxxK71QFN4_PrBhCFmH2Jmw"><img widhtsrc="https://raw.githubusercontent.com/MikeCodesDotNET/ColoredBadges/master/png/streaming/youtube%402x.png"></a></br>
<a href="https://discord.gg/vehicle" taget="_blank"><img src="https://discord.com/api/guilds/694809233819959308/widget.png?style=banner2"></a>

Other ways to contact us:

<a href="https://mtvehicles.nl">Website</a><br>
<a href="https://discord.gg/vehicle">Discord</a>

<a href="https://discord.gg/vehicle">Spigot</a><br>
<a href="https://bstats.org/plugin/bukkit/MTVehicles/5932">bStats</a>

> **You will not receive any support regarding unsupported Minecraft versions or other than latest plugin versions.**

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
✔ Active support/developers.<br>
✔ Open Source.

## **Commands & Permissions**

### Main commands

| Command           | Description                             | permission                                       |
|-------------------|-----------------------------------------|--------------------------------------------------|
| /vehicle info     | Get information about a vehicle         | `None`, `mtvehicles.admin` to display admin info |
| /vehicle help     | Get a list of all the possible commands | `None`, `mtvehicles.admin` to display admin info |
| /vehicle version  | Get the server and plugin version       | `mtvehicles.admin`                               |
| /vehicle language | Change the plugin language              | `mtvehicles.language`                            |

### Vehicle specific commands
These commands can only be executed by the owner of the vehicle

| Command               | Description                                          | permission |
|-----------------------|------------------------------------------------------|------------|
| /vehicle public       | Let everyone sit in your car                         | `None`     |
| /vehicle private      | Only allow people who have access to sit in your car | `None`     |
| /vehicle addrider     | Add a rider to your vehicle                          | `None`     |
| /vehicle addmember    | Add a member to your vehicle                         | `None`     |
| /vehicle removerider  | Remove a rider from your vehicle                     | `None`     |
| /vehicle removemember | Remove a member from your vehicle                    | `None`     |

### Moderation commands

| Command              | Description                                 | permission               |
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

| Permission            | Description                                                                                |
|-----------------------|--------------------------------------------------------------------------------------------|
| `mtvehicles.ride`     | Whether a player can ride others' vehicles                                                 |
| `mtvehicles.anwb`     | Whether a player can pick up vehicles from water - useless unless ANWB turned on in config |
| `mtvehicles.oppakken` | Whether a player can pick up vehicles                                                      |


<div align="right">
  <sub>README.md by <a href="https://github.com/Zettovec">Nikd0</a> & <a href="https://github.com/Jeffrey-H">Jeffrey-H</a></sub>
</div>
