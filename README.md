# PEX-Rankup
A lightweight rankup plugin designed for PEX based prison servers.

### New Placeholder Expansion:
Important Note: DO NOT USE /plugman reload PEX-Rankup if you use this expansion. Please restart the server to replace the PEX-Rankup.jar file.

For those who have requested to have me upload my placeholders to PlaceholderAPI so you can put variables like a player's current group, their next rank, and costs in PlaceholderAPI compatible plugins, here you go:
https://www.spigotmc.org/resources/rankupexpansion.29500/

### This plugin requires:

* Java 7/8
* Spigot 1.8.x to 1.10.x
* Vault
* PermissionsEX

### Command / Permission:

* /rankup | ru
    - pexrankup.rankup
* /ranks
    - pexrankup.ranks
* /setrank
    - pexrankup.setrank
* /pr-reload
    - pexrankup.reload

### Placeholders:

* {username} - refers to the affected player's username.
* {rank} - refers to the affected player's new rank.
* {oldrank} - refers to the affected player's old rank. (Have not tested yet, so please tell me if this placeholder does not work)

### Developer API:

* https://github.com/devhid/PEX-Ranku.../java/net/devhid/pexrankup/api/RankupAPI.java

### Important Notes:

Only use execute-commands option from configuration file if ranking up from the plugin is not working or if you need to execute a different command that does not involve setting someone's rank.
    
The recommended way to handle multiple groups is using "group set" for A-Z ranks and "group add" for any other ranks (donor, staff ranks etc.). The user will still rankup regardless of how you have your multiple ranks setup but you might run into other problems.
