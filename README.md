# SpigotDragonZ
Dragon Ball Z Plugin for Minecraft servers!

This plugin has many planned features:

  - Wide variety of Race's from the official show! (Current have Saiyan and Half-Saiyan)
  - RPG Style system, allowing you to level up and grow stronger!
  - Auras to surround you character while your ina form/charging (Planned, not added)
  - Transformations! Each race has its own specific sets of transformations! (Will be added after auras);
  - Original DragonBallZ saga system, play through the original storyline of the show and gain epic rewards and strength! (Planned)
  - NPC's to talk to, train with and learn new skills from! (Planned, not added);
  - Ki blasts with the possibility to fatally would your enemies and scar the surface of the world! (Planned not added)
  - Hyper-Bolic time training world, train faster in this special world! (Planned, not added);
  - PlaceholderAPI support to allow for ultimate customization! (Planned, not added)

## Important Notes
  This plugin is a OVERHAUL plugin, meaning damage incoming to players is recalculated to scale appropriately with the
plugin! This may have unintended side effects with other plugins that can manipulate EntityDamageEvent.

This plugin was designed with modularity and performance in mind! (While there is not Database Support yet) This plugin utilizes a cache to 
read from JSON player data folder, as well as asynchronous file writing to ensure your main thread is never blocked!
We also clean up events that not longer need to be used and unregistering them, meaning you won't encounter memory leaks using my plugin!

As mentioned, this plugin is coded to be easy to read by anyone (It basically has its own API it can even be accessed by other plugins!), while PR requests
are always welcomed, I recommend you contact me on discord (D B#0001) before starting your work, as there is a good possibility that I am already in the process
of adding certain features and or fixing a known issue! If you find a bug I recommend to open a Issue instead of a PR and i will fix it personally :)
