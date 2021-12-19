# Multimedia-NTUA

<p align="center">
	<img alt="Byte Code Size" src="https://img.shields.io/github/languages/code-size/ChristosHadjichristofi/Multimedia-NTUA?color=red" />
	<img alt="# Lines of Code" src="https://img.shields.io/tokei/lines/github/ChristosHadjichristofi/Multimedia-NTUA?color=red" />
	<img alt="# Languages Used" src="https://img.shields.io/github/languages/count/ChristosHadjichristofi/Multimedia-NTUA?color=yellow" />
	<img alt="Top language" src="https://img.shields.io/github/languages/top/ChristosHadjichristofi/Multimedia-NTUA?color=yellow" />
	<img alt="Last commit" src="https://img.shields.io/github/last-commit/ChristosHadjichristofi/Multimedia-NTUA?color=important" />
</p>

**In this repo you can find both the src with gui and src without gui.**

In this course there was a project that we created the Battleship board game with some minor adjustments. The project asked to be written in Java using the OOP principles. Both the boards of the player and the bot are been read from files (from medialab folder).

* When the game starts, one of the players is randomly selected to make the first move. Both bot and the player can do max 40 moves. Game ends either when 40 moves are reached or when either the bot or the player sink the ships of his opponent.

* Although the project did not ask for different modes (Easy, Medium, Hard, Difficult) i implemented them so the player can choose the difficulty of the bot.

* To make a move you can either click on opponent's (bot) board or from the input boxes and click shoot.

* An other feature that i implemented was the 'Hint' (menu 'Hint'). The player can get a hint of where an enemy ship is (the enemy board will flash when hint is pressed). The player loses some moves when hint is used. More specifically the player loses X moves, where X is the amount of times he/she used the 'Hint'.

* In the end of the game, if the player could not sink the ships of the bot, all tiles which belong to a ship will change color to Gold.

* Player can see latest 5 moves he made and the latest 5 moves the bot made from the menu 'Details'. He can also see the status of the enemy (bot) ships from 'Details'.

* Player can (re)start, load, exit the game from the menu 'Application'.

# GUI

The gui is pretty simple and made with the help of SceneBuilder. Pictures following.

![MediaLab](https://user-images.githubusercontent.com/40044042/118411127-b8b52f00-b69b-11eb-850c-52b9b4ef8549.png)
![MediaLab_1](https://user-images.githubusercontent.com/40044042/118411166-dedacf00-b69b-11eb-89f1-b5560dce3c90.png)
