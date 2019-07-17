# Bomberman Battle
Bomberman Battle is a classic game which is highly influenced by Neo Bomberman (1997) developed by Hudson Soft. Bomberman Battle supports up to 4 human players via network multiplayer. It also supports 2 human players via keyboard sharing. Game of more than one human players and some artificial intelligence (AI) players is also supported via both online and offline, which was absent in the battle mode of Neo Bomberman.


## Play the game

<img src="Cover Art.png" width="600" alt="Bomberman Battle Cover Art" title="Bomberman Battle Cover Art"/>

### Download the game
To play the game, please go to the [latest release](../../releases/latest) and download it. Run the downloaded JAR file to play the game.

### Get started
If you are playing for the first time, it is better to play offline for practice. *Newbie* type in offline mode is specially designed for practice. To know all control keys, please go to *Settings* &rightarrow; *Control*. For learning purposes, you can just see how computer players win or lose by setting 0 human players in the *Normal* type. When you are ready, you can play either in online or offline mode. Note that in both modes, there is no 'pause' option! You can just close the game.

### General rules
Each player needs to survive and defeat other players. The last survival will be the winner. You can take some strategic roles to survive (such as putting bombs in the places where more players can be defeated). Beware that your own bombs can destroy you too!

### Multiplayer
In online mode, up to 4 players can play the game simultaneously via a network (such as WLAN, LAN or the Internet). One player needs to create a game either by peer-to-peer (P2P) or by client-server (CS) connection type. P2P connection type is preferable when possible. Other players need to join the game via the player's IP address. Computer players can play in online mode too. For connection via ISPs, note that if different players use different ISPs, they may not connect each other. For more help or troubleshooting, please see the documentation of your operating system and the programs you use for networking purposes.

### Watch gameplay videos
I have recorded some games against AI players. You may find them in the [YouTube playlist](https://www.youtube.com/playlist?list=PLUnMUWgFJuSCVWxdIZpsEBMaWKzOpS-Cb). Here is a gameplay video of this game from the playlist (click to watch):

<a href="https://www.youtube.com/watch?v=Cjx6MpoXwc0&list=PLUnMUWgFJuSCVWxdIZpsEBMaWKzOpS-Cb" target="_blank">
  <img src="https://img.youtube.com/vi/Cjx6MpoXwc0/hqdefault.jpg" width="600" alt="Bomberman Battle Gameplay video" title="Bomberman Battle Gameplay - Click to Watch"/>
</a>


## Build Project
### Requirements:
- JDK 11 or later
- Maven, or any Java IDE which supports Maven

### Build steps:
1. If you want to use Maven directly (without any IDE), run the following command in the project directory where `pom.xml` file exists:
    ```posh
    mvn package
    ```
    Or, you can use any supported Java IDE (like IntelliJ, Eclipse) to build the project.

2. If the build process ends successfully, it will generate `bomberman-battle-1.0-beta-5.jar` file in `/target` directory.