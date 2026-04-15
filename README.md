# HardcoreLives

HardcoreLives is a straightforward plugin that adds a limited lives system to your Minecraft server. It’s built for SMPs or hardcore servers where you want death to have real consequences, but don't want to permanently ban your players.

When a player hits 0 lives, they get temp-banned for a set amount of time. To keep things balanced, players can use a specific item to add a life back to their account.

## Features
* **Custom Temp-Bans:** Ban players for a specific amount of time when they run out of lives.
* **Life Management:** Set how many lives a player starts with, what their maximum cap is, and how many they get back after their ban expires.
* **Custom 'Life' Items:** Define an item that players can right-click to gain +1 life. It fully supports `CustomModelData`, so you can easily hook it into resource packs or plugins like ItemsAdder, Oraxen, or CraftEngine.
* **PlaceholderAPI:** Use `%hardcorelives_lives%` to display remaining lives on scoreboards or tab lists.
* **Custom Messages:** All messages are fully translatable and support MiniMessage formatting (`<red>`, `<bold>`, etc.).

## Commands & Permissions
* `/lives` - Check your remaining lives.
* `/lives [player]` - Check another player's lives.
* `/lives add <player> <amount>` - Add lives to a player. *(Permission: `hardcorelives.admin`)*
* `/lives set <player> <amount>` - Override and set a player's lives. *(Permission: `hardcorelives.admin`)*

## Configuration (config.yml)
```yaml
starting_lives: 3
ban_hours: 24
max_lives: 5
lives_after_ban: 1

life_item:
  enabled: true
  material: "NETHER_STAR"
  custom_model_data: 1111 # Optional: Remove this line to allow standard vanilla items

messages:
  kick_ban: "<red>You ran out of lives! Banned for %hours% hours.</red>"
  death_lives_remaining: "<yellow>You died! Lives remaining: %lives%</yellow>"
  command_lives_self: "<green>You have %lives% lives remaining.</green>"
  command_lives_other: "<green>%player% has %lives% lives remaining.</green>"
  command_player_not_found: "<red>Player not found.</red>"
  item_used: "<green>Added 1 life! You now have %lives% lives.</green>"
  admin_set: "<green>Set %lives% lives for %player%.</green>"
  admin_add: "<green>Added lives. %player% now has %lives% lives.</green>"
  no_permission: "<red>No permission (hardcorelives.admin).</red>"
  invalid_number: "<red>Invalid number provided.</red>"
  invalid_usage: "<red>Usage: /lives [player] | /lives <set/add> <player> <amount></red>"
```
