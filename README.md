# Multimedia-NTUA

In this course there was a project that we created the Battleship board game with some minor adjustments. The project asked to be written in Java using the OOP principles. Both the boards of the player and the bot are been read from files (from medialab folder).

* When the game starts, one of the players is randomly selected to make the first move. Both bot and the player can do max 40 moves. Game ends either when 40 moves are reached or when either the bot or the player sink the ships of his opponent.

* Although the project did not ask for different modes (Easy, Medium, Hard, Difficult) i implemented them so the player can choose the difficulty of the bot.

* To make a move you can either click on opponent's (bot) board or from the input boxes and click shoot.

* An other feature that i implemented was the 'Hint'. The player can get a hint of where an enemy ship is (the enemy board will flash when hint is pressed). The player loses some moves when hint is used. More specifically the player loses X moves, where X is the amount of times he/she used the 'Hint'.
