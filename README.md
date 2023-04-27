Memory Game
==========

Project initially has been created to **learn basics of Java**. However recently I have decided to **refactor it**. Code became legacy, but helped me with begining my IT career.

Project is just a famous game, memory. Frequently speaking, it is a card game in which all of the cards are laid face down on a surface and two cards are flipped face up over each turn. The object of the game is to turn over pairs of matching cards. That's all.

![Old version of Memory Game](/doc/old_memory.png)

After first iteration, code was just a mess. UI was tangled with game logic. But it was working! Code was simply created according to the idea, "to learn how to program, just program". But nowadays it looks terrible for me. It shows me how much I have learnt since writing last line of code in this project.

Here is new solution. I started exploring and found out that I have something in this game like **flat item** which has **obverse and reverse**.

```java
public final class FlatItem {

    private enum Side {
        Reverse, Obverse
    }

    private final FlatItemId flatItemId;
    private Side side;

    ...

```

It is **Entity from DDD**. We have to know which card we flip. API looks something like this.

```java
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

User of our code don't have to know that we have something like `Side enum`. We just show him **behaviours**, which he can use.

Then there is group of flat items, which aggregates them. Just like for flat item I coded group as **Entity**. We have to know which group we check for being all flat items obverse up.

```java
    public void turnAllToReverseUp() {
        flatItems.forEach(FlatItem::turnReverseUp);
    }

    public void turnToObverse(FlatItemId flatItemId) {
        FlatItem flatItem = flatItems.stream()
                .filter(item -> item.getFlatItemId().equals(flatItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("flat item " + flatItemId + " does not belong to group"));
        flatItem.turnObverseUp();
    }
```

Last one element of game is class `MemoryGame` which aggregates all defined groups. There are also rules of the game. We check if we can continue guessing or we wrongly turned next card. Also there is mechanism to decide if game is over. But there is **no UI or even gaining points for game**. It is just pure engine of the memory game called [Concentration](https://en.wikipedia.org/wiki/Concentration_\(card_game\)).

Then we have GUI made in old, good Swing. I've defined some components, but the most important one is `CardsPanel'. This is hearth of GUI. In this section we show user cards to guess. What is worth to mention is that I use design pattern `Observer`. Using this pattern allowed me to exchange information between GUI components dinamically.

But how the `CardsPanel` renders elements? First of all it sends commands to engine which changes it state. Then this component queries engine for current state. It sounds just like **CQS**. This separation simplifies code which lowers cognitive load of programmer.

![Jungle version of large size game](/doc/new_memory_1.png)

![Earth version of medium size game](/doc/new_memory_2.png)

![Premiere League version of small size game](/doc/new_memory_3.png)

![English clubs version end game with 3 cards in group](/doc/new_memory_4.png)

Above I put some versions of game. I've decided to add some options like **changing size of board, obverse and reverse themes** and, what is the most interesting one, **number of cards in groups**. It allowes us not to only search for pairs but also for three or four cards in groups. This is possible because of generic solution in engine. We did not hardcode the number of cards in group, but made it more elastic.

I hope you will enjoy playing my version of game Memory just like I did!
