Genesys Coding Task
---
*5-in-a-Row, a variation of the famous Connect Four game, is a two-player connection game
in which the players first choose a color and then take turns dropping colored discs from the
top into a nine-column, six-row vertically suspended grid. The pieces fall straight down,
occupying the next available space within the column. The objective of the game is to be the
first to form a horizontal, vertical, or diagonal line of five of one's own discs.*

*Developed on Windows, in the Eclipse IDE*
*Java version 8*

---

**How to Run**
1. Clone/download the project
2. Run the server class first, followed by the client (best way would be compile the server class using 'javac FiveInARow.java' followed by 'java FiveInARow.java' in a command prompt to run it). You can then run the client class in your preferred IDE.
3. Descriptions of the various features can be found in each class

---

**Notable Issues**
One issue I ran into was trying to displaying the game board on the client side. I attempted to send the game object to the client using various methods, such as:
- https://gist.github.com/chatton/14110d2550126b12c0254501dde73616
- https://stackoverflow.com/questions/37370860/send-object-over-socket
- https://www.google.com/search?q=sending+objects+over+socket+java&oq=sending+objects+&aqs=chrome.0.0j69i57j0l6.4745j0j4&sourceid=chrome&ie=UTF-8

Unfortunately, this is something I wasn't able to achieve in the given time. It is something I would be looking to improve going forward.

**Things to improve**
- Host the server - eliminates the need to locally run it
- Add the ability to define a board size
- Improve UI - use of Java swing?
