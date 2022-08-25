# Origins: Accessibilities
An add-on for the [Origins (Forge)](https://github.com/EdwinMindcraft/origins-forge) mod making your Origins life easier.

Inspired by [Origins: Environmental Armor](https://github.com/MagicQuartz/EnvironmentalArmor) and [Origins: Umbrella](https://github.com/Fusion-Flux/Origins-Umbrellas).

## Features

### Items

#### Umbrella
- Can be crafted using Stick and Phantom membrane.
- Protects players from rain and sunlight, which is exactly what Blazeborn, Enderian and Phantom need.
- Should be equipped in hands to function.
- Slowly loses durability in rain but won't get completely broken
- Slowly regains durability when exposed to normal weather, hot environments could accelerate the process.
- Needs to be dyed to a color dark enough to keep out sunlight, and it won't cost durability.
- Propeties can be configured in server config.

#### Goggles
- Defined in tag `forge:goggles` can be used to remove Enderian's visual problem on pumpkins.
- Origins: Accessiblity hasn't implemented any goggles, but Create's goggle is supported by default.

#### Diving and Landwalking Helmets
- Can be crafted using Copper Ingot, Glass and Fried Kelp.
- Water can be poured from Landwalking helmet like Water Bucket, and vice versa for Diving Helemt.
- When in water: Diving Helmet provide Water Breathing for non-water-breathing Origins and slowly gets filled with water,
and eventually turn into Landwalking Helmet.
- When outside water: Landwalking Helmet provides Water Breathing for water-breathing Origins and slowly loses water,
and eventually turn into Diving Helmet.
- Respiration enchantment can slow down the transformation progress, making the helmet works longer.
- Propeties can be configured in server config.

#### Chain Armors and Chain Membrane
- Chainmail Armors is now craftable
- A new kind of Chainmembrane Armor can be crafted using Chain and Phantom Membrane.
- Chainmembrane Armors has the same armor values like chainmail armor, but with high armor toughness values,
providing the best protection for Elytrian.

### Enchantments

#### Flying Protection
- Protection enchantment which excluisvely protects player from "Fly into wall" damage.

### Effects

#### Water Resistance
- Protects players from being damaged by water, potions can be brewed using Water Breathing potion and Scute.

#### Sun Resistance 
- Protects players from being affected by sunlight, potions can be brewed using Night Vision potion and Glow Ink Sac.

#### Fresh Air
- Removes Avian's sleeping height limit, the only source is Create's air current.

### Diet Logics
- Carnivore and Vegetarian now have a more reasonable and configurable diet restriction.
- Diet rules can be switched between Strict and Loose through server config.
- Strict: Carnivore can only eat Meat, while Vegetarian can only eat Vegetables.
- Loose: Carnivore can eat anything besides Vegetables, while Vegetarian can eat anything besides Meat.
- Meat: Defined by builtin item condition `origins:meat` and tag `origins:meat`, supports vanilla meat and some specific mod meat by default.
- Vegetables: Defined by tag `origins:vegetarian_diet`, supports vanilla food by default.
- It is recommended to use Loose mode if you have food mods and haven't got a datapack to add mod foods to tags,
but once you've got a compatible datapack working, Strict mode will provide a more balanced experience

## Contributing
For issues: Follow the issue template, and it shall be fine.

For pull requests: I'm open to PRs. If you'd like to contribute with the mod, feel free to open a PR!
