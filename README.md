Memory Game
==========

Project initially has been created to **learn basics of Java**. However recently I have decided to **refactor it**. Code became legacy, but helped me with begining my IT career.

Project is just a famous game, memory. Frequently speaking, it is a card game in which all of the cards are laid face down on a surface and two cards are flipped face up over each turn. The object of the game is to turn over pairs of matching cards. That's all.

![Old version of Memory Game](/doc/memory.png)

After first iteration, code was just a mess. UI was tangled with game logic. But it was working! Code was simply created according to the idea, "to learn how to program, just program". But nowadays it looks terrible for me. It shows me how much I have learnt since writing last line of code in this project.

Here is new solution. I started exploring and found out that I have something in this game like flat item which has obverse and reverse.

```
public final class FlatItem {

    private enum Side {
        Reverse, Obverse
    }

    private final FlatItemId flatItemId;
    private Side side;

    ...

```

It is entity from DDD. We have to know which card we flip. API looks like this for this class.

```
    public void flip() {
        side = side == Side.Obverse ? Side.Reverse : Side.Obverse;
    }

    public void turnObverseUp() {
        side = Side.Obverse;
    }

    public void turnReverseUp() {
        side = Side.Reverse;
    }
```

User of our code don't have to know that we have something like `Side enum`. We just show him behaviours which he can use. 
