# ConnectFour
## Assumptions
* Assume board  standard size is 6 * 7.  -> 6 rows & 7 columns 
* We use "Long" data type to represent our board for a player as we need just 42 bits (6 * 7) and a bit will be set to 1 if a player plays in this position and 0 otherwise. In favor of saving memory due to recursion steps compared to dealing with string or 2d array or all of ilk which require much more storage space.
## Heuristics
> Our heuristic consists of 3 factors, each factor may have one or more features.
1. The first factor is to count the number of consecutive ones and assign each count to a value, for example we assign values : 10 for 2 , 100 for 3 and so on up to 7 (maximum possible).
2. The second factor is to count the number of plays that offend other plays. To make it clear, if you put a disc next to the opponent's disc, we assign a value of 10 to it, and if it is placed next to 3, we assign a value of 100 and 1000 for the larger values (as 4 or 5 or 6 are the same).
3. The third factor is that each board counts the agent moves that deprive the player from maximizing his/her score. We call it <B>destroying<B>.
## How to execute 
1. Download Javafx packages.
2. Add javafx lib folder path to referenced libraries in the project.
3. Then run Main.java. 
> Java jdk used <B>jdk1.8.0_111<B>  and Javafx sdk version <B>javafx-sdk-17.0.1<B>
-------------------
![image](https://user-images.githubusercontent.com/58639073/145471024-a0d35f15-126d-4af9-9805-c6a425fbdea2.png)
![image](https://user-images.githubusercontent.com/58639073/145471105-a0c73c60-72f5-4a1b-a634-f452223c17b3.png)
![image](https://user-images.githubusercontent.com/58639073/145471306-cf8087af-4372-44a5-86e6-fdc2771c1d21.png)
![image](https://user-images.githubusercontent.com/58639073/145471141-88550989-c4bf-410b-8880-907f3926555e.png)
![image](https://user-images.githubusercontent.com/58639073/145471192-27742e8b-ca1e-4d4c-a8d3-82cae936a337.png)


https://user-images.githubusercontent.com/58639073/145471853-7e11ea3e-a690-4137-935b-7990a54f1cfe.mp4

