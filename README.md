# BerryBasket

![Paper](https://img.shields.io/badge/Paper-1.21.11%2B-blue)
![Requires](https://img.shields.io/badge/requires-PyroFarming-orange)
![Nexo](https://img.shields.io/badge/Nexo-optional-lightgrey)

An addon for PyroFarming that adds a berry basket. Hit a berry on a PyroFarming bush while holding the basket and it breaks instantly.

<img width="426" height="240" alt="Berry Basket Example" src="https://github.com/user-attachments/assets/ec66bd44-674b-4389-940b-4614c9b73924" />

> [!NOTE]
> BerryBasket is not affiliated with or endorsed by PyroFarming or its developer.

## Features

- **Instant berry harvesting.** Berry heads on PyroFarming bushes break in one hit with the basket. Harvesting still goes through PyroFarming normally, so drops, XP, Elysium and regrowth all work like usual.
- **Respects protection.** Breaks go through the normal block break event, so Towny, WorldGuard, etc. still apply.
- **Quick menu access.** Shift right-click with the basket to open the PyroFarming menu.
- **Vanilla or Nexo item.** Use a built-in item, or point it at a Nexo item.
- **Fix command.** Swaps an outdated basket for the current version after you change the config.
  - *Use to update item names/lore, or swap to a different item provider (Nexo)*

## Requirements

| Plugin | |
|---|---|
| Paper 1.21.11+ | Required |
| PyroFarming | Required |
| PyroLib | Required (PyroFarming needs it anyway) |
| Nexo | Optional |

> [!WARNING]
> BerryBasket won't load without PyroFarming installed.

## Commands

| Command | Description | Permission | Default |
|---|---|---|---|
| `/berrybasket get` | Give yourself a basket | `berrybasket.get` | op |
| `/berrybasket give <player>` | Give a player a basket | `berrybasket.give` | op |
| `/berrybasket fix` | Update the basket in your hand to the current version | `berrybasket.fix` | everyone |
| `/berrybasket reload` | Reload the config | `berrybasket.reload` | op |

## Configuration

```yaml
items:
  # Choices vanilla, nexo (default vanilla)
  item-source: vanilla
  vanilla-item:
    name: "<green>Berry Basket"
    lore: "<!italic><gray>Instantly mines berries from PyroFarming"
    material: BOWL
  nexo-item:
    id: berry_basket

detection:
  # Checks that a FLOWERING_AZALEA_LEAVES block is below the head.
  check-below: true
  # Checks for head to have name Pyro-Lib.
  check-head-data: true

# Opens PyroFarming menu on shift right click
open-menu-on-shift-right-click: true
pyrofarming-menu-command: "pyrofarm menu"
```

Names and lore use [MiniMessage](https://docs.advntr.dev/minimessage/format.html) formatting.

If `item-source` is set to `nexo` but Nexo isn't installed, the plugin falls back to the vanilla item and logs a warning.

### Changing the basket

After editing the item in the config:

1. Run `/berrybasket reload`.
2. Players run `/berrybasket fix` while holding their old basket to get the new one.

> [!TIP]
> Switching from vanilla to Nexo works with `/berrybasket fix`. Changing the Nexo item **id** does not, since old baskets with the old id won't be recognized anymore.

## Building

```
./gradlew build
```

The jar ends up in `build/libs/`.

## Feature Requests
Want support for another item plugin (ItemsAdder, Oraxen, etc.) or something else? Open an issue and I’ll take a look.
